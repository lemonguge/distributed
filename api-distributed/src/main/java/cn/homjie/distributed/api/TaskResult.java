package cn.homjie.distributed.api;

import static cn.homjie.distributed.api.exception.ExceptionType.EVENTUAL_EXCEPTION;

import java.io.Serializable;

import cn.homjie.distributed.api.exception.DistributedException;

public class TaskResult<T> implements Serializable {

	private static final long serialVersionUID = -3144849695440150966L;

	private T result;

	private DistributedException ex;

	private boolean ok;

	public TaskResult(T result) {
		this.result = result;
		this.ok = true;
	}

	public TaskResult(Throwable ex) {
		this.ex = new DistributedException(ex, EVENTUAL_EXCEPTION);
		this.ok = false;
	}

	public boolean isOk() {
		return ok;
	}

	public T get() {
		if (ok)
			return result;
		else
			throw ex;
	}

}
