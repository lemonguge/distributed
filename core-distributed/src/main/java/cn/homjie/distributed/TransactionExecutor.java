package cn.homjie.distributed;

public interface TransactionExecutor<T> {

	public T submit(ForkTask<T> task, Distributed distributed) throws Exception;

}
