package uk.ac.plymouth.danielkern.comp2000.data;

import androidx.annotation.NonNull;

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

    @NonNull
    public String toString() {
        return "Username: " + username + "\nPassword: " + password + "\nFirst Name: " + firstname + "\nLast Name: " + lastname + "\nEmail: " + email + "\nContact: " + contact + "\nUser Type: " + usertype;
    }

    public String getUsername() {
        return username;
    }

    public UserType getUsertype() {
        return usertype;
    }

    public String getName() {
        return firstname + " " + lastname;
    }

    public String getPassword() {
        return password;
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
