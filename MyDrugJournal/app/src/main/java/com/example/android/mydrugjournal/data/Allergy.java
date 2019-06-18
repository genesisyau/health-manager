package com.example.android.mydrugjournal.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Allergy {
    private String id;
    private String allergen;
    private String description;

    public Allergy(String id, String allergen, String description){
        this.id = id;
        this.allergen = allergen;
        this.description = description;
    }
}
