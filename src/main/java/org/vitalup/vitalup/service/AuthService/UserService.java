package org.vitalup.vitalup.service.AuthService;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.vitalup.vitalup.repository.Auth.userRepository;
import org.vitalup.vitalup.security.UsernameValidator;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final userRepository userRepo;
    private final UsernameValidator validator;

    @Override
    public UserDetails loadUserByUsername(@Nonnull String username) throws UsernameNotFoundException {

        if(validator.checkUsername(username)){
            return userRepo.findByUsernameIgnoreCase(username).orElseThrow( () -> new UsernameNotFoundException("User not found"));
        }
        throw new UsernameNotFoundException("Invalid username:" + username);
    }
}
