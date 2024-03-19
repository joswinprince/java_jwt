package com.jprince.jwt.jwt.test.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jprince.jwt.jwt.test.repository.UserRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

	private final UserRepository userRepository;
	public UserDetailsServiceImp(UserRepository userRepository) {
		this.userRepository = userRepository;
		// TODO Auto-generated constructor stub
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return userRepository.findByUserName(username)
				.orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
	}

}
