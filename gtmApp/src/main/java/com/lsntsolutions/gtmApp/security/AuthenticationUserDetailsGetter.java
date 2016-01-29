package com.lsntsolutions.gtmApp.security;

import com.lsntsolutions.gtmApp.service.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        User user = userService.getByName(username);
        throwExceptionIfNotFound(user, username);
        return new AuthenticationUserDetails(user);
    }

    private void throwExceptionIfNotFound(User user, String login) {
        if (user == null) {
            throw new UsernameNotFoundException("User with login " + login + "  has not been found.");
        }
    }
}