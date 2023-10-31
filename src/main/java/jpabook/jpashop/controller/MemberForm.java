package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {


    @NotEmpty(message = "Name must not be empty")
    private String name;
    private String city;
    private String street;
    private String zipcode;
}

