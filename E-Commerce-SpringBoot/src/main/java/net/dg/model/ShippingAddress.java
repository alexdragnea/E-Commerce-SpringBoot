package net.dg.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Data
@Entity
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "shipping_address_id")
    private int shippingAddressId;

    @Column(name="street_name")
    private String streetName;

    @Column(name="apartment_number")
    private String apartmentNumber;

    private String city;

    @Column(name="zip_code")
    private String zipCode;

    private String contact;



}
