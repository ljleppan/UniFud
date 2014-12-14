package fi.loezi.unifud.util;

import java.util.List;

public class StringUtil {

    public static String toCommaSeparatedValues(final List<?> items) {

        if (items == null || items.isEmpty()) {
            return "";
        }

        String string = items.get(0).toString();

        for (int i = 1; i < items.size(); i++) {
            string += ", " + items.get(i).toString();
        }

        return string;
    }
}
