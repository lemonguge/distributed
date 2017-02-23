package cn.homjie.boot.mapper;

import java.util.List;

import cn.homjie.boot.domain.City;

public interface CityMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(City record);

	int insertSelective(City record);

	City selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(City record);

	int updateByPrimaryKey(City record);
	
	List<City> queryAll(City city);
}