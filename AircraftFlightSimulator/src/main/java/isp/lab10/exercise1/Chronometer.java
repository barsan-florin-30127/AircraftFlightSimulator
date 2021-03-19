package isp.lab10.exercise1;

/**
 * Class that represents each aircraft's chronometer.
 * The chronometer starts when the aircraft enters in cruise state and counts how many seconds it spent on that state.
 */
public class Chronometer implements Runnable {
    private int k;
    private boolean active = true;

    public void run() {
        while (active) {
            k++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
        }
    }

    public int stopChronometer() {
        active = false;
        return k;
    }
}
