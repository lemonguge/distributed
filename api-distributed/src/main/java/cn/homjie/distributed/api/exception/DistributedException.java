package cn.homjie.distributed.api.exception;

public class DistributedException extends RuntimeException {

	private static final long serialVersionUID = 3026319152089291579L;

	public DistributedException(Throwable cause) {
		super(cause);
	}

}
