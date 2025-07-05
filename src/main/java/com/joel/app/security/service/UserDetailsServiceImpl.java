package com.joel.app.security.service;

import com.joel.app.model.Role;
import com.joel.app.model.User;
import com.joel.app.model.UserRole;
import com.joel.app.repository.RoleRepository;
import com.joel.app.repository.UserRepository;
import com.joel.app.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.webauthn.management.JdbcUserCredentialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        List<UserRole> userRoles = userRoleRepository.findByUserId(user.getUserId());
        List<Integer> roleIds = userRoles.stream()
                .map(u -> u.getRoleId())
                .collect(Collectors.toList());

        List<Role> roles = roleRepository.findAllById(roleIds);

        return UserDetailsImpl.build(user,roles);
    }




}