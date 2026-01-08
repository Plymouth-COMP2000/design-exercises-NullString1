package uk.ac.plymouth.danielkern.comp2000.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Account implements Parcelable {
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setUsertype(UserType usertype) {
        this.usertype = usertype;
    }

    public Account() {
    }


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

    protected Account(Parcel in) {
        username = in.readString();
        password = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        email = in.readString();
        contact = in.readString();
        usertype = UserType.values()[in.readInt()];
    }

    public static final Creator<Account> CREATOR = new Creator<>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    @NonNull
    public String toString() {
        return "Username: " + username + "\nPassword: " + password + "\nFirst Name: " + firstname + "\nLast Name: " + lastname + "\nEmail: " + email + "\nContact: " + contact + "\nUser Type: " + usertype;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(firstname);
        parcel.writeString(lastname);
        parcel.writeString(email);
        parcel.writeString(contact);
        parcel.writeInt(usertype.ordinal());
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
