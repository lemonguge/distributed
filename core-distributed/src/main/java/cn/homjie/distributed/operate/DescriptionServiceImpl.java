package cn.homjie.distributed.operate;

import java.util.List;

import cn.homjie.distributed.domain.DescriptionEntity;
import cn.homjie.distributed.mybatis.DescriptionEntityMapper;

public class DescriptionServiceImpl implements DescriptionService {

	private DescriptionEntityMapper descriptionDao;

	public void setDescriptionDao(DescriptionEntityMapper descriptionDao) {
		this.descriptionDao = descriptionDao;
	}

	@Override
	public List<DescriptionEntity> select(DescriptionEntity condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DescriptionEntity unique(DescriptionEntity condition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(DescriptionEntity object) {
		if (object.getTimes() == 1)
			descriptionDao.insertSelective(object);
		else
			descriptionDao.updateByPrimaryKeySelective(object);
	}

	@Override
	public void update(DescriptionEntity object) {
		// TODO Auto-generated method stub

	}

}
