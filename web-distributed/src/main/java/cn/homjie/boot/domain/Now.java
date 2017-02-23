package cn.homjie.boot.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Now implements Serializable {

	private static final long serialVersionUID = -5319022508168510364L;

	// 自定义JSON解析
	@JsonFormat(pattern = "yyyy/MM/dd")
	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Now [date=" + date + "]";
	}
}