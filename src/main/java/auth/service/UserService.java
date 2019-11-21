package auth.service;

import auth.entity.UserApp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import auth.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserApp getUserByUsername(String username){
        return userRepository.findUserAppByUsername(username);
    }

    public UserApp createUser(UserApp userApp){
        userApp.setPassword(bCryptPasswordEncoder.encode(userApp.getPassword()));
        return userRepository.save(userApp);
    }

    public void deleteUser(UserApp userApp){
        userRepository.delete(userApp);
    }
}
