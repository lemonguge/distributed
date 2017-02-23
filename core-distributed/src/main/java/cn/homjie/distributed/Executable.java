package cn.homjie.distributed;

@FunctionalInterface
public interface Executable<T> {

	T handle() throws Exception;

}