package cn.homjie.distributed.api;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import cn.homjie.distributed.ForkTask;
import cn.homjie.distributed.ForkTaskInfo;
import cn.homjie.distributed.NulExecutable;
import cn.homjie.distributed.TaskResult;
import cn.homjie.distributed.TaskStatus;
import cn.homjie.distributed.api.exception.DistributedException;
import cn.homjie.distributed.api.exception.RollbackFailureException;
import cn.homjie.distributed.api.exception.RollbackSuccessException;
import cn.homjie.distributed.api.spring.SpringHolder;
import cn.homjie.distributed.domain.TaskInfoEntity;
import cn.homjie.distributed.rabbit.RabbitSender;

public enum Transaction {

	ROLLBACK {

		@Override
		public <T> void firstExec(ForkTask<T> task, ForkTaskInfo<T> info, List<ForkTask<?>> tasks) throws Exception {
			// 只有成功才有结果，否则抛出异常
			if (info.getResult() != null)
				return;

			Throwable ex = null;
			try {
				T result = task.getBusiness().handle();
				info.setResult(new TaskResult<T>(result));
				info.setTaskStatus(TaskStatus.SUCCESS.name());
				// 执行成功，立即返回
				return;
			} catch (RollbackFailureException e) {
				info.setTaskStatus(TaskStatus.ROLLBACK_FAILURE.name());
				info.setStackTrace(ExceptionUtils.getStackTrace(e.getCause()));
				ex = e;
			} catch (RollbackSuccessException e) {
				info.setTaskStatus(TaskStatus.ROLLBACK_SUCCESS.name());
				info.setStackTrace(ExceptionUtils.getStackTrace(e.getCause()));
				ex = e;
			} catch (Exception e) { // 原始异常
				info.setTaskStatus(TaskStatus.EXCEPTION.name());
				info.setStackTrace(ExceptionUtils.getStackTrace(e));
				ex = e;
			}

			send(info, ex);

			// 回滚是否出现异常
			boolean rollbackException = false;
			for (ForkTask<?> forktask : tasks) {
				if (task == forktask)
					break;
				NulExecutable rollback = forktask.getRollback();
				if (rollback != null) {
					try {
						rollback.handleWithoutResult();
						info.setTaskStatus(TaskStatus.ROLLBACK_SUCCESS.name());

						send(info);
					} catch (Exception e) {
						rollbackException = true;
						info.setTaskStatus(TaskStatus.ROLLBACK_EXCEPTION.name());
						info.setStackTrace(ExceptionUtils.getStackTrace(e));

						send(info, e);
					}
				} else {
					info.setTaskStatus(TaskStatus.ROLLBACK_NOTFIND.name());

					send(info);
				}
			}
			// 回滚失败的异常，仍然抛出回滚失败
			if (ex instanceof RollbackFailureException) {
				throw (RollbackFailureException) ex;
			} else {
				if (ex instanceof RollbackSuccessException) {
					ex = ex.getCause();
				}
				if (rollbackException) {
					throw new RollbackFailureException(ex);
				} else
					throw new RollbackSuccessException(ex);
			}

		}

		@Override
		public <T> void retryExec(ForkTask<T> task, ForkTaskInfo<T> info, List<ForkTask<?>> tasks) throws Exception {
			if (TaskStatus.ROLLBACK_FAILURE.name().equals(info.getTaskStatus())) {
				task.getBusiness().handle();
			} else if (TaskStatus.ROLLBACK_EXCEPTION.name().equals(info.getTaskStatus())) {
				try {
					task.getRollback().handleWithoutResult();
					info.setTaskStatus(TaskStatus.ROLLBACK_SUCCESS.name());

					send(info);
				} catch (Exception e) {
					info.setTaskStatus(TaskStatus.ROLLBACK_EXCEPTION.name());
					info.setStackTrace(ExceptionUtils.getStackTrace(e));

					send(info, e);
				}
			}
		}
	},

	EVENTUAL {

		@Override
		public <T> void firstExec(ForkTask<T> task, ForkTaskInfo<T> info, List<ForkTask<?>> tasks) throws Exception {
			// 只有状态不为空才有结果
			if (info.getTaskStatus() != null)
				return;
			exec(task, info);
		}

		@Override
		public <T> void retryExec(ForkTask<T> task, ForkTaskInfo<T> info, List<ForkTask<?>> tasks) throws Exception {
			String taskStatus = info.getTaskStatus();
			if (RETRY_STATUS.contains(taskStatus)) {
				exec(task, info);
			}
		}

		private <T> void exec(ForkTask<T> task, ForkTaskInfo<T> info) {
			try {
				T result = task.getBusiness().handle();
				info.setResult(new TaskResult<T>(result));
				info.setTaskStatus(TaskStatus.SUCCESS.name());

				send(info, result);
			} catch (DistributedException e) {
				info.setResult(new TaskResult<T>(e.getCause()));
				info.setTaskStatus(TaskStatus.EVENTUAL_IGNORE.name());

				send(info, e);
			} catch (Exception e) {
				info.setResult(new TaskResult<T>(e));
				info.setTaskStatus(TaskStatus.EVENTUAL_EXCEPTION.name());
				info.setStackTrace(ExceptionUtils.getStackTrace(e));

				send(info, e);
			}
		}

	};

	private static final Gson gson = new Gson();

	// EVENTUAL_FAILURE 进行重试分析后会将 SUCCESS 修改
	private static final List<String> RETRY_STATUS = Lists.newArrayList(TaskStatus.EVENTUAL_FAILURE.name(), TaskStatus.EVENTUAL_EXCEPTION.name(),
			TaskStatus.EVENTUAL_IGNORE.name());

	abstract <T> void firstExec(ForkTask<T> task, ForkTaskInfo<T> info, List<ForkTask<?>> tasks) throws Exception;

	abstract <T> void retryExec(ForkTask<T> task, ForkTaskInfo<T> info, List<ForkTask<?>> tasks) throws Exception;

	private static <T> void send(ForkTaskInfo<T> info, Throwable e) {
		RabbitSender<TaskInfoEntity> taskInfoSender = SpringHolder.getBean("taskInfoSender");
		TaskInfoEntity entity = new TaskInfoEntity();
		entity.setId(info.getId());
		entity.setDescriptionId(info.getDescriptionId());
		entity.setEx(gson.toJson(e));
		entity.setSuccess(false);
		entity.setTaskStatus(info.getTaskStatus());
		entity.setStackTrace(info.getStackTrace());
		try {
			taskInfoSender.send(entity);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	private static <T> void send(ForkTaskInfo<T> info, T t) {
		RabbitSender<TaskInfoEntity> taskInfoSender = SpringHolder.getBean("taskInfoSender");
		TaskInfoEntity entity = new TaskInfoEntity();
		entity.setId(info.getId());
		entity.setDescriptionId(info.getDescriptionId());
		entity.setResult(gson.toJson(t));
		entity.setSuccess(true);
		entity.setTaskStatus(info.getTaskStatus());
		entity.setStackTrace(info.getStackTrace());
		try {
			taskInfoSender.send(entity);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	private static <T> void send(ForkTaskInfo<T> info) {
		RabbitSender<TaskInfoEntity> taskInfoSender = SpringHolder.getBean("taskInfoSender");
		TaskInfoEntity entity = new TaskInfoEntity();
		entity.setId(info.getId());
		entity.setDescriptionId(info.getDescriptionId());
		entity.setSuccess(true);
		entity.setTaskStatus(info.getTaskStatus());
		entity.setStackTrace(info.getStackTrace());
		try {
			taskInfoSender.send(entity);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

}
