package cn.homjie.distributed;

public enum Times {
	// 仅 Transaction.EVENTUAL 有效

	// 有结果则使用，否则处理
	ONCE,

	// 每次运行都执行，不检查
	ALWAYS,

	// 每次运行都执行，检查比较
	CHECK;

}
