package com.smart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*; // Importing JPA annotations from Jakarta Persistence API

/**
 * Represents a Contact entity that will be mapped to a database table.
 * This class defines the structure of contacts stored in the system.
 */
@Entity // Marks this class as a JPA entity (will be mapped to a database table)
@Table(name = "CONTACT") // Explicitly names the database table as "CONTACT"
public class Contact {

    // Primary key field
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures auto-increment for the ID
    private int cId; // Unique identifier for each contact

    // Basic contact information fields
    private String name; // Contact's first name
    private String secondName; // Contact's last name/second name
    private String work; // Contact's job/profession
    private String email; // Contact's email address
    private String phone; // Contact's phone number
    private String image; // Path/reference to contact's profile image

    // Long description field with extended length
    @Column(length = 50000) // Specifies a large column size for lengthy descriptions
    private String description; // Detailed information about the contact
    
    // Relationship field (many contacts can belong to one user)
    @ManyToOne // Establishes many-to-one relationship with User entity
    @JsonIgnore
    private User user; // Reference to the User who owns this contact
      
    @Override
    public String toString() {
        return "Contact [cId=" + cId + ", name=" + name + ", secondName=" + secondName
                + ", work=" + work + ", email=" + email + ", phone=" + phone 
                + ", image=" + image + ", description=" + description 
                + ", userId=" + (user != null ? user.getId() : "null") + "]";
    }

	// Default constructor (required by JPA)
    public Contact() {
        super(); // Calls the parent class constructor (Object in this case)
    }
    
    // Getter for the associated User
    public User getUser() {
        return user; // Returns the User object associated with this contact
    }

    // Setter for the associated User
    public void setUser(User user) {
        this.user = user; // Sets the User object for this contact
    }

    // Below are standard getters and setters for all fields
    // Each follows the JavaBean convention for property access

    public int getcId() {
        return cId; // Returns the contact ID
    }

    public void setcId(int cId) {
        this.cId = cId; // Sets the contact ID
    }

    public String getName() {
        return name; // Returns the contact's name
    }

    public void setName(String name) {
        this.name = name; // Sets the contact's name
    }

    public String getSecondName() {
        return secondName; // Returns the contact's second name
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName; // Sets the contact's second name
    }

    public String getWork() {
        return work; // Returns the contact's work information
    }

    public void setWork(String work) {
        this.work = work; // Sets the contact's work information
    }

    public String getEmail() {
        return email; // Returns the contact's email
    }

    public void setEmail(String email) {
        this.email = email; // Sets the contact's email
    }

    public String getPhone() {
        return phone; // Returns the contact's phone number
    }

    public void setPhone(String phone) {
        this.phone = phone; // Sets the contact's phone number
    }

    public String getImage() {
        return image; // Returns the contact's image reference
    }

    public void setImage(String image) {
        this.image = image; // Sets the contact's image reference
    }

    public String getDescription() {
        return description; // Returns the contact's description
    }

    public void setDescription(String description) {
        this.description = description; // Sets the contact's description
    }
}