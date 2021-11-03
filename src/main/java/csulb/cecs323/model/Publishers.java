package csulb.cecs323.model;


import javax.persistence.*;
import java.util.List;


@Table(name = "Publishers", uniqueConstraints = {
        @UniqueConstraint(name = "uc_publishers_email", columnNames = {"email", "phone"})
})
@NamedNativeQuery(
        name="ReturnPublisher",
        query = "SELECT * " +
                "FROM   PUBLISHERS " +
                "WHERE  PUBLISHER_NAME = ? ",
        resultClass = Publishers.class
)
@NamedNativeQuery(
        name="ReturnAllPublisher",
        query = "SELECT * " +
                "FROM   PUBLISHERS ",
        resultClass = Publishers.class
)
@Entity
/**
 * The Publishers class will represent our Publisher objects to be manipulated within the database.
 * Initially, the Native Queries ensure the data is reflected to the user prior to their
 * requested adjustments.
 */
public class Publishers {
    /**
     * name is used as the primary ID for the publishers table, and is for the publisher object instance
     */
    @Id
    @Column(nullable = false, name = "publisher_name", length = 80)
    private String name;

    /**
     * phone refers to the phone number attached to a publisher for the current publisher instance
     */
    @Column(nullable = false, name = "phone", length = 24)
    private String phone;

    /**
     * email indicates the email attached or affiliated with a specific publisher instance
     */
    @Column(nullable = false, name = "email", length = 80)
    private String email;


    /**
     * Getter function to obtain the Book's given ISBN
     * @return  book ISBN
     */
    public String getName() {
        return name;
    }


    /**
     * books will link the Books object to the Publishers; essentially they are mapped by the publisherName that the
     * book has in the ManyToOne annotation in Books.
     */
    // one to many reevaluation since relation scheme changed with publisher_name pk to foreign_key in books
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisherName", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Books> books;

    /**
     * Publisher instance overloaded constructor based on user input from Library main.
     * @param name          name of the publisher
     * @param email         email of the publisher
     * @param phone         phone number of the  publisher
     */
    public Publishers(String name, String email, String phone){
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Default constructor for current Publisher class
     * */
    public Publishers(){

    }

    /**
     * OverloadedToString function to display all information from given Publisher instance
     * @return   the name, email and phone number of a given publisher
     */
    @Override
    public String toString(){
        return "Publishers - Name: " + this.name + "   Email: " + this.email +
                "   Phone: " + this.phone;
    }
}
