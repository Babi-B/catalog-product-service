package com.gogroups.payload.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignupRequest {

	private final String name;

	private final String username;

	private final String email;

	private final String password;

	private String roles;
}
