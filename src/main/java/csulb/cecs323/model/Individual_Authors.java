package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Individual Authors")
public class Individual_Authors extends  Authoring_Entities{

    // support hour fix
    @ManyToMany(mappedBy = "individual_authors",
            cascade = CascadeType.ALL)
    private List<AdHocTeams> adHocTeams;

    public Individual_Authors() {

    }

    public Individual_Authors(String email, String name){
        super(email,name);
        this.adHocTeams = new ArrayList<AdHocTeams>();
    }
}
