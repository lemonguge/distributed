package cn.homjie.distributed;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import cn.homjie.distributed.api.exception.ExceptionType;

import cn.homjie.distributed.api.ForkTaskInfo;
import cn.homjie.distributed.api.TaskResult;
import cn.homjie.distributed.api.exception.DistributedException;

public class RollbackExector extends AbstractExector {

	@Override
	protected <T> void first(ForkTask<T> task, ForkTaskInfo info, Distributed distributed) throws DistributedException {
		// 只有成功才有结果，否则抛出异常
		if (info.getResult() != null)
			return;

		Throwable ex = null;
		try {
			T result = task.getBusiness().handle();
			info.setResult(TaskResult.ok(result));
			info.setTaskStatus(TaskStatus.SUCCESS.name());
			// 执行成功，立即返回
			return;
		} catch (DistributedException e) {
			// 回滚成功的几率更大，减少判断
			if (e.isRollbackSuccess())
				info.setTaskStatus(TaskStatus.ROLLBACK_SUCCESS.name());
			else if (e.isRollbackFailure())
				info.setTaskStatus(TaskStatus.ROLLBACK_FAILURE.name());

			info.setStackTrace(ExceptionUtils.getStackTrace(e.getCause()));
			ex = e;
		} catch (Exception e) {
			// 原始异常
			info.setTaskStatus(TaskStatus.EXCEPTION.name());
			info.setStackTrace(ExceptionUtils.getStackTrace(e));
			ex = e;
		}

		sendEx(info, ex);

		// 回滚是否出现过异常
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

		if (ex instanceof DistributedException) {
			if (rollbackException)
				// 回滚失败，获取原始异常
				ex = ex.getCause();
			else
				throw (DistributedException) ex;
		}
		if (rollbackException)
			throw new DistributedException(ex, ExceptionType.ROLLBACK_FAILURE);
		else
			throw new DistributedException(ex, ExceptionType.ROLLBACK_SUCCESS);
	}

	@Override
	protected <T> void retry(ForkTask<T> task, ForkTaskInfo info, Distributed distributed) throws DistributedException {
		if (TaskStatus.ROLLBACK_FAILURE.name().equals(info.getTaskStatus())) {
			try {
				task.getBusiness().handle();
			} catch (Exception e) {

			}
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
