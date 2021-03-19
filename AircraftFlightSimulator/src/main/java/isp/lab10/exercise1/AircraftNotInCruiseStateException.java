package isp.lab10.exercise1;

public class AircraftNotInCruiseStateException extends Exception {
    public AircraftNotInCruiseStateException() {
        System.out.println("Aircraft isn't in cruise mode.");
    }
}
