package net.javaguides.springbootbackend.configuration;

import net.javaguides.springbootbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
import net.javaguides.springbootbackend.configuration.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import net.javaguides.springbootbackend.model.User;

import java.util.Optional;


@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       // return new net.javaguides.springbootbackend.configuration.UserDetails(username);
        Optional<User> user = userRepository.findUserByEmail(username);
        user.orElseThrow(()-> new UsernameNotFoundException("Not found: "+username));
        return user.map(net.javaguides.springbootbackend.configuration.UserDetails::new).get();
    }
}
