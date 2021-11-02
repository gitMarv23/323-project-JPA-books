package csulb.cecs323.model;

/**
 * Imported collections to be used in the AdHocTeams Class
 */
import javax.persistence.*;
import java.util.*;

/**
 * This input is used for
 */

@Entity
@NamedNativeQuery(
        name="ReturnAdHocTeamMember",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES " +
                "WHERE AUTHORING_ENTITY_TYPE = 'AD Hoc Teams' and EMAIL = ? ",
        resultClass = Ad_Hoc_Teams_Members.class
)
@NamedNativeQuery(
        name="ReturnAllAdHocTeamMembers",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES "+
                "WHERE AUTHORING_ENTITY_TYPE = 'AD Hoc Teams' ",
        resultClass = Ad_Hoc_Teams_Members.class
)
@DiscriminatorValue("AD Hoc Teams")
public class Ad_Hoc_Teams_Members extends Authoring_Entities{
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "Ad_hoc_teams_member",
            joinColumns = @JoinColumn(name = "ad_hoc_teams_email"),
            inverseJoinColumns = @JoinColumn(name = "individual_authors_email")
    )
    private List<Individual_Authors> individual_authors;

    /**
     * This input is used for
     * */
    public Ad_Hoc_Teams_Members(){

    }

    /**
     * This input is used for
     * */
    public Ad_Hoc_Teams_Members(String email, String name){
        super(email,name);
        this.individual_authors = new ArrayList<Individual_Authors>();
    }

    public List<Individual_Authors> getIndividualAuthors(){
        return this.individual_authors;
    }

    public void addIndividualAuthors(Individual_Authors author){
        individual_authors.add(author);
    }

}
