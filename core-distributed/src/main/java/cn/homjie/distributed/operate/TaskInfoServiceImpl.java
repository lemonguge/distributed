package cn.homjie.distributed.operate;

import java.util.List;

import cn.homjie.distributed.domain.TaskInfoEntity;
import cn.homjie.distributed.mybatis.TaskInfoEntityMapper;

public class TaskInfoServiceImpl implements TaskInfoService {

	private TaskInfoEntityMapper taskInfoDao;
	
	public void setTaskInfoDao(TaskInfoEntityMapper taskInfoDao) {
		this.taskInfoDao = taskInfoDao;
	}

	@Override
	public List<TaskInfoEntity> select(TaskInfoEntity condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskInfoEntity unique(TaskInfoEntity condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(TaskInfoEntity object) {
		taskInfoDao.insert(object);
	}

	@Override
	public void update(TaskInfoEntity object) {
		// TODO Auto-generated method stub

	}

}
