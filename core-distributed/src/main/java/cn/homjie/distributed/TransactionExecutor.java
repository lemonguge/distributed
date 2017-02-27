package cn.homjie.distributed;

import cn.homjie.distributed.api.ForkTaskInfo;
import cn.homjie.distributed.api.exception.DistributedException;

public interface TransactionExecutor {

	public <T> void submit(ForkTask<T> task, ForkTaskInfo info, Distributed distributed) throws DistributedException;

}
