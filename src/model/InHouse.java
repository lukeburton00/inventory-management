package model;
/**
 *
 * @author Luke Burton
 */
public class InHouse extends Part{

    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     *
     * @return the id of the machine
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     *
     * @param machineId the id to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
