package house.greysap;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


public class PriceCombinerTest {
    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";

    @Test
    public void shouldCombinePricesOfTwoProducts() throws ParseException {
        Set<Price> currentPrices = new HashSet<>();
        currentPrices.add(new Price("122856", 1, 1, DateUtils.parseDate("01.01.2013 00:00:00", DATE_FORMAT), DateUtils.parseDate("31.01.2013 23:59:59", DATE_FORMAT), 11000));
        currentPrices.add(new Price("122856", 2, 1, DateUtils.parseDate("10.01.2013 00:00:00", DATE_FORMAT), DateUtils.parseDate("20.01.2013 23:59:59", DATE_FORMAT), 99000));
        currentPrices.add(new Price("6654", 1, 2, DateUtils.parseDate("01.01.2013 00:00:00", DATE_FORMAT), DateUtils.parseDate("31.01.2013 00:00:00", DATE_FORMAT), 5000));

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(new Price("122856", 1, 1, DateUtils.parseDate("20.01.2013 00:00:00", DATE_FORMAT), DateUtils.parseDate("20.02.2013 23:59:59", DATE_FORMAT), 11000));
        newPrices.add(new Price("122856", 2, 1, DateUtils.parseDate("15.01.2013 00:00:00", DATE_FORMAT), DateUtils.parseDate("25.01.2013 23:59:59", DATE_FORMAT), 92000));
        newPrices.add(new Price("6654", 1, 2, DateUtils.parseDate("12.01.2013 00:00:00", DATE_FORMAT), DateUtils.parseDate("13.01.2013 00:00:00", DATE_FORMAT), 4000));

        Set<Price> expectedCombinedPrices = new HashSet<>();
        expectedCombinedPrices.add(new Price("122856", 1, 1, DateUtils.parseDate("01.01.2013 00:00:00", DATE_FORMAT), DateUtils.parseDate("20.02.2013 23:59:59", DATE_FORMAT), 11000));
        expectedCombinedPrices.add(new Price("122856", 2, 1, DateUtils.parseDate("10.01.2013 00:00:00", DATE_FORMAT), DateUtils.parseDate("15.01.2013 00:00:00", DATE_FORMAT), 99000));
        expectedCombinedPrices.add(new Price("122856", 2, 1, DateUtils.parseDate("15.01.2013 00:00:00", DATE_FORMAT), DateUtils.parseDate("25.01.2013 23:59:59", DATE_FORMAT), 92000));
        expectedCombinedPrices.add(new Price("6654", 1, 2, DateUtils.parseDate("01.01.2013 00:00:00", DATE_FORMAT), DateUtils.parseDate("12.01.2013 00:00:00", DATE_FORMAT), 5000));
        expectedCombinedPrices.add(new Price("6654", 1, 2, DateUtils.parseDate("12.01.2013 00:00:00", DATE_FORMAT), DateUtils.parseDate("13.01.2013 00:00:00", DATE_FORMAT), 4000));
        expectedCombinedPrices.add(new Price("6654", 1, 2, DateUtils.parseDate("13.01.2013 00:00:00", DATE_FORMAT), DateUtils.parseDate("31.01.2013 00:00:00", DATE_FORMAT), 5000));

        Set<Price> actualCombinedPrices = PriceCombiner.combinePrices(currentPrices, newPrices);
        assertEquals(expectedCombinedPrices, actualCombinedPrices);
    }

