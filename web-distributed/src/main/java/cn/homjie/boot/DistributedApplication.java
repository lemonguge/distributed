package cn.homjie.boot;

import java.io.IOException;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import cn.homjie.distributed.configure.DistributedConfiguration;

@SpringBootApplication
@Import(DistributedConfiguration.class)
@MapperScan("cn.homjie.boot.mapper")
public class DistributedApplication {

	@Bean(destroyMethod = "shutdown")
	RedissonClient redisson() throws IOException {
		Config config = new Config();
		config.useSingleServer().setAddress("172.30.248.105:6479").setPassword("22pBD7.dubbo").setDatabase(5);
		return Redisson.create(config);
	}

	public static void main(String[] args) {
		SpringApplication.run(DistributedApplication.class, args);
	}
}
