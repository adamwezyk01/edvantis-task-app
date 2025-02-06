package com.example.edvantistask.controller;

import com.example.edvantistask.dto.RegistrationRequest;
import com.example.edvantistask.model.UserAccount;
import com.example.edvantistask.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody RegistrationRequest request) {
        if(userAccountRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        UserAccount user = UserAccount.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role("ROLE_USER")
                .build();
        userAccountRepository.save(user);
    }
}
