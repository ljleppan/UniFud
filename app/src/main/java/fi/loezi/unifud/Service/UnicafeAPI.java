package fi.loezi.unifud.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fi.loezi.unifud.model.Restaurant;

public class UnicafeAPI {

    private final static String BASE_URL = "http://www.hyyravintolat.fi/lounastyokalu/index.php?option=com_ruokalista&Itemid=29&task=lounaslista_tarkkahaku&lang=1&r[1]=1&r[2]=1&r[3]=1&r[4]=1&r[5]=1&r[15]=1&r[6]=1&r[7]=1&r[8]=1&r[10]=1&r[11]=1&r[12]=1&r[13]=1&r[14]=1&r[18]=1&r[19]=1&r[21]=1&r[27]=1&r[29]=1&r[28]=1&r[33]=1&r[36]=1&r[30]=1&r[34]=1&r[31]=1&r[35]=1&r[32]=1&r[42]=1&r[43]=1&v[";
    private final static String URL_SUFFIX = "]=1";

    public List<Restaurant> get() throws IOException {

        final List<Restaurant> restaurants = new ArrayList<Restaurant>();

        final int day = getDayIdentifier();
        final String url = BASE_URL + day + URL_SUFFIX;

        final Document document = Jsoup.connect(url).get();
        final Elements tdElements = document.select("tbody tr td");
        final Elements thElements = document.select("thead tr th");

        for (int i = 0; i < tdElements.size(); i++) {

            final Restaurant restaurant = new Restaurant();

            //Parse restaurant name
            String name = thElements.get(i).child(0).text();

            //Stupid fallback for Ravintola Serpens
            if (name == null || name.isEmpty()) {
                name = thElements.get(i).text();
                if(name.contains("\"")) {
                    name.replace("\"", "");
                }
            }

            restaurant.setName(name);

            //Parse meals
            final Element tdElement = tdElements.get(i);

            final List<String> meals = new ArrayList<String>();
            for (Element pElement : tdElement.children()) {
                String mealName = pElement.child(0).text();

                if(pElement.text().contains("Maukkaasti")) {
                    mealName = "[M] " + mealName;
                }

                meals.add(mealName);
            }

            if (meals.isEmpty()) {
                meals.add("-");
            }

            restaurant.setMeals(meals);
            restaurants.add(restaurant);
        }

        return restaurants;
    }

    /*
     * Probably needlessly complicated but should work no matter how the phone starts counting weekdays
     */
    public int getDayIdentifier() {

        final Calendar now = Calendar.getInstance();
        final int day = now.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                return 1;
            case Calendar.TUESDAY:
                return 2;
            case Calendar.WEDNESDAY:
                return 3;
            case Calendar.THURSDAY:
                return 4;
            case Calendar.FRIDAY:
                return 5;
            case Calendar.SATURDAY:
                return 6;
            case Calendar.SUNDAY:
                return 7;
            default:
                return 0;
        }
    }
}
