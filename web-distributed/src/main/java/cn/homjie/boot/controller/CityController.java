package cn.homjie.boot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.homjie.boot.domain.City;
import cn.homjie.boot.service.CityService;

@RestController
@RequestMapping("city")
public class CityController {
	
	@Autowired
	private CityService cityService;
	
	@RequestMapping(value = "/{id}",method = RequestMethod.GET)
	public City query(@PathVariable int id){
		return cityService.query(id);
	}
	
	@RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
	public void delete(@PathVariable int id){
		cityService.delete(id);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public List<City> queryAll(@ModelAttribute City city){
		return cityService.queryAll(city);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void save(@RequestBody City city){
		cityService.save(city);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody City city){
		cityService.update(city);
	}

}
