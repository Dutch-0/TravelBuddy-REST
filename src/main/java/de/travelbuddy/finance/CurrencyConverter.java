package de.travelbuddy.finance;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;


public class CurrencyConverter implements ICurrencyConverter {

    enum ConversionRate
    {
        // nach ISO 4217
        EUR (BigDecimal.valueOf(1)),//Euro
        CHF (BigDecimal.valueOf(1.07)),//Schweizer Franken
        CNY (BigDecimal.valueOf(7.82)),//Yuan
        CZK (BigDecimal.valueOf(25.15)),//Tschechische Krone
        PLN (BigDecimal.valueOf(4.24)),//Polnischer Zloty
        RUB (BigDecimal.valueOf(70.00)),//Rubel
        USD (BigDecimal.valueOf(1.10));//US Dollar
        
    
        private final BigDecimal conversionRate;

        ConversionRate(BigDecimal conversionRate) { this.conversionRate = conversionRate; }

        public BigDecimal getConversionRate() { return conversionRate; }
    }

    public BigDecimal getRate(Currency currencySource, Currency currencyTarget) throws NotSupportedCurrencyException {
        if (!isSupportedCurrency(currencyTarget))
            throw new NotSupportedCurrencyException(
                    String.format("The currency '%s' is not supported by this CurrencyConverter-Class",
                            currencyTarget.getCurrencyCode()));

        if (!isSupportedCurrency(currencySource))
            throw new NotSupportedCurrencyException(
                    String.format("The currency '%s' is not supported by this CurrencyConverter-Class",
                            currencySource.getCurrencyCode()));

        BigDecimal toEUR = ConversionRate.valueOf(currencySource.getCurrencyCode()).getConversionRate();
        BigDecimal toResultCurrency = ConversionRate.valueOf(currencyTarget.getCurrencyCode()).getConversionRate();

        toEUR = toEUR.multiply(BigDecimal.valueOf(100));
        toResultCurrency = toResultCurrency.multiply(BigDecimal.valueOf(100));

        return toResultCurrency.setScale(10,RoundingMode.HALF_UP).divide(toEUR,RoundingMode.HALF_UP);
    }

    public Money convert(Money money, Currency currencyTarget) throws NotSupportedCurrencyException {
        if (!isSupportedCurrency(currencyTarget))
            throw new NotSupportedCurrencyException(
                    String.format("The currency '%s' is not supported by this CurrencyConverter-Class",
                        currencyTarget.getCurrencyCode()));

        BigDecimal rate = getRate(money.getCurrency(),currencyTarget);

        return new Money(currencyTarget,money.getValue().multiply(rate).setScale(2,RoundingMode.HALF_UP));
    }

    public boolean isSupportedCurrency(Currency currency) {
        for (ConversionRate c : ConversionRate.values()) {
            if (c.name().equals(currency.getCurrencyCode())) {
                return true;
            }
        }
        return false;
    }
}
