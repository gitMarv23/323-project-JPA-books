package csulb.cecs323.model;

/**
 * Imports necessary for current project
 */

import javax.persistence.*;
import java.util.*;

/**
 * Class to represent a single author to be entered into the database.
 * Relation scheme Foreign Key connection via author's email address
 */
@Entity
@NamedNativeQuery(
        name = "ReturnIndividualAuthor",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES " +
                "WHERE AUTHORING_ENTITY_TYPE = 'Individual Authors' and EMAIL = ? ",
        resultClass = Authoring_Entities.class
)
@NamedNativeQuery(
        name = "ReturnAllIndividualAuthors",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES " +
                "WHERE AUTHORING_ENTITY_TYPE = 'Individual Authors'",
        resultClass = Authoring_Entities.class
)
@DiscriminatorValue("Individual Authors")
public class Individual_Authors extends Authoring_Entities {

    /**
     * List of Ad Hoc Team the Individual Author will belong to.
     * Relation scheme depicts the email Foreign Key connections
     */
    @ManyToMany(mappedBy = "individual_authors",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Ad_Hoc_Teams_Members> adHocTeamMembers;

    /**
     * Default constructor where values are originally null,
     * however, the database cannot contain null values for these attributes.
     * Thus, overloaded constructor will be used.
     */
    public Individual_Authors() {

    }

    /**
     * Output Individual Author information using the Authoring entity super class
     *
     * @return information to output
     */
    @Override
    public String toString() {
        return super.toString();
    } // end of toString

    /**
     * Overloaded constructor to build an Individual Author with given.
     * Creates Ad Hoc Teams List to represent team author will be a part of if applicable.
     *
     * @param email Email Address inputted by the user.
     * @param name  Individual author name to be inputted by user.1
     */
    public Individual_Authors(String email, String name) {
        super(email, name);
        this.adHocTeamMembers = new ArrayList<Ad_Hoc_Teams_Members>();
    }
}
