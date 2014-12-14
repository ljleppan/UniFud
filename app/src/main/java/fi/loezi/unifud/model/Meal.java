package fi.loezi.unifud.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import fi.loezi.unifud.util.MessiApiHelper;

public class Meal implements Parcelable{

    private String price;
    private String name;
    private String ingredients;
    private String nutrition;
    private List<String> diets;
    private List<String> specialContents;
    private List<String> notes;

    public Meal() { }

    public Meal(final String price,
                final String name,
                final String ingredients,
                final String nutrition,
                final List<String> diets,
                final List<String> specialContents,
                final List<String> notes) {

        this.price = price;
        this.name = name;
        this.ingredients = ingredients;
        this.nutrition = nutrition;
        this.diets = diets;
        this.specialContents = specialContents;
        this.notes = notes;
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

    public String getIngredients() {

        return ingredients;
    }

    public void setIngredients(final String ingredients) {

        this.ingredients = ingredients;
    }

    public String getNutrition() {

        return nutrition;
    }

    public void setNutrition(final String nutrition) {

        this.nutrition = nutrition;
    }

    public List<String> getDiets() {

        return diets;
    }

    public void setDiets(final List<String> diets) {

        this.diets = diets;
    }

    public List<String> getSpecialContents() {

        return specialContents;
    }

    public void setSpecialContents(final List<String> specialContents) {

        this.specialContents = specialContents;
    }

    public List<String> getNotes() {

        return notes;
    }

    public void setNotes(final List<String> notes) {

        this.notes = notes;
    }

    @Override
    public String toString() {

        if (price.equalsIgnoreCase(MessiApiHelper.HIGH_PRICE_INDICATOR)) {
            return "[$] " + name;
        } else {
            return name;
        }
    }

    /* Parcelable */
    public Meal(final Parcel in) {

        super();

        this.price = in.readString();
        this.name = in.readString();
        this.ingredients = in.readString();
        this.nutrition = in.readString();
        in.readList(this.diets, null);
        in.readList(this.specialContents, null);
        in.readList(this.notes, null);
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {

        dest.writeString(this.price);
        dest.writeString(this.name);
        dest.writeString(this.ingredients);
        dest.writeString(this.nutrition);
        dest.writeList(this.diets);
        dest.writeList(this.specialContents);
        dest.writeList(this.notes);
    }

    public static final Parcelable.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {

        public Meal createFromParcel(final Parcel in) {

            return new Meal(in);
        }

        public Meal[] newArray(final int size) {

            return new Meal[size];
        }
    };
}
