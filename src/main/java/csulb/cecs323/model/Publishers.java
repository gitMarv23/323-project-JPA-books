package csulb.cecs323.model;


import javax.persistence.*;
import java.util.List;

@Table(name = "Publishers", uniqueConstraints = {
        @UniqueConstraint(name = "uc_publishers_email", columnNames = {"email", "phoneNumber"})
})
@Entity
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
public class Publishers {
    @Id
    @Column(nullable = false, length = 80)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "publisherName")
    private List<Books> books;

    @Column(nullable = false, length = 80)
    private String email;

    @Column(nullable = false, length = 24)
    private String phone;

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
