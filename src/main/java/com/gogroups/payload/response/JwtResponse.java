package com.gogroups.payload.response;

import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtResponse {

	private final String acessToken;
	private String type = "Bearer";
	private final String refreshToken;
	private final Long id;
	private final String name;
	private final String username;
	private final String email;
	private final List<String> roles;

}
