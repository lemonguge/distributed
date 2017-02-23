package cn.homjie.distributed;

import com.google.gson.Gson;

import cn.homjie.distributed.api.ForkTaskInfo;
import cn.homjie.distributed.domain.TaskInfoEntity;
import cn.homjie.distributed.rabbit.RabbitSender;
import cn.homjie.distributed.spring.SpringHolder;

public abstract class AbstractExector<T> implements TransactionExecutor<T> {

	protected static Gson gson = new Gson();

	@Override
	public void submit(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception {
		if (distributed.isFirstTime())
			first(task, info, distributed);
		else
			retry(task, info, distributed);
	}

	protected abstract void first(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception;

	protected abstract void retry(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception;

	protected void sendEx(ForkTaskInfo<T> info, Throwable e) {
		RabbitSender<TaskInfoEntity> taskInfoSender = SpringHolder.getBean("taskInfoSender");
		TaskInfoEntity entity = new TaskInfoEntity();
		entity.setId(info.getId());
		entity.setDescriptionId(info.getDescriptionId());
		entity.setEx(gson.toJson(e));
		entity.setSuccess(false);
		entity.setTaskStatus(info.getTaskStatus());
		entity.setStackTrace(info.getStackTrace());
		try {
			taskInfoSender.send(entity);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	protected void sendOk(ForkTaskInfo<T> info, T t) {
		RabbitSender<TaskInfoEntity> taskInfoSender = SpringHolder.getBean("taskInfoSender");
		TaskInfoEntity entity = new TaskInfoEntity();
		entity.setId(info.getId());
		entity.setDescriptionId(info.getDescriptionId());
		entity.setResult(gson.toJson(t));
		entity.setSuccess(true);
		entity.setTaskStatus(info.getTaskStatus());
		entity.setStackTrace(info.getStackTrace());
		try {
			taskInfoSender.send(entity);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	protected void sendOk(ForkTaskInfo<T> info) {
		RabbitSender<TaskInfoEntity> taskInfoSender = SpringHolder.getBean("taskInfoSender");
		TaskInfoEntity entity = new TaskInfoEntity();
		entity.setId(info.getId());
		entity.setDescriptionId(info.getDescriptionId());
		entity.setSuccess(true);
		entity.setTaskStatus(info.getTaskStatus());
		entity.setStackTrace(info.getStackTrace());
		try {
			taskInfoSender.send(entity);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

}
