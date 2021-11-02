package csulb.cecs323.model;

import javax.persistence.*;
import java.util.List;

/**
 * This input is used for
 * */
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
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames =
        {"title", "publisher_name"}),
        @UniqueConstraint( columnNames =
                {"title", "authoring_entity_name"})})
public class Books {

    @Id
    @Column(nullable = false, name = "isbn", length = 17)
    private String ISBN;

    @Column(nullable = false, name = "title", length = 80)
    private String title;

    @Column(nullable = false, name = "year_published")
    private int yearPublished;
    
    public String getISBN() {
        return ISBN;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "Authoring_Entity_Name", nullable = false, referencedColumnName = "email")
    private Authoring_Entities authoringName;

    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "publisher_name", nullable = false, referencedColumnName = "publisher_name")
    private Publishers publisherName;

    /**
     * This input is used for
     * */
    public Books(){

    }

    public Authoring_Entities getAuthoringName() {
        return authoringName;
    }

    public void setAuthoringName(Authoring_Entities authoringName) {
        this.authoringName = authoringName;
    }

    /**
     * This input is used for
     * */
    public Books(String ISBN, String title, int yearPublished,Publishers publisherName ,  Authoring_Entities authoringName){
        this.ISBN = ISBN;
        this.title = title;
        this.yearPublished = yearPublished;
        this.publisherName = publisherName;
        this.authoringName = authoringName;

    }

    @Override
    public String toString() {
        return "Books - Name: " + ISBN + "   Title: " + title +
                "   Year Published: " + yearPublished +
                "   Authoring Email: " + authoringName.getEmail() +
                "   Publisher Name: " + publisherName.getName();
    }

}
