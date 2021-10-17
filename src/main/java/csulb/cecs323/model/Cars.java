package csulb.cecs323.model;

import javax.persistence.*;

/**
 * Individual, physical automobiles that someone can drive on land to transport one or more passengers
 * and a limited amount of cargo around.  Cars have four wheels and usually travel on paved roads.
 */
@Entity
public class Cars {
    /** The unique ID of the vehicle.  Limited to 17 characters. */
    //Declaring the VIN as the primary key
    @Id
    //Setting the length of the column to be 17, and it cannot be null
    @Column(length = 17, nullable = false)
    private String VIN;

    //Many to one relation to Owners by owner_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id",  nullable = false)
    private Owners owner;

    /** The name of the corporation which manufactured the vehicle.  Limited to 40 characters. */
    @Column(length = 40,  nullable = false)
    private String manufacturer;

    /** The popular name of the vehicle, like the Prius for Toyota.  Limited to 20 characters. */
    @Column(length = 20,  nullable = false)
    private String model;

    /** The year that the vehicle was manufactured.  For now, do not worry about validating this #. */
    //For some reason, I kept getting an error when this was just "year" so I changed it and it worked. Not sure if this was intended/allowed, but I had to do it
    private int year_;

    //Many to one relation to auto_body_styles by the style name
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auto_body_style_name")
    private auto_body_styles autoBody;

    //public default constructor
    public Cars(){

    }

    //constructor that takes in inputs
    public Cars(String vin,String manufacturer, String model, int year,Owners owner, auto_body_styles styles){
        this.VIN=vin;
        this.manufacturer=manufacturer;
        this.model = model;
        this.year_ = year;
        this.owner = owner;
        this.autoBody = styles;
    }

    public String getModel() {
        return model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    @Override
    public String toString () {
        return "Cars - VIN: " + this.VIN + " Manufacturer: " + this.manufacturer +
                " Model: " + this.model + " year: " + this.year_;
    }
}
