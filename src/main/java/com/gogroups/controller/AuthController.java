package com.gogroups.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gogroups.exception.TokenRefreshException;
import com.gogroups.model.ERole;
import com.gogroups.model.RefreshToken;
import com.gogroups.model.Role;
import com.gogroups.model.User;
import com.gogroups.payload.request.LoginRequest;
import com.gogroups.payload.request.SignupRequest;
import com.gogroups.payload.request.TokenRefreshRequest;
import com.gogroups.payload.response.JwtResponse;
import com.gogroups.payload.response.ResponseMessage;
import com.gogroups.payload.response.TokenRefreshResponse;
import com.gogroups.repository.RoleJpaRepo;
import com.gogroups.repository.UserJpaRepo;
import com.gogroups.security.UserDetailsImpl;
import com.gogroups.security.jwt.JwtUtils;
import com.gogroups.security.service.RefreshTokenService;
import io.swagger.annotations.*;

@RestController
@RequestMapping("/api/auth")
@Api(tags = "Authentication Controller")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserJpaRepo userRepo;

	@Autowired
	RoleJpaRepo roleRepo;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	RefreshTokenService refreshTokenService;

	@ApiOperation(value = "This method is used to sign in to the app")
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
			throws AccountNotFoundException {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		String jwt = jwtUtils.generateJwtToken(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUserId());

		return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getUserId(),
				userDetails.getName(), userDetails.getUsername(), userDetails.getEmail(), roles));
	}

	@ApiOperation(value = "For the refresh token")
	@PostMapping("/refreshtoken")
	public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();

		return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser).map(user -> {
					String token = jwtUtils.generateTokenFromUsername(user.getUsername());
					return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
				})
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
	}

	@ApiOperation(value = "This method is used to sign up to the service.")
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupReq) {
		if (userRepo.existsByUsername(signupReq.getUsername())) {
			return ResponseEntity.badRequest().body(new ResponseMessage("Error: Username is not available!"));
		}

		if (userRepo.existsByEmail(signupReq.getEmail())) {
			return ResponseEntity.badRequest().body(new ResponseMessage("Error: Email is already registered!"));
		}

		/*
		 * Create new user's account
		 */
		User user = new User(signupReq.getName(), signupReq.getUsername(), signupReq.getEmail(),
				encoder.encode(signupReq.getPassword()), null);

		String strRoles = signupReq.getRoles().toLowerCase();
		Set<Role> roles = new HashSet<>();

		if (strRoles.isEmpty()) {
			Role userRole = roleRepo.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: The role USER is not found."));
			roles.add(userRole);
		} else {
			switch (strRoles) {
			case "admin":
				Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Error: The role ADMIN is not found."));
				roles.add(adminRole);
				break;
			default:
				Role userRole = roleRepo.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
			}
		}
		user.setRoles(roles);
		userRepo.save(user);

		return ResponseEntity.ok(new ResponseMessage("User registered successfully!"));
	}
}