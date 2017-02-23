package cn.homjie.distributed.exception;

public class RollbackSuccessException extends RuntimeException {

	private static final long serialVersionUID = -117644378397164944L;

	public RollbackSuccessException(Throwable cause) {
		super(cause);
	}

}
