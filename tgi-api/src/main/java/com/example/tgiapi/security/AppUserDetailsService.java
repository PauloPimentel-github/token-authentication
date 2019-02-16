package com.example.tgiapi.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.tgiapi.repository.UserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		Optional<com.example.tgiapi.model.User> userOptional = this.userRepository.findByUserEmail(userEmail);
		com.example.tgiapi.model.User user = userOptional.orElseThrow(()-> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
		return new User(userEmail, user.getUserPassword(), this.getUserPermissions(user));
	}

	private Collection<? extends GrantedAuthority> getUserPermissions(com.example.tgiapi.model.User user) {

		Set<GrantedAuthority> authorities = new HashSet<>();
		user.getUserPermissions().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getPermissionDescription().toUpperCase())));
		return authorities;
	}
}
