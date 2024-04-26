package com.gogroups.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogroups.model.RefreshToken;
import com.gogroups.model.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	@Override
	Optional<RefreshToken> findById(Long id);

	Optional<RefreshToken> findByToken(String token);

	int deleteByUser(User user);

}