package cn.homjie.distributed;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.collect.Lists;

import cn.homjie.distributed.api.ForkTaskInfo;
import cn.homjie.distributed.api.TaskResult;
import cn.homjie.distributed.api.exception.DistributedException;

public class EventualExector<T> extends AbstractExector<T> {

	// EVENTUAL_FAILURE 进行重试分析后会将 SUCCESS 修改
	private static final List<String> RETRY_STATUS = Lists.newArrayList(TaskStatus.EVENTUAL_FAILURE.name(), TaskStatus.EVENTUAL_EXCEPTION.name(),
			TaskStatus.EVENTUAL_IGNORE.name());

	private Observer observer;

	public EventualExector(Observer observer) {
		this.observer = observer;
	}

	@Override
	protected void first(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception {
		// 只有状态不为空才有结果
		if (info.getTaskStatus() != null)
			return;
		exec(task, info);
	}

	@Override
	protected void retry(ForkTask<T> task, ForkTaskInfo<T> info, Distributed distributed) throws Exception {
		String taskStatus = info.getTaskStatus();
		if (RETRY_STATUS.contains(taskStatus)) {
			exec(task, info);
		}
	}

	private void exec(ForkTask<T> task, ForkTaskInfo<T> info) {
		try {
			T result = task.getBusiness().handle();
			info.setResult(new TaskResult<T>(result));
			info.setTaskStatus(TaskStatus.SUCCESS.name());

			sendOk(info, result);
		} catch (DistributedException e) {
			info.setResult(new TaskResult<T>(e.getCause()));
			info.setTaskStatus(TaskStatus.EVENTUAL_IGNORE.name());

			sendEx(info, e);
		} catch (Exception e) {
			info.setResult(new TaskResult<T>(e));
			info.setTaskStatus(TaskStatus.EVENTUAL_EXCEPTION.name());
			info.setStackTrace(ExceptionUtils.getStackTrace(e));

			sendEx(info, e);
		}
	}

}
