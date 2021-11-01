package csulb.cecs323.model;

import javax.persistence.*;
import java.util.*;

@NamedNativeQuery(
        name="ReturnAuthor",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES " +
                "WHERE  NAME = ? ",
        resultClass = IndividualAuthors.class
)
@DiscriminatorValue("Individual Authors")
public class IndividualAuthors extends Authoring_Entities{

    /*support hour fix
    @ManyToMany(mappedBy = "individualAuthors",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE}
    )*/


    private List<AdHocTeams> adHocTeams;

    public IndividualAuthors() {

    }

    public IndividualAuthors(String email, String name){
        super(email,name);
        this.adHocTeams = new ArrayList<AdHocTeams>();
    }
}
