package csulb.cecs323.model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@DiscriminatorValue("Individual Authors")
public class IndividualAuthors extends  AuthoringEntities{

    /*@ManyToMany(mappedBy = "individualAuthors",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE}
    )
    private List<AdHocTeams> adHocTeams;*/

    public IndividualAuthors() {

    }
    /*
    public IndividualAuthors(String name, String email){
        super(name,email);
        this.adHocTeams = new ArrayList<AdHocTeams>();
    }*/
}
