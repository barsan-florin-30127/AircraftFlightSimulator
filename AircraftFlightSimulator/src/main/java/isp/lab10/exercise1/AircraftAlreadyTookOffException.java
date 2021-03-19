package isp.lab10.exercise1;

public class AircraftAlreadyTookOffException extends Exception {
    public AircraftAlreadyTookOffException() {
        System.out.println("Airplane already took off.");
    }
}
