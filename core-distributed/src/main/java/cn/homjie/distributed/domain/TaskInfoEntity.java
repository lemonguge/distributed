package cn.homjie.distributed.domain;

import java.io.Serializable;

public class TaskInfoEntity implements Serializable {

	private static final long serialVersionUID = -6978625695732715519L;

	private String id;
	private String descriptionId;

	// Object 序列化
	private String result;
	// Throwable 序列化
	private String ex;
	private Boolean success;

	private String taskStatus;
	private String stackTrace;

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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getEx() {
		return ex;
	}

	public void setEx(String ex) {
		this.ex = ex;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
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
