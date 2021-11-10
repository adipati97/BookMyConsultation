/*
 * Copyright 2018-2019, https://beingtechie.io.
 *
 * File: AuthTokenServiceImpl.java
 * Date: May 5, 2018
 * Author: Thribhuvan Krishnamurthy
 */
package com.upgrad.bookmyconsultation.service;

import com.upgrad.bookmyconsultation.entity.User;
import com.upgrad.bookmyconsultation.entity.UserAuthToken;
import com.upgrad.bookmyconsultation.exception.AuthorizationFailedException;
import com.upgrad.bookmyconsultation.exception.UserErrorCode;
import com.upgrad.bookmyconsultation.provider.token.JwtTokenProvider;
import com.upgrad.bookmyconsultation.repository.UserAuthTokenRepository;
import com.upgrad.bookmyconsultation.repository.UserRepository;
import com.upgrad.bookmyconsultation.util.DateTimeProvider;
import com.upgrad.bookmyconsultation.util.TokenDateTimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;


@Service
public class AuthTokenService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserAuthTokenRepository userAuthDao;

	@Transactional(propagation = Propagation.MANDATORY)
	public UserAuthToken issueToken(final User user) {

		final TokenDateTimeProvider tokenDateTimeProvider = new TokenDateTimeProvider();

		final UserAuthToken userAuthToken = userAuthDao.findByUserEmailId(user.getEmailId());
		final UserAuthTokenVerifier tokenVerifier = new UserAuthTokenVerifier(userAuthToken);
		if (tokenVerifier.isActive()) {
			return userAuthToken;
		}
		//Refresh the token of the user instead of creating a new entry in the user_auth_token table due to token expiry
		if (tokenVerifier.hasExpired()) {
			refreshToken(userAuthToken);
			userAuthDao.save(userAuthToken);
			return userAuthToken;
		}

		final String authToken = getAuthToken(user, tokenDateTimeProvider);
		System.out.println(authToken);
		final UserAuthToken authTokenEntity = new UserAuthToken();
		authTokenEntity.setUser(user);
		authTokenEntity.setAccessToken(authToken);
		authTokenEntity.setLoginAt(tokenDateTimeProvider.getCurrentDateTime());
		authTokenEntity.setExpiresAt(tokenDateTimeProvider.getExpiresAt());
		userAuthDao.save(authTokenEntity);

		return authTokenEntity;

	}

	/**
	 * Since the auth token is stored in a table with an expiry time, this function is used to refresh the token if it
	 * has expired. This prevents multiple entries for the same user due to token expiry and allows for a seamless
	 * user experience.
	 * @param token {@link UserAuthToken}
	 */
	private void refreshToken (UserAuthToken token) {
		final TokenDateTimeProvider tokenDateTimeProvider = new TokenDateTimeProvider();
		User user = userRepository.findByEmailId(token.getUser().getEmailId());

		final String authToken = getAuthToken(user, tokenDateTimeProvider);
		token.setAccessToken(authToken);
		token.setExpiresAt(tokenDateTimeProvider.getExpiresAt());
	}

	/**
	 * Generates a new auth token for the given user.
	 * @param user The user that is either logging in or logging out.
	 * @param provider {@link TokenDateTimeProvider}
	 * @return The generated auth token as a string.
	 */
	private String getAuthToken (User user, TokenDateTimeProvider provider) {
		return new JwtTokenProvider(user.getPassword()).generateToken(
			user.getEmailId(),
			provider.getCurrentDateTime(),
			provider.getExpiresAt()
		);
	}


	@Transactional(propagation = Propagation.REQUIRED)
	public void invalidateToken(final String accessToken) throws AuthorizationFailedException {

		final UserAuthToken userAuthToken = userAuthDao.findByAccessToken(accessToken);
		final UserAuthTokenVerifier tokenVerifier = new UserAuthTokenVerifier(userAuthToken);
		if (tokenVerifier.isNotFound()) {
			throw new AuthorizationFailedException(UserErrorCode.USR_005);
		}
		//Refresh the token of the user instead of creating a new entry in the user_auth_token table due to token expiry
		if (tokenVerifier.hasExpired()) {
			refreshToken(userAuthToken);
		}

		userAuthToken.setLogoutAt(DateTimeProvider.currentProgramTime());
		userAuthDao.save(userAuthToken);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UserAuthToken validateToken(@NotNull String accessToken) throws AuthorizationFailedException {
		final UserAuthToken userAuthToken = userAuthDao.findByAccessToken(accessToken);
		final UserAuthTokenVerifier tokenVerifier = new UserAuthTokenVerifier(userAuthToken);
		//The order of execution of the if conditions has been modified to avoid incorrect status.
		//Ex. A token's expiry status takes priority over the logout out status.
		if (tokenVerifier.isActive()) {
			return userAuthToken;
		} else if (tokenVerifier.hasExpired()) {
			throw new AuthorizationFailedException(UserErrorCode.USR_006);
		} else if (tokenVerifier.hasLoggedOut()) {
			throw new AuthorizationFailedException(UserErrorCode.USR_013);
		} else {
			throw new AuthorizationFailedException(UserErrorCode.USR_005);
		}
	}

}