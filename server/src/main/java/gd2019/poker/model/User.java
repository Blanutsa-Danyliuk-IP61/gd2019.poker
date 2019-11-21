package gd2019.poker.model;

import lombok.Data;

import java.util.UUID;

@Data
public class User {

    private UUID id;
    private String name;

    public User(String name) {
        this.name = name;
    }

}
