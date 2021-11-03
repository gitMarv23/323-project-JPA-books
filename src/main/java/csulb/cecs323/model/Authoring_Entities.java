package csulb.cecs323.model;

/**
 * Imported Libraries used in class
 */

import javax.persistence.*;
import java.util.List;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "ReturnAuthor",
                query = "SELECT * " +
                        "FROM   AUTHORING_ENTITIES " +
                        "WHERE  EMAIL = ? ",
                resultClass = Authoring_Entities.class
        ),
        @NamedNativeQuery(
                name = "ReturnAllAuthors",
                query = "Select * " +
                        "FROM AUTHORING_ENTITIES",
                resultClass = Authoring_Entities.class
        )
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Authoring_Entity_Type", discriminatorType = DiscriminatorType.STRING)

/**
 * Authoring entities is the generalization for the entities involved with the creation, improvement, and disbursement of our Books class.
 */
public abstract class Authoring_Entities {

    /**
     * Email of the Authoring Entity
     */
    @Id
    @Column(nullable = false, length = 30)
    private String email;

    /**
     * Name of the authoring entity
     */
    @Column(nullable = false, length = 80)
    private String name;

    /**
     * List of books that the authoring entity manages
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authoringName", cascade = CascadeType.PERSIST)
    private List<Books> books;

    /**
     * Gets the discriminator value required for  relational mapping
     *
     * @return discriminator value if exists
     * @return null if discriminator value does not exits
     */
    @Transient
    public String getDiscriminatorValue() {
        DiscriminatorValue val = this.getClass().getAnnotation(DiscriminatorValue.class);

        return val == null ? null : val.value();
    } // end of getDiscriminatorValue

    /**
     * Default constructor for class instance
     */
    public Authoring_Entities() {

    } // end of default constructor

    /**
     * Get the name for authoring entity instance
     *
     * @return authoring entity name
     */
    public String getName() {
        return name;
    } // end of getName

    /**
     * Overloaded constructor with parameters entered byt the user
     *
     * @param email existing authoring entity email
     * @param name  existing authoring entity name
     */
    public Authoring_Entities(String email, String name) {
        this.email = email;
        this.name = name;
    } // end of Authoring_Entities overloaded constructor

    /**
     * Gets authoring entity email
     *
     * @return authoring entity email
     */
    public String getEmail() {
        return email;
    } // end of getEmail

    /**
     * Overloaded ToString constructor to display Authoring Entities information
     *
     * @return Authoring entity email and name
     */
    @Override
    public String toString() {
        return "Authoring Entities - " + "Email: " + this.email + "   Name: " + this.name;
    }
}
