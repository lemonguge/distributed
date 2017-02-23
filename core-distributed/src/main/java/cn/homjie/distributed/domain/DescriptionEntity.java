package cn.homjie.distributed.domain;

import java.io.Serializable;

public class DescriptionEntity implements Serializable {

	private static final long serialVersionUID = 6832571090291572690L;

	// 事务处理
	private String transactionName;

	// 当前主键
	private String id;
	// 父级主键
	private String pid;

	// 执行次数
	private int times = 0;

	public String getTransactionName() {
		return transactionName;
	}

	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

}
