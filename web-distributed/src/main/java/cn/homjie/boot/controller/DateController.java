package cn.homjie.boot.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("date")
public class DateController {

	@RequestMapping(value = "/{time}", method = RequestMethod.GET)
	public void time(@PathVariable Date time) { // Formatter
		System.out.println(time);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Date date() { // 全局JSON
		return new Date();
	}

}
