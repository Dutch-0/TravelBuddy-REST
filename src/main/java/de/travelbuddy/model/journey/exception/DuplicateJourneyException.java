package de.travelbuddy.model.journey.exception;

/**
 * Should be thrown if a Journey already exists
 */
public class DuplicateJourneyException extends Exception {
    public DuplicateJourneyException(String message) {
        super(message);
    }
}
