package cn.homjie.distributed;

import cn.homjie.distributed.api.Transaction;

public class TransactionFactory {

	public static <R> TransactionExecutor<R> create(Transaction transaction) {
		TransactionExecutor<R> executor = null;
		switch (transaction) {
		case ROLLBACK:
			executor = new RollbackExector<R>();
			break;
		case EVENTUAL:
			executor = new EventualExector<R>();
			break;
		default:
			executor = new DefaultExecutor<R>();
			break;
		}
		return executor;
	}

}
