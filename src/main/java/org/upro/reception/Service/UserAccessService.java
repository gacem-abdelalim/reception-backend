package org.upro.reception.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.upro.reception.db.Entity.CustomUser;
import org.upro.reception.db.Repo.CustomUserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAccessService {

    private final CustomUserRepository userRepository;




    public String Usernamelog() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = (Jwt) auth.getPrincipal();

        return jwt.getClaimAsString("preferred_username").toUpperCase();
    }

    public List<String> Groupslog() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Jwt jwt = (Jwt) auth.getPrincipal();

        return jwt.getClaimAsStringList("groups");
    }

    public CustomUser getCurrentUser() {

        String username = Usernamelog();
        System.out.println(username);

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }



}