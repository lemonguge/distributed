package cn.homjie.distributed;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import cn.homjie.distributed.api.Description;
import cn.homjie.distributed.api.ForkTaskInfo;
import cn.homjie.distributed.api.TaskResult;
import cn.homjie.distributed.api.Transaction;
import cn.homjie.distributed.api.exception.DistributedException;
import cn.homjie.distributed.utils.IdGen;

public class Distributed {

	private Description description;
	// 是否首次执行
	private boolean firstTime = false;
	// 已执行的和当前任务
	private List<ForkTask<?>> tasks = Lists.newArrayList();

	// 事务执行器
	private TransactionExecutor executor;

	// 当前任务信息
	private int pointInfos = 0;
	// 当前子服务信息位置
	private int pointChildren = 0;

	public Distributed(Description description) {
		this.description = description;
		analyse();
	}

	private void analyse() {
		// 是否为首次运行
		firstTime = description.getTimes() == 0;
		// 执行次数 + 1
		description.incTimes();
		if (firstTime)
			description.setId(IdGen.uuid());

		// 获得事务执行器
		Transaction transaction = description.getTransaction();
		executor = TransactionFactory.create(transaction);
	}

	public void execute(NulExecutable business) throws DistributedException {
		execute(business, null);
	}

	public void execute(NulExecutable business, String taskName) throws DistributedException {
		submit((Executable<Void>) business, taskName);
	}

	public <T> TaskResult submit(Executable<T> business) throws DistributedException {
		return submit(business, null);
	}

	public <T> TaskResult submit(Executable<T> business, String taskName) throws DistributedException {
		if (business == null)
			throw new NullPointerException("任务为空");
		ForkTask<T> task = new ForkTask<T>();
		ForkTaskInfo info = info();

		tasks.add(task);

		if (StringUtils.isBlank(taskName))
			taskName = "任务" + tasks.size();
		task.setBusiness(business);

		executor.submit(task, info, this);

		return info.getResult();
	}

	private ForkTaskInfo info() {
		if (firstTime) {
			ForkTaskInfo info = addInfo();
			return info;
		} else {
			List<ForkTaskInfo> infos = description.getInfos();
			int size = infos.size();
			int point = pointInfos;

			pointInfos++;

			if (point < size)
				return infos.get(point);
			return addInfo();
		}
	}

	private ForkTaskInfo addInfo() {
		ForkTaskInfo info = new ForkTaskInfo();
		info.setDescriptionId(description.getId());
		description.getInfos().add(info);
		return info;
	}

	public void rollback(NulExecutable rollback) {
		// 回滚设置
		tasks.get(tasks.size() - 1).setRollback(rollback);
	}

	public Description childInvoke() {
		if (firstTime) {
			Description child = addDesc();
			return child;
		} else {
			List<Description> children = description.getChildren();
			int size = children.size();
			int point = pointChildren;

			pointChildren++;

			if (point < size)
				return children.get(point);
			return addDesc();
		}
	}

	private Description addDesc() {
		Description child = new Description(description.getTransaction());
		child.setPid(description.getId());
		description.getChildren().add(child);
		return child;
	}

	Description getDescription() {
		return description;
	}

	List<ForkTask<?>> getTasks() {
		return tasks;
	}

	boolean isFirstTime() {
		return firstTime;
	}

}
