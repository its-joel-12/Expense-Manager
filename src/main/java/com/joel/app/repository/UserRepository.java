package com.joel.app.repository;

import com.joel.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

//    @Query("select r.role_name from role r " +
//            "left join users u on u.user_id = r.role_id " +
//            "where r.role_id = :roleId")
//    Optional<String> getRoleName(Integer roleId);

    Optional<User> findByEmail(String email);
}