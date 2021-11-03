package csulb.cecs323.model;

/**
 * Imported collections to be used in the AdHocTeams Class
 */

import javax.persistence.*;
import java.util.*;

/**
 * Ad Hoc Team Authoring Entity to publish Books objects
 */
@Entity
@NamedNativeQuery(
        name = "ReturnAdHocTeamMember",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES " +
                "WHERE AUTHORING_ENTITY_TYPE = 'AD Hoc Teams' and EMAIL = ? ",
        resultClass = Ad_Hoc_Teams_Members.class
)
@NamedNativeQuery(
        name = "ReturnAllAdHocTeamMembers",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES " +
                "WHERE AUTHORING_ENTITY_TYPE = 'AD Hoc Teams' ",
        resultClass = Ad_Hoc_Teams_Members.class
)
@DiscriminatorValue("AD Hoc Teams")
public class Ad_Hoc_Teams_Members extends Authoring_Entities {
    /**
     * List of each individual author involved in the Ad Hoc Team
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "Ad_hoc_teams_member",
            joinColumns = @JoinColumn(name = "ad_hoc_teams_email"),
            inverseJoinColumns = @JoinColumn(name = "individual_authors_email")
    )
    private List<Individual_Authors> individual_authors;

    /**
     * Ad_Hoc_Teams_Members default constructor
     */
    public Ad_Hoc_Teams_Members() {

    } // end of default constructor

    /**
     * Overloaded default constructor to assign existing team member's email and name
     *
     * @param email existing team member email
     * @param name  existing team member name
     */
    public Ad_Hoc_Teams_Members(String email, String name) {
        super(email, name);
        this.individual_authors = new ArrayList<Individual_Authors>();
    } // end of overloaded constructor

    /**
     * Get list of individual authors involved in Ad Hoc Team
     *
     * @return list of individual authors involved in Ad Hoc Team
     */
    public List<Individual_Authors> getIndividualAuthors() {
        return this.individual_authors;
    } // end of getIndividualAuthors

    /**
     * Add individual authors to the Ad Hoc Team instance
     */
    public void addIndividualAuthors(Individual_Authors author) {
        individual_authors.add(author);
    } // end of addIndividualAuthors

}
