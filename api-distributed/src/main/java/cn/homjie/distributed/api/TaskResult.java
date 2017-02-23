package cn.homjie.distributed.api;

import java.io.Serializable;

import cn.homjie.distributed.api.exception.DistributedException;

public class TaskResult<T> implements Serializable {

	private static final long serialVersionUID = -3144849695440150966L;

	private T result;

	private Throwable ex;

	private boolean success;

	public TaskResult(T result) {
		this.result = result;
		this.success = true;
	}

	public TaskResult(Throwable ex) {
		this.ex = ex;
		this.success = false;
	}

	public boolean isOk() {
		return success;
	}

	public T get() {
		if (success)
			return result;
		else
			throw new DistributedException(ex);
	}

}
