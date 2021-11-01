package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("AD Hoc Teams")
public class AdHocTeams extends Authoring_Entities{
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "Ad_hoc_teams_member",
            joinColumns = @JoinColumn(name = "ad_hoc_teams_email"),
            inverseJoinColumns = @JoinColumn(name = "individual_authors_email")
    )
    private List<Individual_Authors> individual_authors;

    public AdHocTeams(){

    }

    public AdHocTeams(String name,String email){
        super(name,email);
        this.individual_authors = new ArrayList<Individual_Authors>();
    }

    public List<Individual_Authors> getIndividualAuthors(){
        return this.individual_authors;
    }

}
