package de.travelbuddy.model.journey;

import de.travelbuddy.model.ContactDetails;
import de.travelbuddy.model.DuplicatePersonException;
import de.travelbuddy.model.Person;
import de.travelbuddy.model.finance.exception.DuplicateExpenseException;
import de.travelbuddy.model.finance.Expense;
import de.travelbuddy.model.finance.Money;
import de.travelbuddy.model.place.Accommodation;
import de.travelbuddy.model.place.Sight;
import de.travelbuddy.model.place.exception.DuplicatePlaceException;
import de.travelbuddy.model.place.Place;
import de.travelbuddy.model.place.exception.PlaceNotFoundException;
import de.travelbuddy.utilities.InstanceHelper;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JourneyTest {
    @Test
    public void correctly_instantiate_journey() throws DuplicatePlaceException, DuplicatePersonException {

        //Given
        String title = "Berlin";
        ContactDetails contact = InstanceHelper.createContactDetails();
        ContactDetails contact2 = InstanceHelper.createContactDetails();
        Place place = InstanceHelper.createPlace(LocalDateTime.now());
        ArrayList<Place> places = new ArrayList<>();
        places.add(place);
        Person person = InstanceHelper.createPersonMale();
        ArrayList<Person> persons = new ArrayList<>();
        persons.add(person);

        //When
        Journey journey = new Journey();
        journey.setTitle(title);
        journey.addPlaces(places);
        journey.addPersons(persons);


        //Then
        assertEquals(journey.getTitle(), title);
        assertTrue(journey.getPlaces().contains(place));
        assertTrue(journey.getPersons().contains(person));
        assertEquals(journey.getPlaces().size(), places.size());
        assertEquals(journey.getPersons().size(), persons.size());
    }

    @Test
    public void total_costs_correct_with_same_currency() throws DuplicatePlaceException, DuplicateExpenseException {
        //Given
        Journey journey = InstanceHelper.createJourney();
        Place place1 = InstanceHelper.createPlace(LocalDateTime.now());
        Place place2 = InstanceHelper.createPlace(LocalDateTime.now().plusDays(1));
        Expense expense1 = InstanceHelper.createExpense(Currency.getInstance("EUR"));
        Expense expense2 = InstanceHelper.createExpense(Currency.getInstance("EUR"));
        Expense expense3 = InstanceHelper.createExpense(Currency.getInstance("EUR"));
        Expense expense4 = InstanceHelper.createExpense(Currency.getInstance("EUR"));

        //When
        journey.addPlace(place1);
        journey.addPlace(place2);
        place1.addExpense(expense1);
        place1.addExpense(expense2);
        place2.addExpense(expense3);
        place2.addExpense(expense4);

        Money totalMoney = new Money();
        totalMoney.setCurrency(expense1.getPrice().getCurrency());
        totalMoney.setValue(expense1.getPrice().getValue());

        totalMoney.add(expense2.getPrice().getCurrency(), expense2.getPrice().getValue());
        totalMoney.add(expense3.getPrice().getCurrency(), expense3.getPrice().getValue());
        totalMoney.add(expense4.getPrice().getCurrency(), expense4.getPrice().getValue());
        Money costs = journey.totalCost(Currency.getInstance("EUR"));

        //Then
        assertEquals(totalMoney.getValue(), costs.getValue());
    }

    @Test
    public void total_costs_person_correct_with_different_currency() throws DuplicatePlaceException, DuplicateExpenseException {
        //Given
        Journey journey = InstanceHelper.createJourney();
        Place place1 = InstanceHelper.createPlace(LocalDateTime.now());
        Place place2 = InstanceHelper.createPlace(LocalDateTime.now().plusDays(1));
        Expense expense1 = InstanceHelper.createExpense(Currency.getInstance("EUR"));
        Expense expense2 = InstanceHelper.createExpense(Currency.getInstance("USD"));
        Expense expense3 = InstanceHelper.createExpense(Currency.getInstance("EUR"));
        Expense expense4 = InstanceHelper.createExpense(Currency.getInstance("USD"));

        //When
        journey.addPlace(place1);
        journey.addPlace(place2);
        place1.addExpense(expense1);
        place1.addExpense(expense2);
        place2.addExpense(expense3);
        place2.addExpense(expense4);

        Money totalMoney = new Money();
        totalMoney.setCurrency(expense1.getPrice().getCurrency());
        totalMoney.setValue(expense1.getPrice().getValue());
        totalMoney.add(expense2.getPrice());
        totalMoney.add(expense3.getPrice());
        totalMoney.add(expense4.getPrice());
        Money costs = journey.totalCost(Currency.getInstance("EUR"));

        //Then
        assertEquals(totalMoney.getValue(), costs.getValue());
    }

    @Test
    public void journey_add_place() throws DuplicatePlaceException {
        //Given
        Journey journey = InstanceHelper.createJourney();

        //When
        journey.addPlace(InstanceHelper.createPlace());

        //Then
        assertEquals(journey.getPlaces().size(), 1);
    }

    @Test
    public void journey_remove_place() throws DuplicatePlaceException, PlaceNotFoundException {
        //Given
        Journey journey = InstanceHelper.createJourney();
        Place place = InstanceHelper.createPlace(LocalDateTime.now());

        //When
        journey.addPlace(place);
        journey.removePlace(place);

        //Then
        assertEquals(journey.getPlaces().size(), 0);
    }

    @Test
    public void journey_get_place() throws DuplicatePlaceException {
        //Given
        Journey journey = InstanceHelper.createJourney();
        Place place = InstanceHelper.createPlace(LocalDateTime.now());

        //When
        journey.addPlace(place);
        List<Place> places = journey.findPlace(place.getName());

        //Then
        assertEquals(places.size(), 1);
        assertEquals(places.get(0), place);
    }

    @Test
    public void journey_remove_place_should_throw_place_not_found()  {
        //Given
        Journey journey = InstanceHelper.createJourney();
        Place place = InstanceHelper.createPlace(LocalDateTime.now());
        place.setName("Blubber");

        //When
        Exception exception = assertThrows(PlaceNotFoundException.class, () -> journey.removePlace(place));

        //Then
        assertTrue(exception.getMessage().contains("Place 'Blubber' does not exist."));
    }

    @Test
    public void journey_add_place_should_throw_duplicate_place() throws DuplicatePlaceException {
        //Given
        Journey journey = InstanceHelper.createJourney();
        Place place = InstanceHelper.createPlace();
        place.setName("Blubber");

        //When
        journey.addPlace(place);
        Exception exception = assertThrows(DuplicatePlaceException.class, () -> journey.addPlace(place));

        //Then
        assertTrue(exception.getMessage().contains("Place 'Blubber' does already exist."));
    }

    @Test
    public void journey_search_place_should_return_given_type() throws DuplicatePlaceException {
        //Given
        Journey journey = InstanceHelper.createJourney();
        Accommodation acco = InstanceHelper.createAccommodation();
        Accommodation acco2 = InstanceHelper.createAccommodation();
        Sight sight = InstanceHelper.createSight();
        Sight sight2 = InstanceHelper.createSight();
        Place place = InstanceHelper.createPlace();
        Place place2 = InstanceHelper.createPlace();

        //When
        acco.setName("Blubber");
        sight.setName("Blubber");
        place.setName("Blubber");
        journey.addPlace(acco);
        journey.addPlace(acco2);
        journey.addPlace(sight);
        journey.addPlace(sight2);
        journey.addPlace(place);
        journey.addPlace(place2);
        List<Place> listAcco = journey.findPlace(acco.getName(), Accommodation.class);
        List<Place> listSight = journey.findPlace(sight.getName(), Sight.class);
        List<Place> listPlace = journey.findPlace(sight.getName(), Place.class);

        //then
        assertEquals(1, listAcco.size());
        assertEquals(acco, listAcco.get(0));

        assertEquals(1, listSight.size());
        assertEquals(sight, listSight.get(0));

        assertEquals(1, listPlace.size());
        assertEquals(place, listPlace.get(0));
    }
}

