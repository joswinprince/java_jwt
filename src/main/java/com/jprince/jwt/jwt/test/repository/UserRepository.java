package com.jprince.jwt.jwt.test.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jprince.jwt.jwt.test.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	Optional<User> findByUserName(String username);

}
