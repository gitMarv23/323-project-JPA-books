package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This input is used for
 * */
@Entity
@NamedNativeQuery(
        name="ReturnIndividualAuthor",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES " +
                "WHERE AUTHORING_ENTITY_TYPE = 'Individual Authors' and EMAIL = ? ",
        resultClass = Authoring_Entities.class
)
@NamedNativeQuery(
        name="ReturnAllIndividualAuthors",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES "+
                "WHERE AUTHORING_ENTITY_TYPE = 'Individual Authors'",
        resultClass = Authoring_Entities.class
)
@DiscriminatorValue("Individual Authors")
public class Individual_Authors extends  Authoring_Entities{

    // support hour fix
    @ManyToMany(mappedBy = "individual_authors",
            cascade = CascadeType.ALL)
    private List<Ad_Hoc_Teams_Members> adHocTeamMembers;

    /**
     * This input is used for
     * */
    public Individual_Authors() {

    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * This input is used for
     * */
    public Individual_Authors(String email, String name){
        super(email,name);
        this.adHocTeamMembers = new ArrayList<Ad_Hoc_Teams_Members>();
    }
}
