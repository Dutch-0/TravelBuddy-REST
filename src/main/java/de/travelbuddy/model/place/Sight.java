package de.travelbuddy.model.place;

import de.travelbuddy.model.ContactDetails;
import de.travelbuddy.model.Person;
import de.travelbuddy.model.finance.Expense;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Sights is a extension from place
 */

@Entity
@Table(name = "SIGHT")
@Getter
@Setter
public class Sight extends Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean indoor;

    // Required for JPA
    public Sight() {};
}
