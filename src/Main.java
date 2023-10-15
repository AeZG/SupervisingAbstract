public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Create an AbstractProgram with a random state change interval of 2000 milliseconds
        AbstractProgram abstractProgram = new AbstractProgram(1000);

        // Create a Supervisor to monitor the AbstractProgram
        Supervisor supervisor = new Supervisor(abstractProgram);

        // Start the Supervisor and AbstractProgram
        supervisor.start();
        abstractProgram.start();

        // Let them run for some time (e.g. 10000 milliseconds)
        Thread.sleep(10000);

        // Stop the Supervisor and AbstractProgram
        supervisor.stopSupervisor();
        abstractProgram.stopProgram();
    }
}