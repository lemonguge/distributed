package cn.homjie.distributed;

import cn.homjie.distributed.api.Transaction;

public class TransactionFactory {

	public static TransactionExecutor create(Transaction transaction) {
		TransactionExecutor executor = null;
		switch (transaction) {
		case ROLLBACK:
			executor = new RollbackExector();
			break;
		case EVENTUAL:
			executor = new EventualExector();
			break;
		default:
			executor = new DefaultExecutor();
			break;
		}
		return executor;
	}

}
