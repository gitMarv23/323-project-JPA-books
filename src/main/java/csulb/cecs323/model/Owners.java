package csulb.cecs323.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Owners {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long owner_id;

    //OnetoMany to have an owner "own" multiple cars
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Cars> cars;

    /** The surname of the car owner. */
    @Column(length=60, nullable = false, updatable = false) //Be sure to set max length & NOT NULL where appropriate
    private String last_name;

    /** The given name of the car owner. */
    @Column (length=60, nullable = false) //Be sure to set max length & NOT NULL where appropriate
    private String first_name;

    /** Their cell phone number that we use to contact the car owner. */
    @Column (length=20, nullable = false, unique = true)
    private String cell_phone;

    public Owners (String first_name, String last_name, String cell_phone) {
        this.setLast_name(last_name);
        this.setFirst_name(first_name);
        this.setCell_phone(cell_phone);
    }

    public Owners () {}

    public long getOwner_id() {
        return owner_id;
    }

    // Removed the setOwner_id() because it should be immutable.

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getCell_phone() {
        return cell_phone;
    }

    public void setCell_phone(String cell_phone) {
        this.cell_phone = cell_phone;
    }

    @Override
    public String toString () {
        return "Owner - ID: " + this.getOwner_id() + " Last Name: " + this.getLast_name() +
                " First Name: " + this.getFirst_name() +
                " Phone #: " + this.getCell_phone();
    }
}
