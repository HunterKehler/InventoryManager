/**
 * Created class InHouse.java
 * @author Hunter Kehler
 */
package hunterkehler.inventorymanager;

/**
 * Part that has a MachineID attached.
 */
public class InHouse extends Part {
    private int machineId;
    /**
     * Default Outsourced Part constructor
     * @param id the id of the Part
     * @param name the name of the Part
     * @param price the price of the Part
     * @param stock the inventory level of the Part
     * @param min the minimum stock of the Part
     * @param max the maximum stock of the Part
     * @param machineId the ID of the machine that made this Part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * @return the machineId
     */
    public int getMachineId() {
        return machineId;
    }
}
