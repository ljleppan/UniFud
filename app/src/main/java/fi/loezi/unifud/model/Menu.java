package fi.loezi.unifud.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Menu implements Parcelable {

    private String date;
    private List<Meal> meals;

    public Menu() {

        super();
    }

    public Menu(final List<Meal> meals, final String date) {

        this.meals = meals;
        this.date = date;
    }

    public String getDate() {

        return date;
    }

    public void setDate(final String date) {

        this.date = date;
    }

    public List<Meal> getMeals() {

        return meals;
    }

    public void setMeals(final List<Meal> meals) {

        this.meals = meals;
    }

     /* Parcelable */
    public Menu(final Parcel in) {

        super();

        this.date = in.readString();
        in.readList(meals, null);
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {

        dest.writeString(this.date);
        dest.writeList(this.meals);
    }

    public static final Parcelable.Creator<Menu> CREATOR = new Parcelable.Creator<Menu>() {

        public Menu createFromParcel(final Parcel in) {

            return new Menu(in);
        }

        public Menu[] newArray(final int size) {

            return new Menu[size];
        }
    };
}
