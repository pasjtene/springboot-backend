package net.javaguides.springbootbackend.services;

import net.javaguides.springbootbackend.model.User;
import net.javaguides.springbootbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean userExist(User user) {

        Optional<User> userExist = this.userRepository.findUserByEmail(user.getEmail());

        return userExist.isPresent();
    }

    public User getUserByUser(User user) {

        Optional<User> userExist = this.userRepository.findUserByEmail(user.getEmail());

        if(userExist.isPresent()) {

            User newUser = this.userRepository.findById(user.getId()).orElseThrow(
                    () -> new IllegalStateException("Student with id Does not exist")
            );
            return newUser;
        }

        return user;
    }


    public User getUserByID(Long userId) {

        //Optional<User> userExist = this.userRepository.findUserByEmail(user.getEmail());

            User theuser = this.userRepository.findById(userId).orElseThrow(
                    () -> new IllegalStateException("Student with id Does not exist")
            );

        return theuser;
    }



}
