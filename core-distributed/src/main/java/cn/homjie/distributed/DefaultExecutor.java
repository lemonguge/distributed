package cn.homjie.distributed;

import cn.homjie.distributed.api.ForkTaskInfo;
import cn.homjie.distributed.api.TaskResult;

public class DefaultExecutor<T> implements TransactionExecutor<T> {

	@Override
	public void submit(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception {
		T result = task.getBusiness().handle();
		info.setResult(new TaskResult<T>(result));
	}

}
