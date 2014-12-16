package fi.loezi.unifud.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RegularHours implements Parcelable {

    private List<String> days;
    private String open;
    private String close;

    public RegularHours(final List<String> days,
                        final String open,
                        final String close) {

        this.days = days;
        this.open = open;
        this.close = close;
    }

    public List<String> getDays() {

        return days;
    }

    public void setDays(final List<String> days) {

        this.days = days;
    }

    public String getOpen() {

        return open;
    }

    public void setOpen(final String open) {

        this.open = open;
    }

    public String getClose() {

        return close;
    }

    public void setClose(final String close) {

        this.close = close;
    }

    /* Parcelable */
    public RegularHours(final Parcel in) {

        super();

        in.readList(this.days, null);
        this.open = in.readString();
        this.close = in.readString();
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {

        dest.writeList(this.days);
        dest.writeString(this.open);
        dest.writeString(this.close);

    }

    public static final Parcelable.Creator<RegularHours> CREATOR = new Parcelable.Creator<RegularHours>() {

        public RegularHours createFromParcel(final Parcel in) {

            return new RegularHours(in);
        }

        public RegularHours[] newArray(final int size) {

            return new RegularHours[size];
        }
    };
}


