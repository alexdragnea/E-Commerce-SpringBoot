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
public class Address {

    private static final Address EMPTY = new Address();

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
        if (streetName == null)
            return true;
        if (streetNumber == null)
            return true;
        if (city == null)
            return true;
        if (zipCode == null)
            return true;
        if (contact == null)
            return true;

        return false;
    }


}
