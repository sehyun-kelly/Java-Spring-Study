package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // Default constructor to allow another constructor
    // make it protected instead of public
    protected Address() {}

    // Values are allowed to set only with constructors so no setters
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
