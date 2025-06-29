package com.smart.dao;

import com.smart.entities.Contact;
import com.smart.entities.User; // <-- correct import
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    @Query("FROM Contact c WHERE c.user.id = :userId")
    Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);

    List<Contact> findByNameContainingAndUser(String name, User user);
}
