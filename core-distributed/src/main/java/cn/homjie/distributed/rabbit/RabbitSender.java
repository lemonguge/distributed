package cn.homjie.distributed.rabbit;

public interface RabbitSender<T> {
	
	public void send(T object);
}
