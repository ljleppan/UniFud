package fi.loezi.unifud.util.hoursFormatter;

import java.util.List;

import fi.loezi.unifud.model.ExceptionalHours;

public class ExceptionalHoursFormatter extends AbstractHoursFormatter {

    public static String toString(final List<ExceptionalHours> exceptionalHours ) {

        final StringBuilder sb = new StringBuilder();
        String separator = "";

        for (ExceptionalHours hours : exceptionalHours) {

            sb.append(separator);

            String suffix;
            if (hours.isClosed()) {
                suffix = "closed";
            } else {
                suffix = combineExistingWithSeparator(
                    hours.getOpen(),
                    " - ",
                    hours.getClose()
                );
            }

            sb.append(
                combineExistingWithSeparator(
                    combineExistingWithSeparator(
                        hours.getFrom(),
                        " - ",
                        hours.getTo()
                    ),
                    ": ",
                    suffix
                )
            );

            separator = "\n";
        }

        return sb.toString();
    }
}
