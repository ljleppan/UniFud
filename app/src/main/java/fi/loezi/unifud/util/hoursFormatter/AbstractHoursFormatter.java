package fi.loezi.unifud.util.hoursFormatter;

public abstract class AbstractHoursFormatter {

    protected static boolean hasContent(final String string) {

        if (string == null) {
            return false;
        }

        final String trimmed = string.trim();

        if (trimmed.isEmpty()) {
            return false;
        }

        if (trimmed.equals("null")) {
            return false;
        }

        return true;
    }

    protected static String combineExistingWithSeparator(final String prefix,
                                                         final String separator,
                                                         final String suffix) {

        String result = "";

        if (hasContent(prefix)) {
            result = prefix;

            if (hasContent(suffix)) {
                result += separator + suffix;
            }
        } else if (hasContent(suffix)) {
            result = suffix;
        }

        return result;
    }
}
