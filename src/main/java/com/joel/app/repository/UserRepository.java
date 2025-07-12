package com.joel.app.repository;

import com.joel.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    @Query("select r.roleName from Role r " +
            "left join UserRole ur on ur.roleId = r.roleId " +
            "left join User u on u.userId = ur.userId " +
            "where u.userId = :userId")
    List<String> getRoles(Long userId);

    Optional<User> findByEmail(String email);
}