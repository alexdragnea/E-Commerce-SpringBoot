package net.dg.model;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter

@Entity
@Table(name="inCartProduct")
public class InCartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "needed_quantity")
    @Builder.Default
    private Integer neededQuantity = 1;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

}
