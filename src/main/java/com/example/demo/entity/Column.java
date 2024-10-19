package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Column {

    private String name;
    private String type;

    public Column(String name, String type) {
        this.name = name;
        setType(type);
    }

    public void setType(String type) {
        if (!isValidType(type)) {
            throw new IllegalArgumentException("Invalid column type: " + type +
                    ". Allowed types are 'integer' or 'String'.");
        }
        this.type = type;
    }

    private boolean isValidType(String type) {
        return "integer".equalsIgnoreCase(type) || "String".equalsIgnoreCase(type);
    }


}
