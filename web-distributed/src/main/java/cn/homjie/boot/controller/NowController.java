package cn.homjie.boot.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.homjie.boot.domain.Now;

@RestController
@RequestMapping("now")
public class NowController {

	@RequestMapping(method = RequestMethod.POST)
	public void time(@RequestBody Now now) { // 自定义JSON
		System.out.println(now);
	}

	@RequestMapping(method = RequestMethod.GET)
	public Now date() { // 自定义JSON
		Now now = new Now();
		now.setDate(new Date());
		return now;
	}

}
