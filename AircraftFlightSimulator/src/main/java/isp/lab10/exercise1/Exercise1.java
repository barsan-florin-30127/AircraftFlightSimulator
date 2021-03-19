package isp.lab10.exercise1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * Main class that contains the user interface and ATC object and represents the layer that user interacts with.
 * Commands are read through a scanner object.
 */
public class Exercise1 {
    public static void main(String[] args) {
        List<Aircraft> aircraftList = new ArrayList<>();
        ATC atc = new ATC(aircraftList);
        System.out.println("Menu options list - 1");
        System.out.println("Add aircraft- 2");
        System.out.println("Show aircraft list- 3");
        System.out.println("Takeoff command- 4");
        System.out.println("Land command - 5");
        System.out.println("Exit - 6");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Introduce the number of the option you want to use:");
            int choice = (scanner.nextInt());
            switch (choice) {
                case 1: {
                    System.out.println("Menu options list - 1");
                    System.out.println("Add aircraft- 2");
                    System.out.println("Show aircraft list- 3");
                    System.out.println("Takeoff command- 4");
                    System.out.println("Land command - 5");
                    System.out.println("Exit - 6");
                    break;
                }
                case 2: {
                    System.out.println("Introduce the aircraft id:");
                    String aircraftName = scanner.next();
                    atc.addAircraft(aircraftName);
                    break;
                }
                case 3: {
                    atc.showAircrafts();
                    break;
                }
                case 4: {
                    System.out.println("Introduce the aircraft id:");
                    String aircraftName = (scanner.next());
                    System.out.println("Introduce the cruising altitude:");
                    int crusingAltitude = (scanner.nextInt());
                    try {
                        atc.sendCommand(aircraftName, new TakeoffCommand(crusingAltitude));
                    } catch (AircraftAlreadyTookOffException e) {
                        e.printStackTrace();
                    } catch (AircraftNotInCruiseStateException aircraftNotInCruiseState) {
                        aircraftNotInCruiseState.printStackTrace();
                    } catch (AircraftIdNotFoundException aircraftIdNotFound) {
                        aircraftIdNotFound.printStackTrace();
                    } catch (UnknownCommandException unknownCommand) {
                        unknownCommand.printStackTrace();
                    }
                    break;
                }
                case 5: {
                    System.out.println("Introduce the aircraft id:");
                    String aircraftName = (scanner.next());
                    try {
                        atc.sendCommand(aircraftName, new LandCommand());
                    } catch (AircraftAlreadyTookOffException e) {
                        e.printStackTrace();
                    } catch (AircraftNotInCruiseStateException aircraftNotInCruiseState) {
                        aircraftNotInCruiseState.printStackTrace();
                    } catch (AircraftIdNotFoundException aircraftIdNotFound) {
                        aircraftIdNotFound.printStackTrace();
                    } catch (UnknownCommandException unknownCommand) {
                        unknownCommand.printStackTrace();
                    }
                    break;
                }
                case 6: {
                    exit(1);
                }
                default: {
                    System.out.println("Introduce a number from the list !");
                }
            }
        }
    }
}
