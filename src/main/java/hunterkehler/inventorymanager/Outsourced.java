/**
 * Created class Outsourced.java
 * @author Hunter Kehler
 */
package hunterkehler.inventorymanager;

/**
 * Part that has a CompanyName attached.
 */
public class Outsourced extends Part{
    private String companyName;

    /**
     * Default Outsourced Part constructor
     * @param id the id of the Part
     * @param name the name of the Part
     * @param price the price of the Part
     * @param stock the inventory level of the Part
     * @param min the minimum stock of the Part
     * @param max the maximum stock of the Part
     * @param companyName the name of the company the Part was made in
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }
}
