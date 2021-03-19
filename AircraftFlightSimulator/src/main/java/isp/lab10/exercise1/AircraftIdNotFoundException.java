package isp.lab10.exercise1;

public class AircraftIdNotFoundException extends Exception {
    public AircraftIdNotFoundException(){
        System.out.println("Aircraft id hasn't been found");
    }
}
