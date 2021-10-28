package csulb.cecs323.model;

import javax.persistence.*;
import java.util.List;

@Entity
@NamedNativeQuery(
        name="ReturnBook",
        query = "SELECT * " +
                "FROM   BOOKS " +
                "WHERE  ISBN = ? ",
        resultClass = Books.class
)
@NamedNativeQuery(
        name="ReturnAllBooks",
        query = "SELECT * " +
                "FROM   BOOKS ",
        resultClass = Books.class
)
public class Books {

    @Id
    @Column(nullable = false, length = 17)
    private String ISBN;

    @Column(nullable = false, length = 80)
    private String title;

    @Column(nullable = false)
    private int yearPublished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AuthoringEntityType", nullable = false, referencedColumnName = "AuthoringEntityType")
    private AuthoringEntities authoringName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name", nullable = false, referencedColumnName = "name")
    private Publishers publisherName;

    public Books(){

    }
    public Books(String ISBN, String title, int yearPublished,Publishers publisherName ,  AuthoringEntities authoringName){
        this.ISBN = ISBN;
        this.title = title;
        this.yearPublished = yearPublished;
        this.publisherName = publisherName;
        this.authoringName = authoringName;

    }


}
