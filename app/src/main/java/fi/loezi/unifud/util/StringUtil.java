package fi.loezi.unifud.util;

import java.util.List;

import fi.loezi.unifud.model.Restaurant;

public class StringUtil {

    public static String toCommaSeparatedValues(final List<? extends Object> items) {

        if (items == null || items.isEmpty()) {
            return "";
        }

        String string = items.get(0).toString();

        for (int i = 1; i < items.size(); i++) {
            string += ", " + items.get(i).toString();
        }

        return string;
    }

    public static String parseAddressLine(final Restaurant restaurant) {

        return "";
    }
}
