package csulb.cecs323.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@DiscriminatorValue("AD Hoc Teams")
public class AdHocTeams extends AuthoringEntities{
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "ad_hoc_teams_member",
            joinColumns = @JoinColumn(name = "ad_hoc_teams_email"),
            inverseJoinColumns = @JoinColumn(name = "individual_authors_email")
    )
    private List<IndividualAuthors> individualAuthors;

    public AdHocTeams(){

    }

    public AdHocTeams(String name,String email){
        super(name,email);
        this.individualAuthors = new ArrayList<IndividualAuthors>();
    }

    public List<IndividualAuthors> getIndividualAuthors(){
        return this.individualAuthors;
    }

}
