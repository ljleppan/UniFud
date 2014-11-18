package fi.loezi.unifud.Service;

import java.util.HashMap;
import java.util.Map;

public class RestaurantCampusService {

    private final static Map<String, String> restaurantCampus;

    static {
        restaurantCampus = new HashMap<String, String>();

        restaurantCampus.put("Metsätalo", "keskusta");
        restaurantCampus.put("Olivia", "keskusta");
        restaurantCampus.put("Porthania", "keskusta");
        restaurantCampus.put("Rotunda", "keskusta");
        restaurantCampus.put("Soc&Kom", "keskusta");
        restaurantCampus.put("Topelias", "keskusta");
        restaurantCampus.put("Valtiotiede", "keskusta");
        restaurantCampus.put("Ylioppilasaukio", "keskusta");
        restaurantCampus.put("Cafe Portaali", "keskusta");

        restaurantCampus.put("Chemicum", "kumpula");
        restaurantCampus.put("Exactum", "kumpula");
        restaurantCampus.put("Physicum", "kumpula");

        restaurantCampus.put("Meilahti", "meilahti");

        restaurantCampus.put("Ruskeasuo", "ruskeasuo");

        restaurantCampus.put("Biokeskus", "viikki");
        restaurantCampus.put("Korona", "viikki");
        restaurantCampus.put("Viikuna", "viikki");

        restaurantCampus.put("Agricolankatu Ricola", "metropolia");
        restaurantCampus.put("Albertinkatu", "metropolia");
        restaurantCampus.put("Bulevardi", "metropolia");
        restaurantCampus.put("Hämeentie", "metropolia");
        restaurantCampus.put("Leiritie", "metropolia");
        restaurantCampus.put("Onnentie", "metropolia");
        restaurantCampus.put("Sofianlehto", "metropolia");
        restaurantCampus.put("Tukholmankatu", "metropolia");
        restaurantCampus.put("Vanha Maantie", "metropolia");
        restaurantCampus.put("Vanha Viertotie", "metropolia");

        restaurantCampus.put("Ravintola Serpens", "yths");
    }

    public static Map<String, String> getRestaurantCampusMap() {

        return restaurantCampus;
    }
}
