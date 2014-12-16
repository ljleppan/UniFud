package fi.loezi.unifud.util.hoursFormatter;

import java.util.List;

import fi.loezi.unifud.model.RegularHours;
import fi.loezi.unifud.util.StringUtil;

public class RegularHoursFormatter extends AbstractHoursFormatter{

    public static String toString(final List<RegularHours> regularHours) {

        final StringBuilder sb = new StringBuilder();
        String separator = "";

        for (RegularHours hours : regularHours) {

            sb.append(separator);

            sb.append(
                combineExistingWithSeparator(
                    StringUtil.toCommaSeparatedValues(hours.getDays()),
                    ": ",
                    combineExistingWithSeparator(
                        hours.getOpen(),
                        " - ",
                        hours.getClose()
                    )
                )
            );

            separator = "\n";
        }

        return sb.toString();
    }
}
