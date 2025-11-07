package uk.ac.plymouth.danielkern.comp2000.data;

public class Account {
    enum UserType {
        STAFF, GUEST, MANAGER
    }
    String username,
    password,
    firstname,
    lastname,
    email,
    contact;
    UserType type;
}
