package csulb.cecs323.model;

import javax.persistence.*;
import java.util.List;

@NamedNativeQueries({
    @NamedNativeQuery(
            name="ReturnAuthor",
            query = "SELECT * " +
                    "FROM   AUTHORING_ENTITIES " +
                    "WHERE  EMAIL = ? ",
            resultClass = Authoring_Entities.class
    ),
    @NamedNativeQuery(
            name = "ReturnAllAuthors",
            query = "Select * " +
                    "FROM AUTHORING_ENTITIES",
            resultClass = Authoring_Entities.class
    )
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Authoring_Entity_Type", discriminatorType = DiscriminatorType.STRING)

public abstract class Authoring_Entities {

    @Id
    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 80 )
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "authoringName", cascade = CascadeType.PERSIST)
    private List<Books> books;

    @Transient
    public String getDiscriminatorValue(){
        DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );

        return val == null ? null : val.value();
    }


    public Authoring_Entities() {

    }

    public String getName() {
        return name;
    }

    public Authoring_Entities(String email, String name){
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Authoring Entities - " + "Email: " + this.email + "   Name: " + this.name;
    }
}
