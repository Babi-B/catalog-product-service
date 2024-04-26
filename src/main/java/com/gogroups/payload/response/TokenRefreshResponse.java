package com.gogroups.payload.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TokenRefreshResponse {

	private final String accessToken;
	private final String refreshToken;
	private String tokenType = "Bearer";

}