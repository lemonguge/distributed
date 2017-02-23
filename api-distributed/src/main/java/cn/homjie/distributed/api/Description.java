package cn.homjie.distributed.api;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * @Class Description
 * @Description 服务信息
 * @Author JieHong
 * @Date 2017年1月11日 下午4:05:10
 */
public class Description implements Serializable {

	private static final long serialVersionUID = -3941038773069693258L;

	// 事务处理
	private Transaction transaction;

	// 当前主键
	private String id;
	// 父级主键
	private String pid;

	// 执行次数
	private int times = 0;

	// 所有任务信息
	private List<ForkTaskInfo<?>> infos = Lists.newArrayList();
	// 所有子服务信息
	private List<Description> children = Lists.newArrayList();

	public Description(Transaction transaction) {
		this.transaction = transaction;
	}

	public void incTimes() {
		times++;
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

	public List<ForkTaskInfo<?>> getInfos() {
		return infos;
	}

	public void setInfos(List<ForkTaskInfo<?>> infos) {
		this.infos = infos;
	}

	public List<Description> getChildren() {
		return children;
	}

	public void setChildren(List<Description> children) {
		this.children = children;
	}

	public Transaction getTransaction() {
		return transaction;
	}

//	private void send() {
//		RabbitSender<DescriptionEntity> descriptionSender = SpringHolder.getBean("descriptionSender");
//		DescriptionEntity entity = new DescriptionEntity();
//		entity.setId(id);
//		entity.setPid(pid);
//		entity.setTransactionName(transaction.name());
//		entity.setTimes(times);
//		try {
//			descriptionSender.send(entity);
//		} catch (Exception exp) {
//			exp.printStackTrace();
//		}
//	}


}
