package com.upgrad.bookmyconsultation.util;

import java.time.ZonedDateTime;

/**
 * This class is used to get the present system time and set the expiry of the auth token accordingly. This can be used
 * to store the data related to an auth token instead of repeating logic when refreshing a user's token.
 */
public class TokenDateTimeProvider {
    private ZonedDateTime currentDateTime;
    private ZonedDateTime expiresAt;

    public ZonedDateTime getCurrentDateTime () {
        return currentDateTime;
    }

    public void setCurrentDateTime (ZonedDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public ZonedDateTime getExpiresAt () {
        return expiresAt;
    }

    public void setExpiresAt (ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public TokenDateTimeProvider () {
        setCurrentDateTime(DateTimeProvider.currentProgramTime());
        setExpiresAt(currentDateTime.plusMinutes(1));
    }
}
