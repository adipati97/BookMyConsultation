/*
 * Copyright 2018-2019, https://beingtechie.io.
 *
 * File: UserAuthTokenVerifier.java
 * Date: May 5, 2018
 * Author: Thribhuvan Krishnamurthy
 */
package com.upgrad.bookmyconsultation.service;

import com.upgrad.bookmyconsultation.enums.UserAuthTokenStatus;
import com.upgrad.bookmyconsultation.entity.UserAuthToken;
import com.upgrad.bookmyconsultation.util.DateTimeProvider;

import java.time.ZonedDateTime;

/**
 * Verifies the authentication token and capture the status.
 */
public final class UserAuthTokenVerifier {

	private final UserAuthTokenStatus status;

	public UserAuthTokenVerifier(final UserAuthToken userAuthToken) {

		//Made changes to this logic to ensure that only one entry per user is stored in the user_auth_token table.
		if (isActive(userAuthToken)) {
			status = UserAuthTokenStatus.ACTIVE;
		} else if (isExpired(userAuthToken)) {
			status = UserAuthTokenStatus.EXPIRED;
		} else if (isLoggedOut(userAuthToken)) {
			status = UserAuthTokenStatus.LOGGED_OUT;
		} else {
			status = UserAuthTokenStatus.NOT_FOUND;
		}
	}

	public boolean isActive() {
		return UserAuthTokenStatus.ACTIVE == status;
	}

	public boolean hasExpired() {
		return UserAuthTokenStatus.EXPIRED == status;
	}

	public boolean hasLoggedOut() {
		return UserAuthTokenStatus.LOGGED_OUT == status;
	}

	public boolean isNotFound() {
		return UserAuthTokenStatus.NOT_FOUND == status;
	}

	public UserAuthTokenStatus getStatus() {
		return status;
	}

	private boolean isExpired(final UserAuthToken userAuthToken) {
		final ZonedDateTime now = DateTimeProvider.currentProgramTime();
		return userAuthToken != null &&
			!(now.isBefore(userAuthToken.getExpiresAt()) || now.isEqual(userAuthToken.getExpiresAt()));
	}

	private boolean isLoggedOut(final UserAuthToken userAuthToken) {
		return userAuthToken != null && userAuthToken.getLogoutAt() != null;
	}

	private boolean isActive (final UserAuthToken userAuthToken) {
		return userAuthToken != null && !isExpired(userAuthToken);
	}

}