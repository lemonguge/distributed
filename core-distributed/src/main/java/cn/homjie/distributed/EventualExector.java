package cn.homjie.distributed;

import org.apache.commons.lang3.exception.ExceptionUtils;

import cn.homjie.distributed.api.ForkTaskInfo;
import cn.homjie.distributed.api.TaskResult;
import cn.homjie.distributed.api.exception.DistributedException;

public class EventualExector extends AbstractExector {

	@Override
	protected <T> void first(ForkTask<T> task, ForkTaskInfo info, Distributed distributed) throws DistributedException {
		// 只有状态不为空才有结果
		if (info.getTaskStatus() != null)
			return;
		exec(task, info);
	}

	@Override
	protected <T> void retry(ForkTask<T> task, ForkTaskInfo info, Distributed distributed) throws DistributedException {
		String taskStatus = info.getTaskStatus();
		if (!TaskStatus.SUCCESS.name().equals(taskStatus)) {
			exec(task, info);
		}
	}

	private <T> void exec(ForkTask<T> task, ForkTaskInfo info) {
		try {
			T result = task.getBusiness().handle();
			info.setResult(TaskResult.ok(result));
			info.setTaskStatus(TaskStatus.SUCCESS.name());

			sendOk(info, result);
		} catch (DistributedException e) {
			info.setResult(TaskResult.ex(e.getCause()));
			info.setTaskStatus(TaskStatus.EVENTUAL_IGNORE.name());

			sendEx(info, e);
		} catch (Exception e) {
			info.setResult(TaskResult.ex(e));
			info.setTaskStatus(TaskStatus.EVENTUAL_EXCEPTION.name());
			info.setStackTrace(ExceptionUtils.getStackTrace(e));

			sendEx(info, e);
		}
	}

}
