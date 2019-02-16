package com.example.tgiapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tgiapi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public Optional<User> findByUserEmail(String email);
}
