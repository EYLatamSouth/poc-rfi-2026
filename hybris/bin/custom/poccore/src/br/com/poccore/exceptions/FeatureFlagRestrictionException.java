package br.com.poccore.exceptions;

public class FeatureFlagRestrictionException extends RuntimeException {

    public FeatureFlagRestrictionException() {
    }

    public FeatureFlagRestrictionException(String message) {
        super(message);
    }

    public FeatureFlagRestrictionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeatureFlagRestrictionException(Throwable cause) {
        super(cause);
    }

    public FeatureFlagRestrictionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
