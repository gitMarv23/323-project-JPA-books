package csulb.cecs323.model;

import javax.persistence.*;
import java.util.List;

@DiscriminatorValue("Writing Group")
public class WritingGroups extends AuthoringEntities{

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "email")
    private List<AuthoringEntities> email;

    private String headWriter;

    private int yearFormed;

    public WritingGroups(){

    }

    public WritingGroups(String name,String email, String headWriter, int yearFormed){
        super(name,email);
        this.headWriter = headWriter;
        this.yearFormed = yearFormed;
    }


}
