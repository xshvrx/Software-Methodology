package models;

import java.io.Serializable;

/*
Rutgers University CS213 Software Methodology
Photos Application Implementation
@author Zaeem Zahid
@author Shiv Patel
*/

public class Tag implements Serializable {
    public String name;
    public String value;


    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    
    /** 
     * @return String
     */
    public String getValue() {
        return value;
    }

    
    /** 
     * @param tag
     * @return boolean
     */
    // Check if the tag is equal to another tag
    public boolean equals(Tag tag) {
        return name.equals(tag.getName()) && value.equals(tag.getValue());
    }


    
    /** 
     * @return String
     */
    public String toString() {
        return "Type: " + name + ", Value: " + value;
    }

}
