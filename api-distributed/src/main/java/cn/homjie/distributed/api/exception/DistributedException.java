package cn.homjie.distributed.api.exception;

import static cn.homjie.distributed.api.exception.ExceptionType.DEFAULT_EXCEPTION;
import static cn.homjie.distributed.api.exception.ExceptionType.EVENTUAL_EXCEPTION;
import static cn.homjie.distributed.api.exception.ExceptionType.ROLLBACK_FAILURE;
import static cn.homjie.distributed.api.exception.ExceptionType.ROLLBACK_SUCCESS;

import cn.homjie.distributed.api.exception.ExceptionType;

public class DistributedException extends RuntimeException {

	private static final long serialVersionUID = 3026319152089291579L;

	private ExceptionType type;

	public DistributedException(Throwable cause, ExceptionType type) {
		super(cause);
		this.type = type;
	}

	public boolean isDefaultException() {
		return DEFAULT_EXCEPTION == type;
	}

	public boolean isEventualException() {
		return EVENTUAL_EXCEPTION == type;
	}

	public boolean isRollbackSuccess() {
		return ROLLBACK_SUCCESS == type;
	}

	public boolean isRollbackFailure() {
		return ROLLBACK_FAILURE == type;
	}

}
