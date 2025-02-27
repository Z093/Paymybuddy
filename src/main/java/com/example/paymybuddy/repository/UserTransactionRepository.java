package com.example.paymybuddy.repository;

import com.example.paymybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserTransactionRepository extends JpaRepository<User, Long> {
    //User findByMail(String mail);
    Optional<User> findByMail(String mail);

}
