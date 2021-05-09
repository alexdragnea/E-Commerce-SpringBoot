package net.dg.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter

@Entity
@Table(name="carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private Long id;


    @Transient
    private Map<Product, Integer> productsWithQuantity = new HashMap<>();

    @JoinColumn(name = "order_id")
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Order> orders;



}
