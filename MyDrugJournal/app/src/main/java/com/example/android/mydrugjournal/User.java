package com.example.android.mydrugjournal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private String firstName;
    private String lastName;
    private String birthDate;
    private Double height;
    private Double weight;
    private String country;
    private String bloodType;
    private String sex;
}
