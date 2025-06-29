// Package declaration - organizes related classes
package com.smart.dao;

// Import necessary Spring Data JPA classes
import com.smart.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 * UserRepository interface for database operations on User entities.
 * Extends JpaRepository to inherit common CRUD operations.
 * 
 * <p>This interface serves as:
 * - A Spring Data JPA repository
 * - Auto-implemented by Spring at runtime
 * - Provides database operations without implementation code
 */
public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);
    // This interface inherits all standard CRUD operations:
    // save(), findById(), findAll(), deleteById(), etc.


	@Query("select u from User u where u.email = :email")
	public User getUserByUserName(@Param("email") String email);



	

	
    
    // No additional methods are declared here yet, but custom queries can be added as needed
    // Example custom method that could be added:
    // User findByEmail(String email);
	
}