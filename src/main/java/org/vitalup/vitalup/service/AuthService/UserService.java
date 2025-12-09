package org.vitalup.vitalup.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.repository.Auth.userRepository;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final userRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Remember-> Add validation

        return userRepo.findByUsernameIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
