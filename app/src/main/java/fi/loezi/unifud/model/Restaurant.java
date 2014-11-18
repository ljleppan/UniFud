package fi.loezi.unifud.model;

import java.util.List;

public class Restaurant {

    private String name;
    private List<String> meals;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<String> getMeals() {
        return meals;
    }

    public void setMeals(final List<String> meals) {
        this.meals = meals;
    }
}
