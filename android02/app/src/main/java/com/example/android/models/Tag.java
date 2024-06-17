package com.example.android.models;

import java.io.Serializable;

public class Tag implements Serializable {
    public String name;
    public String value;


    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(Tag tag) {
        return name.equals(tag.getName()) && value.equals(tag.getValue());
    }

    public String toString() {
        return "Type: " + name + ", Value: " + value;
    }

}
