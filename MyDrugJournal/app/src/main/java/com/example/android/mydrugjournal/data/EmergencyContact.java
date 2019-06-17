package com.example.android.mydrugjournal.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyContact {
    private String id;
    private String name;
    private String address;
    private String phoneNumber;

    public EmergencyContact(String id, String name, String address, String phoneNumber){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
