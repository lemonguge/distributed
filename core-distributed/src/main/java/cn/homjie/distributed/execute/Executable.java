package cn.homjie.distributed.execute;

@FunctionalInterface
public interface Executable<T> {

	T handle() throws Exception;

}