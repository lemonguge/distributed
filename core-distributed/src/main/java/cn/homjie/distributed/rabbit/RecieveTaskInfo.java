package cn.homjie.distributed.rabbit;

import cn.homjie.distributed.domain.TaskInfoEntity;
import cn.homjie.distributed.operate.TaskInfoService;

public class RecieveTaskInfo extends RecieverAdapter<TaskInfoEntity>{
	
	private TaskInfoService taskInfoService;
	
	public void setTaskInfoService(TaskInfoService taskInfoService) {
		this.taskInfoService = taskInfoService;
	}

	@Override
	public void recieve(TaskInfoEntity entity) {
		taskInfoService.insert(entity);
	}

}
