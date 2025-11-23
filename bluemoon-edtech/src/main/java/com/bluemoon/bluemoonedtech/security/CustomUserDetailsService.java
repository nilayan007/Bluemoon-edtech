package com.bluemoon.bluemoonedtech.security;


import com.bluemoon.bluemoonedtech.entity.User;
import com.bluemoon.bluemoonedtech.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;


    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String usernameOrPublicId) throws UsernameNotFoundException {
// We try to load by publicId first, then email as fallback
        User user = userRepository.findByPublicId(usernameOrPublicId)
                .orElseGet(() -> userRepository.findByEmail(usernameOrPublicId).orElse(null));


        if (user == null) throw new UsernameNotFoundException("User not found: " + usernameOrPublicId);
        return new CustomUserDetails(user);
    }
}