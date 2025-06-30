package com.smart.entities;

import java.util.ArrayList;

import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="USER")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(length = 100)
    @NotBlank(message="Name field is required")
    @Size(min=2, max=20, message="Name must be between 2-20 characters")
    private String name;

    @Column(unique = true)
    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 20, message = "Password must be between 6-20 characters")
    private String password;
 
    @Column(name = "compact_view", nullable = false, columnDefinition = "boolean default false")
    private Boolean compactView = false; 
    
    private String role;
    private boolean enabled;
    private String imageUrl;

    @Column(length=500)
    private String about;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
    private List<Contact> contacts = new ArrayList<>();

    public User() {
        super();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
                + ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", about=" + about + ", contacts=" + contacts
                + "]";
    }
}
