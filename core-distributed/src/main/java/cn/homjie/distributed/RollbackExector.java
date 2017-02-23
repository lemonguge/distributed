package cn.homjie.distributed;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import cn.homjie.distributed.api.ForkTaskInfo;
import cn.homjie.distributed.api.TaskResult;
import cn.homjie.distributed.api.exception.RollbackFailureException;
import cn.homjie.distributed.api.exception.RollbackSuccessException;

public class RollbackExector<T> extends AbstractExector<T> {

	@Override
	protected void first(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception {
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

		sendEx(info, ex);

		// 回滚是否出现异常
		boolean rollbackException = false;
		List<ForkTask<?>> tasks = distributed.getTasks();
		for (ForkTask<?> forktask : tasks) {
			if (task == forktask)
				break;
			NulExecutable rollback = forktask.getRollback();
			if (rollback != null) {
				try {
					rollback.handleWithoutResult();
					info.setTaskStatus(TaskStatus.ROLLBACK_SUCCESS.name());

					sendOk(info);
				} catch (Exception e) {
					rollbackException = true;
					info.setTaskStatus(TaskStatus.ROLLBACK_EXCEPTION.name());
					info.setStackTrace(ExceptionUtils.getStackTrace(e));

					sendEx(info, e);
				}
			} else {
				info.setTaskStatus(TaskStatus.ROLLBACK_NOTFIND.name());

				sendOk(info);
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
	protected void retry(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception {
		if (TaskStatus.ROLLBACK_FAILURE.name().equals(info.getTaskStatus())) {
			task.getBusiness().handle();
		} else if (TaskStatus.ROLLBACK_EXCEPTION.name().equals(info.getTaskStatus())) {
			try {
				task.getRollback().handleWithoutResult();
				info.setTaskStatus(TaskStatus.ROLLBACK_SUCCESS.name());

				sendOk(info);
			} catch (Exception e) {
				info.setTaskStatus(TaskStatus.ROLLBACK_EXCEPTION.name());
				info.setStackTrace(ExceptionUtils.getStackTrace(e));

				sendEx(info, e);
			}
		}
	}

}
