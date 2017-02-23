package cn.homjie.distributed;

public enum TaskStatus {

	SUCCESS("执行成功"),

	EXCEPTION("执行异常"),

	FAILURE("执行失败"),

	ROLLBACK_SUCCESS("执行成功，之后出现异常，调用回滚成功"),

	ROLLBACK_FAILURE("执行成功，之后出现异常，调用回滚失败"),

	ROLLBACK_NOTFIND("执行成功，之后出现异常，无需进行回滚"),

	ROLLBACK_EXCEPTION("执行成功，之后出现异常，调用回滚异常"),

	EVENTUAL_FAILURE("执行失败，即将进行消息重试补偿"),

	EVENTUAL_EXCEPTION("执行异常，即将进行消息重试补偿"),

	EVENTUAL_IGNORE("执行忽略，即将进行消息重试补偿");

	private String log;

	private TaskStatus(String log) {
		this.log = log;
	}

	public String log() {
		return log;
	}
}
