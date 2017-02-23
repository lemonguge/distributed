package cn.homjie.distributed;

public abstract class AbstractExector<T> implements TransactionExecutor<T> {

	@Override
	public T submit(ForkTask<T> task, Distributed distributed) throws Exception {
		if (distributed.isFirstTime())
			return first(task, distributed);
		else
			return retry(task, distributed);
	}

	protected abstract T first(ForkTask<T> task, Distributed distributed) throws Exception;

	protected abstract T retry(ForkTask<T> task, Distributed distributed) throws Exception;

}
