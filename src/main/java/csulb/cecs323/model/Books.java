package csulb.cecs323.model;

/**
 * Libraries required for class execution
 */
import javax.persistence.*;
import java.util.List;

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
/**
 * The Books class will represent our Book objects to be manipulated within the database.
 * Initially, the Native Queries ensure the data is reflected to the user prior to their
 * requested adjustments.
 */
public class Books {

    /**
     * ISBN for book object instance. Usually a number within 17 digits for reference
     */
    @Id
    @Column(nullable = false, name = "isbn", length = 17)
    private String ISBN;

    /**
     * Title for the current book object instance
     */
    @Column(nullable = false, name = "title", length = 80)
    private String title;

    /**
     * Year of publication for current Book object instance
     */
    @Column(nullable = false, name = "year_published")
    private int yearPublished;

    /**
     * Getter function to obtain the Book's given ISBN
     * @return  book ISBN
     */
    public String getISBN() {
        return ISBN;
    } // end of getISBN

    /**
     * Name of the authoring entity who created the Book
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "Authoring_Entity_Name", nullable = false, referencedColumnName = "email")
    private Authoring_Entities authoringName;

    /**
     * Name of the current Book's publisher
     */
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "publisher_name", nullable = false, referencedColumnName = "publisher_name")
    private Publishers publisherName;

    /**
     * Default constructor for current Books class
     * */
    public Books(){
    } // end of Books default constructor

    /**
     * Get the authoring entity for the current Book object instance
     * @return  book authoring entity name
     */
    public Authoring_Entities getAuthoringName() {
        return authoringName;
    } // end getAuthoringName()

    /**
     * Set name of Book's authoring entity
     * @param authoringName name of authoring entity
     */
    public void setAuthoringName(Authoring_Entities authoringName) {
        this.authoringName = authoringName;
    } // end setAuthoringName

    /**
     * Book instance overloaded constructor based on user input from Library main.
     * @param ISBN          unique number given to book based on international system
     * @param title         title of book
     * @param yearPublished year of publication for given book instance
     * @param publisherName name of publisher for given book instance
     * @param authoringName name of authoring entity for given book instance
     */
    public Books(String ISBN, String title, int yearPublished,Publishers publisherName ,  Authoring_Entities authoringName){
        this.ISBN = ISBN;
        this.title = title;
        this.yearPublished = yearPublished;
        this.publisherName = publisherName;
        this.authoringName = authoringName;
    } // end of Books (overloaded)

    /**
     * OverloadedToString function to display all information from given Book instance
     */
    @Override
    public String toString() {
        return "Books - ISBN: " + ISBN + "   Title: " + title +
                "   Year Published: " + yearPublished +
                "   Authoring Email: " + authoringName.getEmail() +
                "   Publisher Name: " + publisherName.getName();
    } // end of ToString
}
