package cn.homjie.distributed.mybatis;

import cn.homjie.distributed.domain.DescriptionEntity;

public interface DescriptionEntityMapper {
	int deleteByPrimaryKey(String id);

	int insert(DescriptionEntity record);

	int insertSelective(DescriptionEntity record);

	DescriptionEntity selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(DescriptionEntity record);

	int updateByPrimaryKey(DescriptionEntity record);
}