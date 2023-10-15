class Supervisor extends Thread {
    private AbstractProgram abstractProgram;
    private boolean stopRequested;

    public Supervisor(AbstractProgram abstractProgram) {
        this.abstractProgram = abstractProgram;
        this.stopRequested = false;
    }

    @Override
    public void run() {
        while (!stopRequested) {
            while (!stopRequested && abstractProgram.getProgramState() != ProgramState.STOPPING
                    && abstractProgram.getProgramState() != ProgramState.FATAL_ERROR) {
                try {
                    abstractProgram.waitForStateChange();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            ProgramState state = abstractProgram.getProgramState();
            if (state == ProgramState.STOPPING) {
                System.out.println("Supervisor restarting AbstractProgram");
                abstractProgram.stopProgram();
                try {
                    abstractProgram.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                abstractProgram = new AbstractProgram(abstractProgram.getInterval());
                abstractProgram.start();
            } else if (state == ProgramState.FATAL_ERROR) {
                System.out.println("Supervisor stopping AbstractProgram due to FATAL ERROR");
                abstractProgram.stopProgram();
                break;
            }
        }
    }

    public void stopSupervisor() {
        stopRequested = true;
        abstractProgram.notifyStateChange();
        abstractProgram.interrupt();
    }
}
