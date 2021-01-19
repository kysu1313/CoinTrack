//package models;
//
//public class DaemonThread implements DaemonThreadInterface {
//
//    private final boolean DEBUG = controllers.Tab1Controller.DEBUG;
//    private Thread thread;
//
//    public DaemonThread() {
//        start();
//    }
//
//    /**
//     * Create a new thread if the thread is not already started.
//     */
//    @Override
//    public void start() {
//        if (DEBUG){System.out.println("Starting Thread");}
//        if (this.thread == null){
//            this.thread = new Thread(this);
//            this.thread.setDaemon(true);
//            this.thread.start();
//        }
//    }
//
//    @Override
//    public void run() {
//
//    }
//}
