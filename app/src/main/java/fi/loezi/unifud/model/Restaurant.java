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
    private String businessRegular;
    private String businessException;
    private String lunchRegular;
    private String lunchException;
    private String bistroRegular;
    private String bistroException;

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

    public String getBusinessRegular() {

        return businessRegular;
    }

    public void setBusinessRegular(final String businessRegular) {

        this.businessRegular = businessRegular;
    }

    public String getBusinessException() {

        return businessException;
    }

    public void setBusinessException(final String businessException) {

        this.businessException = businessException;
    }

    public String getLunchRegular() {

        return lunchRegular;
    }

    public void setLunchRegular(final String lunchRegular) {

        this.lunchRegular = lunchRegular;
    }

    public String getLunchException() {

        return lunchException;
    }

    public void setLunchException(final String lunchException) {

        this.lunchException = lunchException;
    }

    public String getBistroRegular() {

        return bistroRegular;
    }

    public void setBistroRegular(final String bistroRegular) {

        this.bistroRegular = bistroRegular;
    }

    public String getBistroException() {

        return bistroException;
    }

    public void setBistroException(final String bistroException) {

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
        this.businessRegular = in.readString();
        this.businessException = in.readString();
        this.lunchRegular = in.readString();
        this.lunchException = in.readString();
        this.bistroRegular = in.readString();
        this.bistroException = in.readString();
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
        dest.writeString(this.businessRegular);
        dest.writeString(this.businessException);
        dest.writeString(this.lunchRegular);
        dest.writeString(this.lunchException);
        dest.writeString(this.bistroRegular);
        dest.writeString(this.bistroException);
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
