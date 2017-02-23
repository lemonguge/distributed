package cn.homjie.distributed;

import cn.homjie.distributed.api.Transaction;

public class TransactionFactory {

	public static <R> TransactionExecutor<R> create(Transaction transaction, Observer observer) {
		TransactionExecutor<R> executor = null;
		switch (transaction) {
		case ROLLBACK:
			executor = new RollbackExector<R>();
			break;
		case EVENTUAL:
			executor = new EventualExector<R>(observer);
			break;
		default:
			executor = new DefaultExecutor<R>();
			break;
		}
		return executor;
	}

}
