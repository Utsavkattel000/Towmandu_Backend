package com.tow.mandu.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.io.Serial;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	@Serial
	private static final long serialVersionUID = -114874802667864120L;
	private final String username;

	public JwtAuthenticationToken(String username) {
		super(null);
		this.username = username;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

}
