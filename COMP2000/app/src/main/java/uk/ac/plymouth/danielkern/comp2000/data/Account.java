package uk.ac.plymouth.danielkern.comp2000.data;

public class Account {
    public Account(String username, String password, String firstname, String lastname, String email, String contact) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.usertype = UserType.GUEST;
    }
    public Account(String username, String password, String firstname, String lastname, String email, String contact, UserType type) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.usertype = type;
    }

    public String getUsername() {
        return username;
    }

    public UserType getUsertype() {
        return usertype;
    }

    public enum UserType {
        STAFF, GUEST, MANAGER
    }
    String username,
    password,
    firstname,
    lastname,
    email,
    contact;
    UserType usertype;
}
