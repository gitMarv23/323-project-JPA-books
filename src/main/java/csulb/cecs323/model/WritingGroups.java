package csulb.cecs323.model;

import javax.persistence.*;
import java.util.List;

/**
 * This input is used for
 * */
@NamedNativeQuery(
        name="ReturnWriting",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES " +
                "WHERE  AUTHORING_ENTITY_TYPE = 'Writing Group' and HEAD_WRITER = ?",
        resultClass = WritingGroups.class
)
@NamedNativeQuery(
        name="ReturnAllWriting",
        query = "SELECT * " +
                "FROM   AUTHORING_ENTITIES " +
                "WHERE  AUTHORING_ENTITY_TYPE = 'Writing Group'",
        resultClass = WritingGroups.class
)
@Entity
@DiscriminatorValue("Writing Group")
public class WritingGroups extends Authoring_Entities{

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "email", cascade = CascadeType.ALL)
    private List<Authoring_Entities> email;

    private String head_Writer;

    private int year_Formed;

    public int getYear_Formed() {
        return year_Formed;
    }

    public void setYear_Formed(int yearFormed) {
        this.year_Formed = yearFormed;
    }

    public List<Authoring_Entities> getEmail() {
        return email;
    }

    public void setEmail(List<Authoring_Entities> email) {
        this.email = email;
    }

    /**
     * This input is used for
     * */
    public WritingGroups() {

    }

    /**
     * This input is used for
     * */
    public WritingGroups(String email, String name, String head_Writer, int yearFormed){
        super(email,name);
        this.head_Writer = head_Writer;
        this.year_Formed = yearFormed;
    }


}
