package csulb.cecs323.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

@Entity
@NamedNativeQuery(
        name = "ReturnAllAuthors",
        query = "Select *" +
                "FROM AUTHORING_ENTITIES",
        resultClass = AuthoringEntities.class
)
public class AuthoringEntities {

    @Id
    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = true, length = 31 )
    private String entityType;

    @Column(nullable = false, length = 80 )
    private String name;

    @Column(nullable = true, length = 80 )
    private String headWriter;

    @Column(nullable = true )
    private int yearFormed;

    public AuthoringEntities(String email, String entityType, String name, String headWriter, int yearFormed){
        this.email = email;
        this.entityType = entityType;
        this.name = name;
        this.headWriter = headWriter;
        this.yearFormed = yearFormed;

    }

    public AuthoringEntities(){

    }

    @Override
    public String toString() {
        return "Authoring Entities - " + "Email: " + this.email + "   Author Type: " + this.entityType
                + "   Name: " + this.name + "   Head Writer: " + this.headWriter + "   Year Formed: " + this.yearFormed;
    }
}
