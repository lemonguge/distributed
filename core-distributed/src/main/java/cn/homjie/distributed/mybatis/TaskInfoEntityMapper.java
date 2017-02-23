package cn.homjie.distributed.mybatis;

import cn.homjie.distributed.domain.TaskInfoEntity;

public interface TaskInfoEntityMapper {
	int deleteByPrimaryKey(String id);

	int insert(TaskInfoEntity record);

	int insertSelective(TaskInfoEntity record);

	TaskInfoEntity selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(TaskInfoEntity record);

	int updateByPrimaryKey(TaskInfoEntity record);

}