package com.linkgent.xxaidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ping on 2015/9/1.
 */
public class XxLocationInfo implements Parcelable {

    public XxLocationInfo(){
    }


    protected XxLocationInfo(Parcel in) {
        lon = in.readDouble();
        lat = in.readDouble();
        speed = in.readFloat();
        bearing = in.readFloat();
        altitude = in.readDouble();
        timestamp = in.readLong();
        state = in.readInt();
        type = in.readInt();
    }

    public static final Creator<XxLocationInfo> CREATOR = new Creator<XxLocationInfo>() {
        @Override
        public XxLocationInfo createFromParcel(Parcel in) {
            return new XxLocationInfo(in);
        }

        @Override
        public XxLocationInfo[] newArray(int size) {
            return new XxLocationInfo[size];
        }
    };

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    double lon;
    double lat;
    float speed;
    float bearing;
    double altitude;
    long timestamp;
    int state;
    int type ;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lon);
        dest.writeDouble(lat);
        dest.writeFloat(speed);
        dest.writeFloat(bearing);
        dest.writeDouble(altitude);
        dest.writeLong(timestamp);
        dest.writeInt(state);
        dest.writeInt(type);
    }
}
