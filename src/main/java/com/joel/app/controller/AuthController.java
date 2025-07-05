package com.joel.app.controller;

import com.joel.app.dto.ApiResponseDto;
import com.joel.app.exception.ResourceNotFoundException;
import com.joel.app.model.AppRole;
import com.joel.app.model.Role;
import com.joel.app.model.User;
import com.joel.app.model.UserRole;
import com.joel.app.repository.RoleRepository;
import com.joel.app.repository.UserRepository;
import com.joel.app.repository.UserRoleRepository;
import com.joel.app.security.jwt.JwtUtils;
import com.joel.app.security.request.LoginRequest;
import com.joel.app.security.request.SignupRequest;
import com.joel.app.security.response.LoginResponse;
import com.joel.app.security.response.UserInfoResponse;
import com.joel.app.security.service.UserDetailsImpl;
//import com.joel.app.service.TotpService;
import com.joel.app.service.UserService;
import com.joel.app.utils.AuthUtil;
//import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @PostMapping("/public/signin")
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", exception.getMessage());
            map.put("success", false);
            map.put("status", "401");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }

//      set the authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        // Collect roles from the UserDetails
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Prepare the response body, now including the JWT token directly in the body
        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);

        // Return the response entity with the JWT token included in the response body
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/public/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponseDto("User already registered with the given email ID!", false), HttpStatus.BAD_REQUEST);
        }

        // Create new user's account
        User user = new User(
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()
                ));
        user.setTwoFactorEnabled(false);
        user.setSignUpMethod("email");
        user.setCreatedBy(user.getFirstName());
        user = userRepository.save(user);

        Set<String> strRoles = signUpRequest.getRole();

        Role role;

        if (strRoles == null || strRoles.isEmpty()) {
            role = roleRepository.findByRoleName(AppRole.ROLE_USER.name())
                    .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(role.getRoleId());
            userRoleRepository.save(userRole);
        } else {
            for (String roleStr : strRoles) {
                role = roleRepository.findByRoleName(
                        roleStr.equalsIgnoreCase("admin") ? AppRole.ROLE_ADMIN.name() : AppRole.ROLE_USER.name()
                ).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getUserId());
                userRole.setRoleId(role.getRoleId());
                userRoleRepository.save(userRole);
            }
        }

        return ResponseEntity.ok(new ApiResponseDto("User registered successfully!", true));
    }


}
