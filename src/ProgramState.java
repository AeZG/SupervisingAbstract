import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

enum ProgramState {
    UNKNOWN,
    STOPPING,
    RUNNING,
    FATAL_ERROR
}
