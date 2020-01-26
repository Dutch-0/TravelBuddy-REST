package de.travelbuddy.finance;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyConverterFactoryTest {

    @Test
    public void get_correct_instance(){

        //Given
        ICurrencyConverter converter;

        //When
        converter = CurrencyConverterFactory.create();

        //Then
        assertEquals(converter.getClass(), CurrencyConverter.class);
    }
}