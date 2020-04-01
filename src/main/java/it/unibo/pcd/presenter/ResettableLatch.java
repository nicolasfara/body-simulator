package it.unibo.pcd.presenter;

public class ResettableLatch {

    private int nParticipants;
    private int counter;

    public ResettableLatch(int nParticipants) {
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
