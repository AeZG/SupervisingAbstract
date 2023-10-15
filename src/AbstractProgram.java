import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class AbstractProgram extends Thread {
    private ProgramState state;
    private final long interval;
    private boolean stopRequested;
    private final Object stateLock = new Object();

    public AbstractProgram(long interval) {
        this.state = ProgramState.UNKNOWN;
        this.interval = interval;
        this.stopRequested = false;
        System.out.println("AbstractProgram starting state set to " + this.state);
    }

    public long getInterval() {
        return interval;
    }

    public void waitForStateChange() throws InterruptedException {
        synchronized (stateLock) {
            stateLock.wait();
        }
    }

    public void notifyStateChange() {
        synchronized (stateLock) {
            stateLock.notifyAll();
        }
    }

    public void stopProgram() {
        synchronized (stateLock) {
            stopRequested = true;
            stateLock.notifyAll();
        }
    }

    public ProgramState getProgramState() {
        synchronized (stateLock) {
            return state;
        }
    }

    @Override
    public void run() {
        while (!stopRequested) {
            synchronized (stateLock) {
                if (stopRequested) break;
                int newStateIndex = 1 + (int) (Math.random() * ProgramState.values().length - 1);
                state = ProgramState.values()[newStateIndex];
                System.out.println("AbstractProgram state changed to " + state);
                stateLock.notifyAll();
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
