package cn.homjie.boot.configure;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addFormatters(FormatterRegistry registry) {
		// Add formatters and/or converters

		registry.addFormatter(DateFormatter.instance());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 全局JSON解析
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
	}

}
