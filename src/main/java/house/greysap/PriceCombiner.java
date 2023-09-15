package house.greysap;

import java.util.*;


public class PriceCombiner {

    public static Set<Price> combinePrices(Set<Price> currentPrices, Set<Price> newPrices) {
        Set<Price> stalePrices = new HashSet<>(currentPrices);
        Set<Price> freshPrices = new HashSet<>(newPrices);
        Set<Price> combinedPrices = new HashSet<>();

        for (Price freshPrice : freshPrices) {
            Set<Price> stalePricesByParams = getPricesByParams(stalePrices, freshPrice.getProductCode(), freshPrice.getNumber(), freshPrice.getDepart());
            List<Price> intersectedPrices = getIntersectedPriceIntervals(stalePricesByParams, freshPrice.getBegin(), freshPrice.getEnd());

            if (intersectedPrices.isEmpty()) {
                combinedPrices.add(freshPrice);
            }

            boolean wasPriceProlonged = false;
            for (Price intersectedPrice : intersectedPrices) {

                if (freshPrice.getValue() != intersectedPrice.getValue()) {
                    Price additionalPrice = createAdditionalPrice(intersectedPrice, freshPrice);

                    resizeOldPrice(intersectedPrice, freshPrice);

                    combinedPrices.add(intersectedPrice);

                    if (!wasPriceProlonged) {
                        combinedPrices.add(freshPrice);
                    }

                    if (additionalPrice != null) {
                        combinedPrices.add(additionalPrice);
                    }

                } else {
                    prolongOldPrice(intersectedPrice, freshPrice);
                    wasPriceProlonged = true;
                    combinedPrices.add(intersectedPrice);
                }
            }
        }

        combinedPrices.removeIf(e -> e.getBegin().getTime() >= e.getEnd().getTime());

        return combinedPrices;
    }

    private static Set<Price> getPricesByParams(Set<Price> prices, String productCode, int number, int depart) {
        Set<Price> foundPrices = new HashSet<>();

        for (Price price : prices) {
            if ( (price.getProductCode().equals(productCode)) && (price.getNumber() == number) && (price.getDepart() == depart)) {
                foundPrices.add(price);
            }
        }
        return foundPrices;
    }

    private static List<Price> getIntersectedPriceIntervals(Set<Price> prices, Date startDate, Date endDate) {
        List<Price> intersectedDateIntervals = new ArrayList<>();

        for (Price price : prices) {
            if ((startDate.getTime() <= price.getEnd().getTime()) && (price.getBegin().getTime() <= endDate.getTime())) {
                intersectedDateIntervals.add(price);
            }
        }
        intersectedDateIntervals.sort((o1, o2) -> {
            if (o1.getBegin().getTime() < o2.getBegin().getTime()) {
                return -1;
            } else if (o1.getBegin().getTime() > o2.getBegin().getTime()) {
                return 1;
            }
            return 0;
        });
        return intersectedDateIntervals;
    }

    private static Price createAdditionalPrice(Price stalePrice, Price freshPrice) {
        if ((stalePrice.getBegin().getTime() <= freshPrice.getBegin().getTime()) && (stalePrice.getEnd().getTime() >= freshPrice.getEnd().getTime())) {
            return new Price(freshPrice.getProductCode(), freshPrice.getNumber(), freshPrice.getDepart(), new Date(freshPrice.getEnd().getTime()), stalePrice.getEnd(), stalePrice.getValue());
        }
        return null;
    }

    private static void resizeOldPrice(Price stalePrice, Price freshPrice) {
        if (stalePrice.getBegin().getTime() <= freshPrice.getBegin().getTime()) {
            stalePrice.setEnd(new Date(freshPrice.getBegin().getTime()));
        } else {
            stalePrice.setBegin(new Date(freshPrice.getEnd().getTime()));
        }
    }

    private static void prolongOldPrice(Price stalePrice, Price freshPrice) {
        if (stalePrice.getBegin().getTime() <= freshPrice.getBegin().getTime()) {
            stalePrice.setEnd(freshPrice.getEnd());
        } else {
            stalePrice.setBegin(freshPrice.getBegin());
        }
    }
}
