package auth.service;

import java.util.List;

import auth.entity.UserApp;
import auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("userDetailsServiceImpl")   // It has to be annotated with @Service.
public class UserDetailsServiceImpl implements UserDetailsService  {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");

        UserApp userApp = userRepository.findUserAppByUsername(username);
        if (userApp == null) {
            // If user not found. Throw this exception.
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }

        return new User(userApp.getUsername(), userApp.getPassword(), grantedAuthorities);

    }
}