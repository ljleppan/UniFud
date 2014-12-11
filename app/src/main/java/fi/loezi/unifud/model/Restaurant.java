package fi.loezi.unifud.model;

import java.util.List;

public class Restaurant implements Comparable<Restaurant> {

    private int areacode;
    private int id;
    private String name;
    private List<Meal> menu;

    public Restaurant() {
    }

    public Restaurant(final int areacode,
                      final int id,
                      final String name,
                      final List<Meal> menu) {

        this.areacode = areacode;
        this.id = id;
        this.name = name;
        this.menu = menu;
    }

    public int getAreacode() {

        return areacode;
    }

    public void setAreacode(final int areacode) {

        this.areacode = areacode;
    }

    public int getId() {

        return id;
    }

    public void setId(final int id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(final String name) {

        this.name = name;
    }

    public List<Meal> getMenu() {

        return menu;
    }

    public void setMenu(final List<Meal> menu) {

        this.menu = menu;
    }

    @Override
    public int compareTo(final Restaurant another) {

        final int areaCodeDifference = this.areacode - another.areacode;

        if (areaCodeDifference != 0) {
            return areaCodeDifference;
        } else {
            return this.name.compareTo(another.name);
        }
    }
}
