package csulb.cecs323.model;


import javax.persistence.*;
import java.util.List;

@Table(name = "Publishers", uniqueConstraints = {
        @UniqueConstraint(name = "uc_publishers_email", columnNames = {"email", "phoneNumber"})
})
@NamedNativeQuery(
        name="ReturnPublisher",
        query = "SELECT * " +
                "FROM   PUBLISHERS " +
                "WHERE  name = ? ",
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

    // one to many reevaluation since relation scheme changed with publisher_name pk to foreign_key in books
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisherName")
    private List<Books> books;

    public Publishers(String name, String email, String phone){
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Publishers(){

    }

    @Override
    public String toString(){
        return "Publishers - Name: " + this.name + "   Email: " + this.email +
                "   Phone: " + this.phone;
    }
}
