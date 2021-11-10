package com.upgrad.bookmyconsultation.util;

import java.time.ZonedDateTime;

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
