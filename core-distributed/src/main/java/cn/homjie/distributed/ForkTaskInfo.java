package cn.homjie.distributed;

import java.io.Serializable;
import java.util.UUID;

public class ForkTaskInfo<T> implements Serializable {

	private static final long serialVersionUID = -5228081734361376651L;

	private String id;
	private String descriptionId;

	private TaskResult<T> result;

	private String taskStatus;
	private String stackTrace;

	public ForkTaskInfo() {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescriptionId() {
		return descriptionId;
	}

	public void setDescriptionId(String descriptionId) {
		this.descriptionId = descriptionId;
	}

	public TaskResult<T> getResult() {
		return result;
	}

	public void setResult(TaskResult<T> result) {
		this.result = result;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

}
