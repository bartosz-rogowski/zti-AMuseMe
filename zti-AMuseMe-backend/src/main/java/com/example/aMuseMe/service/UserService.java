package com.example.aMuseMe.service;

import com.example.aMuseMe.exception.ApiRequestException;
import com.example.aMuseMe.model.User;
import com.example.aMuseMe.repository.ForLaterListRepository;
import com.example.aMuseMe.repository.RatingRepository;
import com.example.aMuseMe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final ForLaterListRepository forLaterListRepository;
    private final PasswordEncoder passwordEncoder;


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if( userOptional.isPresent() ) {
            throw new ApiRequestException("email already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userID) {
        boolean userExists = userRepository.existsById(userID);
        if (!userExists){
            throw new ApiRequestException("There is no user with a given ID.");
        }
        forLaterListRepository.deleteByUser_id(userID);
        ratingRepository.deleteByUser_id(userID);
        userRepository.deleteById(userID);
    }

    @Transactional
    public void updateUser(
            Long userID,
            String email,
            String password,
            String nickname,
            LocalDate birthdate
    ) {
        User user = userRepository.findById(userID)
                .orElseThrow(
                        () -> new ApiRequestException("There is no user with given ID")
                );
        if (email != null
            && email.length() > 0
            && !Objects.equals(user.getEmail(), email)
        ) {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if( userOptional.isPresent() ) {
                throw new ApiRequestException("Email already taken");
            }
            user.setEmail(email);
        }

        if (password != null
                && password.length() > 0
                && !Objects.equals(user.getPassword(), password)
        ) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (nickname != null
                && nickname.length() > 0
                && !Objects.equals(user.getNickname(), nickname)
        ) {
            user.setNickname(nickname);
        }

        if (birthdate != null ) {
            if (Period.between(birthdate, LocalDate.now()).isNegative()){
                throw new ApiRequestException("Birthdate cannot be set to future date");
            }
            user.setBirthdate(birthdate);
        }
    }

    public User getUserByID(Long userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(
                        () -> new ApiRequestException("There is no user with given ID")
                );
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("There is no user with given ID");
        }
        else {
            return new org.springframework.security.core.userdetails.User(
                    userOptional.get().getEmail(),
                    userOptional.get().getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("USER"))
            );
        }
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
