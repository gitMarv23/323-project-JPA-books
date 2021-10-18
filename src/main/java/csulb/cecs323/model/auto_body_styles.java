package csulb.cecs323.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;

@Entity
@NamedNativeQuery(
        name="ReturnAutoBodyStyle",
        query = "SELECT * " +
                "FROM   AUTO_BODY_STYLES " +
                "WHERE  name = ? ",
        resultClass = auto_body_styles.class
)
public class auto_body_styles {
    @Id
    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 200)
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString () {
        return "auto_body_style - Name: " + this.getName() + " Description: " + this.getDescription().substring(0,20);
    }
}
