package com.example.snakesushi.auth;

import com.example.snakesushi.Repository.AdminRepository;
import com.example.snakesushi.model.Admin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByNick(username).orElseThrow(
                () -> new UsernameNotFoundException(username)
        );




        return new CustomUserDetails(admin);
    }
}
