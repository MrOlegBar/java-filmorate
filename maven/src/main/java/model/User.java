package model;

import java.util.Date;

@lombok.Data
public class User {
    private final int id;
    private final String email;
    private final String login;
    private final String name;
    private final Date birthday;
}
