package csulb.cecs323.model;

import javax.persistence.*;

@Entity
@NamedNativeQueries({
    @NamedNativeQuery(
            name="ReturnAuthor",
            query = "SELECT * " +
                    "FROM   AUTHORING_ENTITIES " +
                    "WHERE  EMAIL = ? ",
            resultClass = AuthoringEntities.class
    ),
    @NamedNativeQuery(
            name = "ReturnAllAuthors",
            query = "Select *" +
                    "FROM AUTHORING_ENTITIES",
            resultClass = AuthoringEntities.class
    )
})

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "AuthoringEntityType", discriminatorType = DiscriminatorType.STRING)

public abstract class AuthoringEntities {

    @Id
    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 80 )
    private String name;

    public AuthoringEntities() {

    }

    public AuthoringEntities(String email, String name){
        this.email = email;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Authoring Entities - " + "Email: " + this.email + "   Name: " + this.name;
    }
}
