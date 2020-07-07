package de.travelbuddy.model.place;
import static org.junit.jupiter.api.Assertions.*;

import de.travelbuddy.model.place.exception.InvalidLatitudeException;
import de.travelbuddy.model.place.exception.InvalidLongitudeException;
import org.junit.jupiter.api.Test;


public class CoordinatesTest {
    @Test
    public void correctly_instantiate_Coordinates() throws InvalidLatitudeException, InvalidLongitudeException {

        double latitude = 40.689249;
        double longitude = -74.044500;

        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(latitude);
        coordinates.setLongitude(longitude);

        assertEquals(coordinates.getLatitude(), latitude);
        assertEquals(coordinates.getLongitude(), longitude);
    }

    @Test
    public void instantiate_Coordinates_with_invalid_latitude_should_throw_exception() {
        //Given
        double latitude = 92.689249;

        //When
        Coordinates coordinates = new Coordinates();
        Exception exception = assertThrows(InvalidLatitudeException.class, () -> coordinates.setLatitude(latitude));

        //Then
        assertTrue(exception.getMessage().contains("Latitude should be between -90 and 90."));
    }

    @Test
    public void instantiate_Coordinates_with_invalid_longitude_should_throw_exception() {
        //Given
        double longitude = -190.044500;

        //When
        Coordinates coordinates = new Coordinates();
        Exception exception = assertThrows(InvalidLongitudeException.class, () -> coordinates.setLongitude(longitude));

        //Then
        assertTrue(exception.getMessage().contains("Longitude should be between -180 and 180."));
    }

    @Test
    public void check_latitude_and_longitude() {
        //Given
        double testLatitude = 40.689249;
        double testLongitude = -178.044500;

        //When
        boolean checkLa = Coordinates.checkLatitude(testLatitude);
        boolean checkLo = Coordinates.checkLongitude(testLongitude);

        //Then
        assertTrue(checkLa);
        assertTrue(checkLo);
    }

}



