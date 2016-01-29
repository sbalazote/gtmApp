package com.lsntsolutions.gtmApp.security;

import com.lsntsolutions.gtmApp.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public class AuthenticationUserDetailsGetter implements UserDetailsService {
    private UserService userService;

    //required by cglib because I use class based aspect weaving
    protected AuthenticationUserDetailsGetter() {
    }

    public AuthenticationUserDetailsGetter(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        com.lsntsolutions.gtmApp.model.User user = userService.getByName(username);
            System.out.println("User : "+user);
            if(user==null){
                System.out.println("User not found");
                throw new UsernameNotFoundException("Username not found");
            }
            return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
                    true, true, true, true, user.getProfile().getRoles());
    }

    private void throwExceptionIfNotFound(User user, String login) {
        if (user == null) {
            throw new UsernameNotFoundException("User with login " + login + "  has not been found.");
        }
    }

}