package cn.homjie.distributed;

public class DefaultExecutor<T> implements TransactionExecutor<T> {

	@Override
	public T submit(ForkTask<T> task, Distributed distributed) throws Exception {
		return task.getBusiness().handle();
	}

}
