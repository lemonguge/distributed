package cn.homjie.distributed.api;

import static cn.homjie.distributed.api.exception.ExceptionType.EVENTUAL_EXCEPTION;

import java.io.Serializable;

import cn.homjie.distributed.api.exception.DistributedException;

public class TaskResult implements Serializable {

	private static final long serialVersionUID = -3144849695440150966L;

	private Object result;

	private DistributedException ex;

	private boolean ok;

	private TaskResult() {
	}

	public static <T> TaskResult ok(T result) {
		TaskResult tr = new TaskResult();
		tr.result = result;
		tr.ok = true;
		return tr;
	}

	public static TaskResult ex(Throwable ex) {
		TaskResult tr = new TaskResult();
		tr.ex = new DistributedException(ex, EVENTUAL_EXCEPTION);
		tr.ok = false;
		return tr;
	}

	public boolean isOk() {
		return ok;
	}

	@SuppressWarnings("unchecked")
	public <T> T get() {
		if (ok)
			return (T) result;
		else
			throw ex;
	}

}
