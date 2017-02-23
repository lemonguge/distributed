package cn.homjie.distributed;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import cn.homjie.distributed.domain.DescriptionEntity;
import cn.homjie.distributed.rabbit.RabbitSender;
import cn.homjie.distributed.spring.SpringHolder;

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

	// 当前任务信息
	private int pointInfos = 0;
	// 所有任务信息
	private List<ForkTaskInfo<?>> infos = Lists.newArrayList();

	// 当前子服务信息位置
	private int pointChildren = 0;
	// 所有子服务信息
	private List<Description> children = Lists.newArrayList();

	public Description(Transaction transaction) {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
		this.transaction = transaction;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	@SuppressWarnings("unchecked")
	<T> ForkTaskInfo<T> info() {
		if (firstTime()) {
			ForkTaskInfo<T> info = addInfo();
			return info;
		} else {
			int size = infos.size();
			int point = pointInfos;

			pointInfos++;

			if (point < size)
				return (ForkTaskInfo<T>) infos.get(point);
			return addInfo();
		}
	}

	private <T> ForkTaskInfo<T> addInfo() {
		ForkTaskInfo<T> info = new ForkTaskInfo<T>();
		info.setDescriptionId(id);
		infos.add(info);
		return info;
	}

	public Description child() {
		if (firstTime()) {
			Description child = addDesc();
			return child;
		} else {
			int size = children.size();
			int point = pointChildren;

			pointChildren++;

			if (point < size)
				return children.get(point);
			return addDesc();
		}
	}

	private Description addDesc() {
		Description child = new Description(transaction);
		child.pid = id;
		children.add(child);
		return child;
	}

	void incTimesAndSend() {
		// 初始化索引
		pointInfos = 0;
		pointChildren = 0;
		times++;

		send();
	}

	private void send() {
		RabbitSender<DescriptionEntity> descriptionSender = SpringHolder.getBean("descriptionSender");
		DescriptionEntity entity = new DescriptionEntity();
		entity.setId(id);
		entity.setPid(pid);
		entity.setTransactionName(transaction.name());
		entity.setTimes(times);
		try {
			descriptionSender.send(entity);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	boolean firstTime() {
		// 会先调用incTimes()
		return times == 1;
	}

	public int getTimes() {
		return times;
	}

	public String getId() {
		return id;
	}

	public String getPid() {
		return pid;
	}

}
