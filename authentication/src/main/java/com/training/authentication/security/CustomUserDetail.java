package com.training.authentication.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.training.authentication.entity.User;

public class CustomUserDetail implements UserDetails {

	private static final long serialVersionUID = 1L;
	private User loginUser;

	public CustomUserDetail(User user) {
		super();
		this.loginUser = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + loginUser.getRole().name()));
	}

	@Override
	public String getUsername() {
		return loginUser.getPhoneNumber().toString();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getPassword() {
		return loginUser.getPassword();
	}

}
