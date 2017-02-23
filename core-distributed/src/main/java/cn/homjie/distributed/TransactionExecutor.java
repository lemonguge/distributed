package cn.homjie.distributed;

import cn.homjie.distributed.api.ForkTaskInfo;

public interface TransactionExecutor<T> {

	public void submit(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception;

}
