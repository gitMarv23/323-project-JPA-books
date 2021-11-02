package csulb.cecs323.model;


import javax.persistence.*;
import java.util.List;


/**
 * This input is used for
 * */
@Table(name = "Publishers", uniqueConstraints = {
        @UniqueConstraint(name = "uc_publishers_email", columnNames = {"email", "phone"})
})
@NamedNativeQuery(
        name="ReturnPublisher",
        query = "SELECT * " +
                "FROM   PUBLISHERS " +
                "WHERE  PUBLISHER_NAME = ? ",
        resultClass = Publishers.class
)
@NamedNativeQuery(
        name="ReturnAllPublisher",
        query = "SELECT * " +
                "FROM   PUBLISHERS ",
        resultClass = Publishers.class
)
@Entity
public class Publishers {
    @Id
    @Column(nullable = false, name = "publisher_name", length = 80)
    private String name;

    @Column(nullable = false, name = "phone", length = 24)
    private String phone;

    @Column(nullable = false, name = "email", length = 80)
    private String email;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // one to many reevaluation since relation scheme changed with publisher_name pk to foreign_key in books
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisherName", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Books> books;

    /**
     * This input is used for
     * */
    public Publishers(String name, String email, String phone){
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    /**
     * This input is used for
     * */
    public Publishers(){

    }

    @Override
    public String toString(){
        return "Publishers - Name: " + this.name + "   Email: " + this.email +
                "   Phone: " + this.phone;
    }
}
