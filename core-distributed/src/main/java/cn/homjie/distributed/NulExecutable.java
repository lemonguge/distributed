package cn.homjie.distributed;

@FunctionalInterface
public interface NulExecutable extends Executable<Void> {

	default Void handle() throws Exception {
		handleWithoutResult();
		return null;
	}

	void handleWithoutResult() throws Exception;

}