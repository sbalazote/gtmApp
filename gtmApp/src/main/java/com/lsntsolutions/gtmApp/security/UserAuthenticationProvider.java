package com.lsntsolutions.gtmApp.security;

import com.lsntsolutions.gtmApp.helper.EncryptionHelper;
import com.lsntsolutions.gtmApp.model.Role;
import com.lsntsolutions.gtmApp.model.User;
import com.lsntsolutions.gtmApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Repository("userAuthenticationProvider")
public class UserAuthenticationProvider implements AuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);

	@Autowired
	private UserService userService;

	@Override
	public Authentication authenticate(Authentication authentication) {
		if (!authentication.isAuthenticated()) {
			logger.info("Intentando autenticar usuario {}...", authentication.getPrincipal().toString());
			String username = authentication.getPrincipal().toString();
			String password = authentication.getCredentials().toString();
			User user = this.userService.getByName(username);
			if (user != null && password != null) {
				String hashedPassword = EncryptionHelper.generateHash(username + password);

				if (hashedPassword.equals(user.getPassword())) {
					if (user.isActive()) {
						logger.info("Usuario autenticado");
                        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                        HttpSession session = attr.getRequest().getSession(true); // true == allow create
                        session.setAttribute("userName", username);
                        session.setAttribute("Locale", new Locale("ES"));
						return new UsernamePasswordAuthenticationToken(username, password, this.getAuthorities(user.getProfile().getRoles()));
					} else {
						logger.info("Usuario inhabilitado.");
						throw new BadCredentialsException("Usuario inhabilitado. Consulte con el Administrador del Sistema.");
					}
				} else {
					logger.info("Usuario o contraseña incorrectos.");
					throw new BadCredentialsException("Usuario o contrasenia incorrectos");
				}
			} else {
				logger.info("Usuario o contraseña incorrectos.");
				throw new BadCredentialsException("Usuario o contrasenia incorrectos");
			}
		}
		return authentication;
	}

	public Collection<GrantedAuthority> getAuthorities(List<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Role role : roles) {
			authorities.add(role);
		}
		return authorities;
	}

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
