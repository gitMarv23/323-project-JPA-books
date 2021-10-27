package csulb.cecs323.model;

import javax.persistence.*;

@Entity
public class Books {

    @Id
    @Column(nullable = false, length = 17)
    private String ISBN;

    @Column(nullable = false, length = 80)
    private String title;

    @Column(nullable = false)
    private int yearPublished;

    @Column(nullable = false, length = 30)
    private String authoringName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name", nullable = false, referencedColumnName = "name")
    private Publishers publisherName;

}
