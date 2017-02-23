package cn.homjie.boot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.homjie.boot.domain.City;
import cn.homjie.boot.mapper.CityMapper;
import cn.homjie.boot.service.CityService;
import cn.homjie.distributed.Description;
import cn.homjie.distributed.Distributed;
import cn.homjie.distributed.TaskResult;

@Service
public class CityServiceImpl implements CityService {
	
	@Autowired
	private CityMapper cityDao;

	@Override
	public City query(int id) {
		return cityDao.selectByPrimaryKey(id);
	}

	@Override
	public void save(City city) {
		cityDao.insertSelective(city);
	}

	@Override
	public void update(City city) {
		cityDao.updateByPrimaryKeySelective(city);
	}

	@Override
	public void delete(int id) {
		cityDao.deleteByPrimaryKey(id);
	}

	@Override
	public List<City> queryAll(City city) {
		return cityDao.queryAll(city);
	}

	@Override
	public TaskResult<City> query(int id, Description description) throws Exception {
		Distributed distributed = new Distributed(description);
		return distributed.submit(() -> query(id));
	}

}
