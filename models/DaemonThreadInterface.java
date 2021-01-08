package models;

public interface DaemonThreadInterface extends Runnable {
    void start();

    @Override
    void run();
}
