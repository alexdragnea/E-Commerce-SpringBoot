package net.dg.entity;

import lombok.*;
import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter

@Entity
@Table(name = "shippingAddress")
public class ShippingAddress {

    private static final ShippingAddress EMPTY = new ShippingAddress();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false, nullable = false)
    private int shippingAddressId;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "street_number")
    private String streetNumber;

    private String city;

    @Column(name = "zip_code")
    private String zipCode;

    private String contact;

    public boolean isEmpty() {

        boolean isEmpty = false;

        if (streetName == null || streetNumber == null
                || city == null || zipCode == null ||contact == null )
            isEmpty = true;

        return isEmpty;
    }


}
