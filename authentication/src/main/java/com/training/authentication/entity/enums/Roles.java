package com.training.authentication.entity.enums;

public enum Roles {
	ADMIN(0), USER(1);

	private final int val;

	Roles(int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}

}
