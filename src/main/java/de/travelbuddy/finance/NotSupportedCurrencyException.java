package de.travelbuddy.finance;

/**
 * Should be thrown if tried to use a Currency which is not supported/implemented by code
 */
public class NotSupportedCurrencyException extends Exception {
    public NotSupportedCurrencyException(String message) {
        super(message);
    }
}
