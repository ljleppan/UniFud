package fi.loezi.unifud.model;

public class Meal {

    private String price;
    private String name;

    public Meal() {
    }

    public Meal(final String price, final String name) {

        this.price = price;
        this.name = name;
    }

    public String getPrice() {

        return price;
    }

    public void setPrice(final String price) {

        this.price = price;
    }

    public String getName() {

        return name;
    }

    public void setName(final String name) {

        this.name = name;
    }
}
