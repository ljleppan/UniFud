package fi.loezi.unifud.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Restaurant implements Comparable<Restaurant>, Parcelable {

    private int areaCode;
    private int id;
    private String name;
    private List<Menu> menus;
    private String address;
    private List<RegularHours> businessRegular;
    private List<ExceptionalHours> businessException;
    private List<RegularHours> lunchRegular;
    private List<ExceptionalHours> lunchException;
    private List<RegularHours> bistroRegular;
    private List<ExceptionalHours> bistroException;

    public Restaurant() { }

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

    public String getAddress() {

        return address;
    }

    public void setAddress(final String address) {

        this.address = address;
    }

    public List<RegularHours> getBusinessRegular() {

        return businessRegular;
    }

    public void setBusinessRegular(final List<RegularHours> businessRegular) {

        this.businessRegular = businessRegular;
    }

    public List<ExceptionalHours> getBusinessException() {

        return businessException;
    }

    public void setBusinessException(final List<ExceptionalHours> businessException) {

        this.businessException = businessException;
    }

    public List<RegularHours> getLunchRegular() {

        return lunchRegular;
    }

    public void setLunchRegular(final List<RegularHours> lunchRegular) {

        this.lunchRegular = lunchRegular;
    }

    public List<ExceptionalHours> getLunchException() {

        return lunchException;
    }

    public void setLunchException(final List<ExceptionalHours> lunchException) {

        this.lunchException = lunchException;
    }

    public List<RegularHours> getBistroRegular() {

        return bistroRegular;
    }

    public void setBistroRegular(final List<RegularHours> bistroRegular) {

        this.bistroRegular = bistroRegular;
    }

    public List<ExceptionalHours> getBistroException() {

        return bistroException;
    }

    public void setBistroException(final List<ExceptionalHours> bistroException) {

        this.bistroException = bistroException;
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
        this.address = in.readString();
        in.readList(this.businessRegular, null);
        in.readList(this.businessException, null);
        in.readList(this.lunchRegular, null);
        in.readList(this.lunchException, null);
        in.readList(this.bistroRegular, null);
        in.readList(this.bistroException, null);
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
        dest.writeString(this.address);
        dest.writeList(this.businessRegular);
        dest.writeList(this.businessException);
        dest.writeList(this.lunchRegular);
        dest.writeList(this.lunchException);
        dest.writeList(this.bistroRegular);
        dest.writeList(this.bistroException);
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
