package csulb.cecs323.model;

import javax.persistence.*;
import java.util.List;


@NamedNativeQuery(
        name = "ReturnWriting",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES " +
                "WHERE  AUTHORING_ENTITY_TYPE = 'Writing Group' and EMAIL = ?",
        resultClass = WritingGroups.class
)
@NamedNativeQuery(
        name = "ReturnAllWriting",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES " +
                "WHERE  AUTHORING_ENTITY_TYPE = 'Writing Group'",
        resultClass = WritingGroups.class
)
@Entity
@DiscriminatorValue("Writing Group")
/**
 * Class to represent a single writing group to be entered into the database.
 * This class will be directly represented as the head writer and year formed columns inside
 * the authoring entities table in the database
 */
public class WritingGroups extends Authoring_Entities {

    /**
     * List of the authoring entities that the Writing group happens to be in mapped by the email
     * We aren't entirely sure if this is needed, but we will leave it in case
     * Deleting it will cause an error. Apologies for the confusion
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "email", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Authoring_Entities> email;


    /**
     * head writer refers to the head writer name of a writing group
     */
    @Column(length = 30, name = "head_writer")
    private String head_Writer;

    /**
     * year formed refers to the year that the given writing group was formed
     */
    private int year_Formed;

    /**
     * Getter function to obtain the year formed
     *
     * @return book ISBN
     */
    public int getYear_Formed() {
        return year_Formed;
    }


    /**
     * Default constructor for the current Writing Group CLass
     */
    public WritingGroups() {

    }

    /**
     * Publisher instance overloaded constructor based on user input from Library main.
     *
     * @param email       email of the writing group
     * @param name        name of the writing group
     * @param head_Writer head writer name
     * @param yearFormed  year that the writing group was formed
     */
    public WritingGroups(String email, String name, String head_Writer, int yearFormed) {
        super(email, name);
        this.head_Writer = head_Writer;
        this.year_Formed = yearFormed;
    }

    /**
     * Overloaded constructor to display all the information given of a WritingGroup instance
     *
     * @return returns the email, name, headwriter and year formed of a specifc writing group
     */
    @Override
    public String toString() {
        return "WritingGroups - " +
                "Email: " + super.getEmail() +
                "   Name: " + super.getName() +
                "   Head Writer: " + head_Writer +
                "   Year Formed=" + year_Formed;
    }
}
