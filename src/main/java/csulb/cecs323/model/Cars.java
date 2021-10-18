package csulb.cecs323.model;

import javax.persistence.*;

/**
 * Individual, physical automobiles that someone can drive on land to transport one or more passengers
 * and a limited amount of cargo around.  Cars have four wheels and usually travel on paved roads.
 */
@Entity
public class Cars {
    /** The unique 17-character ID of the vehicle.  Limited to 17 characters. Annotated as primary key*/
    @Id
    @Column(nullable = false, length = 17)
    private String VIN;

    /** Many-to-One relationship with owners and auto_body_styles based on owner_id and auto_body_style_name foreign keys */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, referencedColumnName = "owner_id")
    private Owners owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_body_style_name", nullable = false, referencedColumnName = "name")
    private auto_body_styles autoBodyStyle;

    /** The name of the corporation which manufactured the vehicle.  Limited to 40 characters. */
    @Column(name = "manufacturer", nullable = false, length = 40)
    private String manufacturer;

    /** The popular name of the vehicle, like the Prius for Toyota.  Limited to 20 characters. */
    @Column(nullable = false, length = 20)
    private String model;

    /** The year that the vehicle was manufactured.  For now, do not worry about validating this #. */
    @Column(nullable = false)
    private int modelYear;

    /** Cars constructor with NON-NULL values */
    public Cars(String vin, String manufacturer, String model, int modelYear, Owners owner, auto_body_styles autoBodyStyle) {
        this.VIN = vin;
        this.manufacturer = manufacturer;
        this.model = model;
        this.modelYear = modelYear;
        this.owner = owner;
        this.autoBodyStyle = autoBodyStyle;
    }

    /** Default Cars Constructor */
    public Cars(){

    }

    @Override
    public String toString () {
        return "Cars - VIN: " + this.VIN + " Manufacturer: " + this.manufacturer +
                " Model: " + this.model + " year: " + this.modelYear;
    }
}
