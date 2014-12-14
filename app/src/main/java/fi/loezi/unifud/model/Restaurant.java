package fi.loezi.unifud.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Restaurant implements Comparable<Restaurant>, Parcelable {

    private int areaCode;
    private int id;
    private String name;
    private List<Menu> menus;

    public Restaurant() { }

    public Restaurant(final int areaCode,
                      final int id,
                      final String name,
                      final List<Menu> menus) {

        this.areaCode = areaCode;
        this.id = id;
        this.name = name;
        this.menus = menus;
    }

    public int getAreaCode() {

        return areaCode;
    }

    public void setAreaCode(final int areaCode) {

        this.areaCode = areaCode;
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

    public List<Menu> getMenus() {

        return menus;
    }

    public void setMenus(final List<Menu> menus) {

        this.menus = menus;
    }

    @Override
    public int compareTo(final Restaurant another) {

        final int areaCodeDifference = this.areaCode - another.areaCode;

        if (areaCodeDifference != 0) {
            return areaCodeDifference;
        } else {
            return this.name.compareTo(another.name);
        }
    }

    @Override
    public String toString() {

        return name;
    }

    /* Parcelable */
    public Restaurant(final Parcel in) {

        super();

        this.areaCode = in.readInt();
        this.id = in.readInt();
        this.name = in.readString();
        in.readList(this.menus, null);
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {

        dest.writeInt(this.areaCode);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeList(this.menus);
    }

    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {

        public Restaurant createFromParcel(final Parcel in) {

            return new Restaurant(in);
        }

        public Restaurant[] newArray(final int size) {

            return new Restaurant[size];
        }
    };
}
