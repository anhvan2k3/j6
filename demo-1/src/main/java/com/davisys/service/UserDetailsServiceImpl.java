package com.davisys.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.davisys.dao.UserDAO;
import com.davisys.entity.Users;
import com.davisys.reponsitory.CustomUserDetails;
import com.davisys.reponsitory.UsersReponsitory;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersReponsitory usersReponsitory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDAO dao;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = dao.findEmailUser(email);
        // Users user =usersReponsitory.findByEmail(email).orElseThrow();
        if (user == null) {
            System.out.println("User not found with username: " + email);
            throw new UsernameNotFoundException("User not found with username: " + email);
        }

        System.out.println("find: " + email + " " + user.getUsername() + " " + user.getPassword() + "\n\n\n");
        System.out.println("qưerty");
        /*
         * custom.setUsername(user.getUsername());
         * custom.setPassword(user.getPassword());
         */
        /*
         * String[] roles = user.getAuthorities().toArray(new String[0]);
         * for (String r : roles) {
         * System.out.println(r);
         * }
         */
        // System.out.println("Auths: "+Arrays.toString(user.getAuth()));

        try {
            String[] roles = user.getRoles().stream().map(au -> au.getRole_id())
                    .collect(Collectors.toList()).toArray(new String[0]);
            return org.springframework.security.core.userdetails.User.withUsername(email)
                    .password(user.getPassword()).roles(roles).build();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        System.out.println(user.getPassword() + user.getUsername() + Arrays.toString(user.getAuth()));
        return User.withUsername(user.getUsername()).password(user.getPassword()).roles(user.getAuth()).build();
    }
}
