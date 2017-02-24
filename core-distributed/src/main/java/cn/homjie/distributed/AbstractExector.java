package cn.homjie.distributed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import cn.homjie.distributed.api.ForkTaskInfo;
import cn.homjie.distributed.api.exception.DistributedException;
import cn.homjie.distributed.domain.TaskInfoEntity;
import cn.homjie.distributed.rabbit.RabbitSender;
import cn.homjie.distributed.spring.SpringHolder;

public abstract class AbstractExector implements TransactionExecutor {

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected static Gson gson = new Gson();

	@Override
	public <T> void submit(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws DistributedException {
		if (distributed.isFirstTime())
			first(task, info, distributed);
		else
			retry(task, info, distributed);
	}

	protected abstract <T> void first(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws DistributedException;

	protected abstract <T> void retry(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws DistributedException;

	protected void sendEx(ForkTaskInfo<?> info, Throwable e) {
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

	protected void sendOk(ForkTaskInfo<?> info, Object result) {
		RabbitSender<TaskInfoEntity> taskInfoSender = SpringHolder.getBean("taskInfoSender");
		TaskInfoEntity entity = new TaskInfoEntity();
		entity.setId(info.getId());
		entity.setDescriptionId(info.getDescriptionId());
		entity.setResult(gson.toJson(result));
		entity.setSuccess(true);
		entity.setTaskStatus(info.getTaskStatus());
		entity.setStackTrace(info.getStackTrace());
		try {
			taskInfoSender.send(entity);
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	protected void sendOk(ForkTaskInfo<?> info) {
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
