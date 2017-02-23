package cn.homjie.distributed.configure;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.homjie.distributed.domain.DescriptionEntity;
import cn.homjie.distributed.domain.TaskInfoEntity;
import cn.homjie.distributed.mybatis.DescriptionEntityMapper;
import cn.homjie.distributed.mybatis.TaskInfoEntityMapper;
import cn.homjie.distributed.operate.DescriptionService;
import cn.homjie.distributed.operate.DescriptionServiceImpl;
import cn.homjie.distributed.operate.TaskInfoService;
import cn.homjie.distributed.operate.TaskInfoServiceImpl;
import cn.homjie.distributed.rabbit.RabbitSender;
import cn.homjie.distributed.rabbit.RecieveDescription;
import cn.homjie.distributed.rabbit.RecieveTaskInfo;
import cn.homjie.distributed.rabbit.SenderAdapter;
import cn.homjie.distributed.spring.SpringHolder;

@Configuration
@MapperScan("cn.homjie.distributed.mybatis")
public class DistributedConfiguration {

	private static final String EXCHANGE = "DistributedExchange";

	private static final String QUEUE_DESCRIPTION = "distributed.queue.description";
	private static final String ROUTE_DESCRIPTION = "distributed.binding.description";

	private static final String QUEUE_TASK_INFO = "distributed.queue.taskInfo";
	private static final String ROUTE_TASK_INFO = "distributed.binding.taskInfo";

	@Bean
	SpringHolder springHolder() {
		return new SpringHolder();
	}

	public ConnectionFactory connectionFactory() {
		return SpringHolder.getBean(ConnectionFactory.class);
	}

	@Bean
	public DescriptionService descriptionService() {
		DescriptionServiceImpl descriptionService = new DescriptionServiceImpl();
		DescriptionEntityMapper descriptionDao = SpringHolder.getBean(DescriptionEntityMapper.class);
		descriptionService.setDescriptionDao(descriptionDao);
		return descriptionService;
	}

	@Bean
	public TaskInfoService taskInfoService() {
		TaskInfoServiceImpl taskInfoService = new TaskInfoServiceImpl();
		TaskInfoEntityMapper taskInfoDao = SpringHolder.getBean(TaskInfoEntityMapper.class);
		taskInfoService.setTaskInfoDao(taskInfoDao);
		return taskInfoService;
	}

	@Bean
	public Queue descriptionQueue() {
		Queue queue = new Queue(QUEUE_DESCRIPTION);
		return queue;
	}

	@Bean
	public Queue taskInfoQueue() {
		return new Queue(QUEUE_TASK_INFO);
	}

	@Bean
	public DirectExchange exchange() {
		// 一个交换器
		return new DirectExchange(EXCHANGE);
	}

	@Bean
	public Binding bindingDescription() {
		return BindingBuilder.bind(descriptionQueue()).to(exchange()).with(ROUTE_DESCRIPTION);
	}

	@Bean
	public Binding bindingTaskInfo() {
		return BindingBuilder.bind(taskInfoQueue()).to(exchange()).with(ROUTE_TASK_INFO);
	}

	@Bean
	public RabbitTemplate descriptionTemplate() {
		// 发送方模板
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setExchange(EXCHANGE);
		rabbitTemplate.setRoutingKey(ROUTE_DESCRIPTION);
		return rabbitTemplate;
	}

	@Bean
	public RabbitSender<DescriptionEntity> descriptionSender() {
		// DescriptionEntity 发送方
		SenderAdapter<DescriptionEntity> sender = new SenderAdapter<DescriptionEntity>() {
		};
		sender.setRabbitTemplate(descriptionTemplate());
		return sender;
	}

	@Bean
	public RabbitTemplate taskInfoTemplate() {
		// 发送方模板
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setExchange(EXCHANGE);
		rabbitTemplate.setRoutingKey(ROUTE_TASK_INFO);
		return rabbitTemplate;
	}

	@Bean
	public RabbitSender<TaskInfoEntity> taskInfoSender() {
		// TaskInfoEntity 发送方
		SenderAdapter<TaskInfoEntity> sender = new SenderAdapter<TaskInfoEntity>() {
		};
		sender.setRabbitTemplate(taskInfoTemplate());
		return sender;
	}

	@Bean
	public SimpleMessageListenerContainer listenerDescriptionContainer() {
		// 接收方
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames(QUEUE_DESCRIPTION);

		RecieveDescription messageListener = new RecieveDescription();
		messageListener.setDescriptionService(descriptionService());

		container.setMessageListener(messageListener);
		return container;
	}

	@Bean
	public SimpleMessageListenerContainer listenerTaskInfoContainer() {
		// 接收方
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames(QUEUE_TASK_INFO);

		RecieveTaskInfo messageListener = new RecieveTaskInfo();
		messageListener.setTaskInfoService(taskInfoService());

		container.setMessageListener(messageListener);
		return container;
	}

}
