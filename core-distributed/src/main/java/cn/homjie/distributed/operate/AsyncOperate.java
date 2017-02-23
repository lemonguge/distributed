package cn.homjie.distributed.operate;

import java.util.List;

public interface AsyncOperate<T> {

	public List<T> select(T condition);

	public T unique(T condition);

	public void insert(T object);

	public void update(T object);

}
