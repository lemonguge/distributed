package cn.homjie.distributed;

public class ForkTask<T> {

	private String taskName;
	private Executable<T> business;
	private NulExecutable rollback;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Executable<T> getBusiness() {
		return business;
	}

	public void setBusiness(Executable<T> business) {
		this.business = business;
	}

	public NulExecutable getRollback() {
		return rollback;
	}

	public void setRollback(NulExecutable rollback) {
		this.rollback = rollback;
	}

}
