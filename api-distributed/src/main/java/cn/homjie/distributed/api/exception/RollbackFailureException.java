package cn.homjie.distributed.api.exception;

public class RollbackFailureException extends RuntimeException {

	private static final long serialVersionUID = -2734664063899165755L;

	public RollbackFailureException(Throwable cause) {
		super(cause);
	}

}
