package fi.loezi.unifud.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ExceptionalHours implements Parcelable{

    private String from;
    private String to;
    private boolean closed;
    private String open;
    private String close;

    public ExceptionalHours(final String from,
                            final String to,
                            final boolean closed,
                            final String open,
                            final String close) {

        this.from = from;
        this.to = to;
        this.closed = closed;
        this.open = open;
        this.close = close;
    }

    public String getFrom() {

        return from;
    }

    public void setFrom(final String from) {

        this.from = from;
    }

    public String getTo() {

        return to;
    }

    public void setTo(final String to) {

        this.to = to;
    }

    public boolean isClosed() {

        return closed;
    }

    public void setClosed(final boolean closed) {

        this.closed = closed;
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
    public ExceptionalHours(final Parcel in) {

        super();

        this.from = in.readString();
        this.to = in.readString();
        this.closed = in.readByte() != 0;
        this.open = in.readString();
        this.close = in.readString();
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {

        dest.writeString(this.from);
        dest.writeString(this.to);
        dest.writeByte((byte) (this.closed ? 1 : 0));
        dest.writeString(this.open);
        dest.writeString(this.close);
    }

    public static final Parcelable.Creator<ExceptionalHours> CREATOR = new Parcelable.Creator<ExceptionalHours>() {

        public ExceptionalHours createFromParcel(final Parcel in) {

            return new ExceptionalHours(in);
        }

        public ExceptionalHours[] newArray(final int size) {

            return new ExceptionalHours[size];
        }
    };
}
