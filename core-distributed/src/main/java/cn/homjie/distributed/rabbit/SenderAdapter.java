package cn.homjie.distributed.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;

public abstract class SenderAdapter<T> implements RabbitSender<T> {

	private Logger log = LoggerFactory.getLogger(getClass());

	protected RabbitTemplate rabbitTemplate;

	public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Override
	public void send(T object) {
		rabbitTemplate.setConfirmCallback(new ConfirmCallback() {

			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if (!ack) {
					log.error("消息发生失败[{}]", cause);
					convertAndSend(object);
				} else {
					log.info("消息发送成功");
				}
			}
		});
		convertAndSend(object);
	}

	private void convertAndSend(T object) {
		try {
			rabbitTemplate.convertAndSend(object);
		} catch (Exception e) {
			log.error("消息发送异常", e);
		}
	}

}
