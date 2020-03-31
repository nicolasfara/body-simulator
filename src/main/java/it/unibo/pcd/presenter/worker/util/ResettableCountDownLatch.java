package it.unibo.pcd.presenter.worker.util;

public class ResettableCountDownLatch {
    private final int nParticipants;
    private int counter;

    public ResettableCountDownLatch(final int nParticipants) {
        this.nParticipants = nParticipants;
        counter = nParticipants;
    }
    
    public synchronized void await() throws InterruptedException {
        while (counter > 0) {
            wait();
        }
    }
    
    public synchronized void down() {
        counter--;
        if (counter == 0) {
            notifyAll();
        }
    }
    
    public synchronized void reset() {
        counter = nParticipants;
    }
}
