package cn.homjie.distributed;

import cn.homjie.distributed.api.exception.ExceptionType;

import cn.homjie.distributed.api.ForkTaskInfo;
import cn.homjie.distributed.api.TaskResult;
import cn.homjie.distributed.api.exception.DistributedException;

public class DefaultExecutor implements TransactionExecutor {

	@Override
	public <T> void submit(ForkTask<T> task, ForkTaskInfo info, Distributed distributed) throws DistributedException {
		try {
			T result = task.getBusiness().handle();
			info.setResult(TaskResult.ok(result));
		} catch (DistributedException e) {
			throw e;
		} catch (Exception e) {
			// 原始异常
			throw new DistributedException(e, ExceptionType.DEFAULT_EXCEPTION);
		}
	}

}