    @Test
    public void shouldInsertNewPriceInOldPriceInterval() throws ParseException {

        Set<Price> currentPrices = new HashSet<>();
        currentPrices.add(new Price("42", 3, 3, DateUtils.parseDate("01.01.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("31.01.2020 23:59:59", DATE_FORMAT), 50));

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(new Price("42", 3, 3, DateUtils.parseDate("13.01.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("27.01.2020 00:00:00", DATE_FORMAT), 60));

        Set<Price> expectedCombinedPrices = new HashSet<>();
        expectedCombinedPrices.add(new Price("42", 3, 3, DateUtils.parseDate("01.01.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("13.01.2020 00:00:00", DATE_FORMAT), 50));
        expectedCombinedPrices.add(new Price("42", 3, 3, DateUtils.parseDate("13.01.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("27.01.2020 00:00:00", DATE_FORMAT), 60));
        expectedCombinedPrices.add(new Price("42", 3, 3, DateUtils.parseDate("27.01.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("31.01.2020 23:59:59", DATE_FORMAT), 50));

        Set<Price> actualCombinedPrices = PriceCombiner.combinePrices(currentPrices, newPrices);
        assertEquals(expectedCombinedPrices, actualCombinedPrices);
    }

    @Test
    public void shouldApplyNewPriceOnTheBorderBetweenTwoOldPrices() throws ParseException {

        Set<Price> currentPrices = new HashSet<>();
        currentPrices.add(new Price("43", 4, 4, DateUtils.parseDate("01.02.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("23.02.2020 23:59:59", DATE_FORMAT), 100));
        currentPrices.add(new Price("43", 4, 4, DateUtils.parseDate("24.02.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("29.02.2020 23:59:59", DATE_FORMAT), 120));

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(new Price("43", 4, 4, DateUtils.parseDate("20.02.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("27.02.2020 00:00:00", DATE_FORMAT), 110));

        Set<Price> expectedCombinedPrices = new HashSet<>();
        expectedCombinedPrices.add(new Price("43", 4, 4, DateUtils.parseDate("01.02.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("20.02.2020 00:00:00", DATE_FORMAT), 100));
        expectedCombinedPrices.add(new Price("43", 4, 4, DateUtils.parseDate("20.02.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("27.02.2020 00:00:00", DATE_FORMAT), 110));
        expectedCombinedPrices.add(new Price("43", 4, 4, DateUtils.parseDate("27.02.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("29.02.2020 23:59:59", DATE_FORMAT), 120));

        Set<Price> actualCombinedPrices = PriceCombiner.combinePrices(currentPrices, newPrices);
        assertEquals(expectedCombinedPrices, actualCombinedPrices);
    }

    @Test
    public void shouldOverrideOldPriceWithTwoNewPrices() throws ParseException {

        Set<Price> currentPrices = new HashSet<>();
        currentPrices.add(new Price("44", 5, 5, DateUtils.parseDate("01.03.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("08.03.2020 23:59:59", DATE_FORMAT), 80));
        currentPrices.add(new Price("44", 5, 5, DateUtils.parseDate("09.03.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("19.03.2020 23:59:59", DATE_FORMAT), 87));
        currentPrices.add(new Price("44", 5, 5, DateUtils.parseDate("20.03.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("31.03.2020 23:59:59", DATE_FORMAT), 90));

        Set<Price> newPrices = new HashSet<>();
        newPrices.add(new Price("44", 5, 5, DateUtils.parseDate("05.03.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("15.03.2020 00:00:00", DATE_FORMAT), 80));
        newPrices.add(new Price("44", 5, 5, DateUtils.parseDate("15.03.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("26.03.2020 00:00:00", DATE_FORMAT), 85));

        Set<Price> expectedCombinedPrices = new HashSet<>();
        expectedCombinedPrices.add(new Price("44", 5, 5, DateUtils.parseDate("01.03.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("15.03.2020 00:00:00", DATE_FORMAT), 80));
        expectedCombinedPrices.add(new Price("44", 5, 5, DateUtils.parseDate("15.03.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("26.03.2020 00:00:00", DATE_FORMAT), 85));
        expectedCombinedPrices.add(new Price("44", 5, 5, DateUtils.parseDate("26.03.2020 00:00:00", DATE_FORMAT), DateUtils.parseDate("31.03.2020 23:59:59", DATE_FORMAT), 90));

        Set<Price> actualCombinedPrices = PriceCombiner.combinePrices(currentPrices, newPrices);
        assertEquals(expectedCombinedPrices, actualCombinedPrices);
    }
}
