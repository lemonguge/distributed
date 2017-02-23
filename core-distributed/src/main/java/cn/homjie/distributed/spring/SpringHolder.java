package cn.homjie.distributed.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Class SpringDistributedHolder
 * @Description 获取Spring容器工具类
 * @Author JieHong
 * @Date 2017年2月20日 下午12:47:31
 */
public class SpringHolder implements ApplicationContextAware {
	private static ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (ctx == null) {
			ctx = applicationContext;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		if (ctx != null) {
			return (T) ctx.getBean(beanName);
		}
		return null;
	}

	public static <T> T getBean(Class<T> requiredType) {
		if (ctx != null) {
			return ctx.getBean(requiredType);
		}

		return null;
	}

}
