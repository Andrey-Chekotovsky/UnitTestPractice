package org.example;

public class User {
    private int id;
    private String firstName;
    private  String LastName;
    private String username;
    private String password;
    private Role role;
    public User() {}
    public User(String firstName, String lastName, String username, String password, Role role) {
        this.firstName = firstName;
        LastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public String roleToString()
    {
        return switch (role) {
            case Admin -> "admin";
            case Customer -> "customer";
        };
    }
    public void setRoleWithString(String str)
    {
        switch (str) {
            case "admin" -> role = Role.Customer;
            case "customer" -> role = Role.Admin;
            default -> role = Role.Customer;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj.getClass() != User.class)
            return false;
        if (obj == this)
            return true;
        User user = (User)obj;
        if(this.username.equals(user.username))
            return true;
        else return false;
    }
}
enum Role
{
    Customer,
    Admin

}