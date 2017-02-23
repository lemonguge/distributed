package cn.homjie.distributed;

import static cn.homjie.distributed.Observer.ONCE;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import cn.homjie.distributed.api.Description;
import cn.homjie.distributed.api.ForkTaskInfo;
import cn.homjie.distributed.api.TaskResult;
import cn.homjie.distributed.api.Transaction;
import cn.homjie.distributed.utils.IdGen;

public class Distributed {

	private Description description;
	// 是否首次执行
	private boolean firstTime = false;
	// 已执行的和当前任务
	private List<ForkTask<?>> tasks = Lists.newArrayList();

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
	}

	public void execute(NulExecutable business) throws Exception {
		execute(business, null, ONCE);
	}

	public void execute(NulExecutable business, String taskName) throws Exception {
		execute(business, taskName, ONCE);
	}

	public void execute(NulExecutable business, String taskName, Observer observer) throws Exception {
		submit((Executable<Void>) business, taskName, observer);
	}

	public <T> TaskResult<T> submit(Executable<T> business) throws Exception {
		return submit(business, null, ONCE);
	}

	public <T> TaskResult<T> submit(Executable<T> business, String taskName) throws Exception {
		return submit(business, taskName, ONCE);
	}

	public <T> TaskResult<T> submit(Executable<T> business, String taskName, Observer observer) throws Exception {
		if (business == null)
			throw new NullPointerException("任务为空");
		if (observer == null)
			throw new NullPointerException("观察为空");
		ForkTask<T> task = new ForkTask<T>();
		ForkTaskInfo<T> info = info();

		tasks.add(task);

		if (StringUtils.isBlank(taskName))
			taskName = "任务" + tasks.size();
		task.setBusiness(business);

		Transaction transaction = description.getTransaction();
		TransactionExecutor<T> executor = TransactionFactory.create(transaction, observer);
		executor.submit(task, info, this);

		return info.getResult();
	}

	@SuppressWarnings("unchecked")
	private <T> ForkTaskInfo<T> info() {
		if (firstTime) {
			ForkTaskInfo<T> info = addInfo();
			return info;
		} else {
			List<ForkTaskInfo<?>> infos = description.getInfos();
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
		info.setDescriptionId(description.getId());
		description.getInfos().add(info);
		return info;
	}

	public void rollback(NulExecutable rollback) throws Exception {
		// 回滚设置
		tasks.get(tasks.size() - 1).setRollback(rollback);
	}

	public Description description() {
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
