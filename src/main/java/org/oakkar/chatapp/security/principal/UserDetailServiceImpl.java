package org.oakkar.chatapp.security.principal;

import lombok.RequiredArgsConstructor;
import org.oakkar.chatapp.model.entity.User;
import org.oakkar.chatapp.model.exception.ResourceNotFoundException;
import org.oakkar.chatapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User not found with email : '%s'.", email)));
        return UserPrincipal.build(user);
    }
}
