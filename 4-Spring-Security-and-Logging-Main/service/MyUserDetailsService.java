package com.example.DeviPrasadSecurityCorporatePortal.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final InMemoryUserDetailsManager manager;

    public MyUserDetailsService(InMemoryUserDetailsManager manager) {
        this.manager = manager;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return manager.loadUserByUsername(username);
    }
}
