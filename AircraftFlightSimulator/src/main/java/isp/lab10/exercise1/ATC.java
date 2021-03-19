package isp.lab10.exercise1;

import java.util.List;


/**
 * Class that represents the layer which user interacts with commands and aircraft system.
 */
public class ATC {
    /**
     * Attribute that contains Aircraft objects stored in a list.
     */
    private List<Aircraft> aircraftList;

    /**
     * Main constructor that assign to our class attribute a list of Aircraft objects.
     *
     * @param aircraftList - list of Aircraft objects.
     */
    public ATC(List<Aircraft> aircraftList) {
        this.aircraftList = aircraftList;
    }

    /**
     * Method that add Aircraft objects into our attribute field list.
     *
     * @param id represents the unique id of every aircraft.
     *           it is used to differentiate one Aircraft from another.
     */
    public void addAircraft(String id) {
        Aircraft tempAircraft = new Aircraft(id);
        if (this.aircraftList.contains(tempAircraft)) {
            System.out.println("Aircraft with id " + tempAircraft.getId() + " already exist!");
        } else {
            this.aircraftList.add(tempAircraft);
            Thread tempName = new Thread(tempAircraft);
            Thread.currentThread().setName(id);
            tempName.start();
            System.out.println("The aircraft with id " + tempAircraft.getId() + " has been added.");
        }
    }

    /**
     * Method that send commands based on user's preferences to the desired aircraft.
     * After we have the id and the command, this method will call to the desired aircraft the receivedCommand method.
     * If the aircraft with introduced id isn't found in the list, a message is shown.
     * To search a aircraft, we create a temporary object with the searched id (tempAircraft) and
     *                      call the overrided equals method.
     * UnknownCommandException is thrown when the user send another command to an aircraft than took off or land
     * AircraftIdNotFoundException is thrown when id entered by user couldn't be found in ATC's aircraft list
     *
     * @param aircraftId - id of the aircraft which will receive the specific command.
     * @param cmd        - command that we want to transmit to the aircraft.
     */
    public void sendCommand(String aircraftId, AtcCommand cmd) throws AircraftAlreadyTookOffException, AircraftNotInCruiseStateException, AircraftIdNotFoundException, UnknownCommandException {
        Aircraft tempAircraft = new Aircraft(aircraftId);
        boolean found = false;
        for (int i = 0; i < aircraftList.size(); i++) {
            if (this.aircraftList.get(i).equals(tempAircraft)) {
                found = true;
                if (cmd instanceof TakeoffCommand) {
                    this.aircraftList.get(i).receiveAtcCommand(new TakeoffCommand(((TakeoffCommand) cmd).getAltitude()));
                } else if (cmd instanceof LandCommand) {
                    found = true;
                    this.aircraftList.get(i).receiveAtcCommand(new LandCommand());
                } else {
                    throw new UnknownCommandException();
                }
            }
        }
        if (!found) {
            throw new AircraftIdNotFoundException();
        }
    }

    /**
     * Method that shows us all aircraft that has been added into the list.
     * This method uses the overrided toString() method from the Aircraft class.
     */
    public void showAircrafts() {
        for (int i = 0; i < aircraftList.size(); i++) {
            this.aircraftList.get(i).toString();
        }
    }
}
