package isp.lab10.exercise1;

import java.util.Objects;

/**
 * Class that represents the main objects from this application.
 * Each aircraft represents a Thread , reason why we implement Runnable class.
 * Attributes of the class:
 * Integer y = new Integer(10) - object that we use to synchronize our thread.
 * private int secondsInCruiseMode - int that contains the time spent (secounds) of each aircraft in cruise mode.
 * private boolean active - parameter used to start/stop our thread.
 * private boolean paused - parameter used to pause our thread.
 * private String id - unique for each aircraft, allow us to differentiate two aircraft objects.
 * private int altitude - initially 0, will contain the altitude when the aircraft will enter in cruise mode.
 * private State state - contain the current state of the aircraft,will be changed based on aircraft's state.
 */
public class Aircraft implements Runnable {
    Integer y = new Integer(10);
    private int secondsInCruiseMode = 0;
    private boolean active;
    private boolean paused;
    private String id;
    private int altitude;
    private State state;

    /**
     * Main constructor of our aircraft class.
     *
     * @param id - user set the id for the aircraft.
     *           Initial state is OnStand to allow our aircraft to take off.
     *           Active attribute is set to true to allow our thread to start.
     *           Paused is set true to pause the thread until a take of command arrives.
     */
    public Aircraft(String id) {
        this.id = id;
        this.altitude = 0;
        this.state = State.OnStand;
        this.active = true;
        this.paused = true;

    }

    public String getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    /**
     * Method that receive commands and influence our aircraft thread based on them.
     * Take off command-Can be applied only if Aircraft is in On Stand state and it hasn't been applied already one time.
     *                  It comes along with the cruise altitude parameter that will set our altitude attribute .
     *                  When this command is called,the state goes changes to Taxing and our thread is notified to exit.
     *                  from it's blocked state.
     * Land command-Can be applied only if aircraft is in Cruise state.
     *              This command set current state to Descending and notify and our thread is notified to exit
     *              from it's blocked state.
     * AircraftAlreadyTookOffException is thrown when aircraft receives a takeoff command but he already executed one.
     * AircraftNotInCruiseStateException is thrown when aircraft receives a land command but he isn't in cruise mode.
     *
     * @param cmd - should contain one of the command explained before.
     */
    public void receiveAtcCommand(AtcCommand cmd) throws AircraftAlreadyTookOffException, AircraftNotInCruiseStateException {
        if (this.active == false) {
            System.out.println("Airplane with id " + this.id + " already flew one time.");
            throw new AircraftAlreadyTookOffException();
        } else {
            if (cmd instanceof TakeoffCommand) {
                if (this.state == State.OnStand) {
                    System.out.println("Take off command has been called.");
                    this.altitude = ((TakeoffCommand) cmd).getAltitude();
                    this.setState(State.Taxing);
                    this.exitPause();
                } else {
                    throw new AircraftAlreadyTookOffException();
                }
            } else if (cmd instanceof LandCommand) {
                if (this.state == State.Cruise) {
                    System.out.println("Landing command has been called.");
                    this.setState(State.Descending);
                    this.exitPause();
                } else {
                    throw new AircraftNotInCruiseStateException();
                }
            }
        }
    }

    /**
     * Method that set our thread into the Blocked state until it is notified to exit it.
     */
    public void setPause() {
        paused = true;
    }

    /**
     * Method that make our thread to leave the blocked state (notifies him).
     */
    public void exitPause() {
        paused = false;
        synchronized (y) {
            y.notify();
        }
    }

    @Override
    public String toString() {
        System.out.println("Aircraft{" +
                "id='" + id + '\'' +
                ", altitude=" + altitude +
                '}');
        return "Aircraft{" +
                "id='" + id + '\'' +
                ", altitude=" + altitude +
                '}';
    }

    /**
     * Overrided toString() method that makes two objects with the same id to be equal.
     * We use it to match two aircraft objects.
     *
     * @param o Aircraft object.
     * @return true/false based on the state of equality between the two objects.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aircraft aircraft = (Aircraft) o;
        return Objects.equals(id, aircraft.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Represents each aircraft's flow and what it should do based on it's state.
     * On the On Stand state the aircraft wait(is blocked) for the take off command which put it in Taxing state.
     * In Taxing state it waits 5 seconds and then go into Ascending state.
     * In Ascending state it will wait 10*altitude seconds until he reaches it's Cruise state when it is put on pause.
     * When Cruise state starts, we create a chronometer c1 that count how many seconds our aircraft stand in that state.
     * When Land Command arrives,our chronometer stops and register seconds into the specific attribute and our aircraft
     *                           is unlocked to continue and put in Descending state.
     * In Descending state our aircraft wait for 10*altitude seconds to land.
     * When aircraft landed, we put it into Landed state and show the seconds spent in the Cruise state.
     * After that we stop our thread setting "active" attribute to false.
     */
    @Override
    public void run() {
        while (active) {
            this.paused = true;
            this.state = State.OnStand;
            if (this.getState() == State.OnStand) {
                System.out.println("Airplane with id " + this.getId() + " is in On Stand state...");
                this.setPause();
            }
            if (paused) {
                synchronized (y) {
                    try {
                        y.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (this.getState() == State.Taxing) {
                System.out.println("Airplane with id: " + this.getId() + " went into Taxing state...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.setState(State.Takeoff);
            }
            if (this.getState() == State.Takeoff) {
                System.out.println("Airplane with id: " + this.getId() + " went into Takeoff state...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.setState(State.Ascending);
            }
            if (this.getState() == State.Ascending) {
                System.out.println("Airplane with id: " + this.getId() + " is ascending...");
                try {
                    Thread.sleep(10000 * this.altitude);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Airplane with id: " + this.getId() + " has reached cruising altitude...");
                this.setState(State.Cruise);
                Chronometer c1 = new Chronometer();
                Thread t1 = new Thread(c1);
                t1.start();
                this.setPause();
                if (paused) {
                    synchronized (y) {
                        try {
                            y.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                this.secondsInCruiseMode = c1.stopChronometer();
            }
            if (this.getState() == State.Descending) {
                System.out.println("Airplane with id: " + this.getId() + " went into Descending state...");
                try {
                    Thread.sleep(10000 * this.altitude);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.setState(State.Landed);
                System.out.println("Airplane with id: " + this.getId() + " landed successfully...");
                System.out.println("It spent " + this.secondsInCruiseMode + " seconds in cruise mode.");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.setState(State.OnStand);
            this.active = false;
        }
    }
}

