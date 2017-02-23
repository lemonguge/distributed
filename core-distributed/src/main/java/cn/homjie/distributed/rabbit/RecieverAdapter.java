package cn.homjie.distributed.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

public abstract class RecieverAdapter<T> implements MessageListener {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	private MessageConverter messageConverter = new SimpleMessageConverter();

	@Override
	@SuppressWarnings("unchecked")
	public final void onMessage(Message message) {
		Object object = null;
		try{
			object = messageConverter.fromMessage(message);
		}catch (Exception e) {
			log.error("转换消息失败",e);
			return;
		}
		try{
			recieve((T) object);
		}catch (Exception e) {
			log.error("处理消息失败",e);
			return;
		}
	}
	
	public abstract void recieve(T entity);

}
