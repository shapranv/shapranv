package shapranv.shell.utils.executors;

import java.util.concurrent.Executor;

public interface DisposableExecutor extends Executor, Disposable, TaskCounter, WithConflation {
}
