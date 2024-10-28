package task_manager_back.task_manager_back.security;

import task_manager_back.task_manager_back.model.User;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;


import lombok.RequiredArgsConstructor;
import task_manager_back.task_manager_back.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    
    private final UserRepository repository;

    @Bean UserDetailsService userDetailsService(){
        return username->{
            final User user=repository.findByEmail(username);
            // Aquí se añade la autoridad
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER"); // O el rol correspondiente
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .authorities(Collections.singletonList(authority)) // Agregar autoridades aquí
            .build();
        };
    }



}
