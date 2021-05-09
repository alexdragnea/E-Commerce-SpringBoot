package net.dg.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
//@ToString
@Getter
@Setter

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private Set<OrderedProduct> orderedProducts;

    @Column(name = "isApproved")
    @Builder.Default
    private boolean isApproved = false;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + user +
                ", orderDate=" + orderDate +
                ", orderedProducts=" +
                ", isApproved=" + isApproved +
                '}';
    }
}
