package cn.homjie.boot.service;

import java.util.List;

import cn.homjie.boot.domain.City;
import cn.homjie.distributed.Description;
import cn.homjie.distributed.TaskResult;

public interface CityService {

	City query(int id);

	void save(City city);

	void update(City city);

	void delete(int id);

	List<City> queryAll(City city);

	TaskResult<City> query(int id, Description description) throws Exception;

}
