package com.linkgent.xxaidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ping on 2015/9/2.
 */
public class XxDistrict implements Parcelable {
    public  XxDistrict(){
    }

    public void copy(XxDistrict district){
        this.country = district.country;
        this.province = district.province;
        this.provinceCode = district.provinceCode;
        this.city = district.city;
        this.cityCode = district.cityCode;
        this.area = district.area;
        this.areaCode = district.areaCode;
    }


    protected XxDistrict(Parcel in) {
        country = in.readString();
        province = in.readString();
        city = in.readString();
        area = in.readString();
        provinceCode = in.readInt();
        cityCode = in.readInt();
        areaCode = in.readInt();
    }

    public static final Creator<XxDistrict> CREATOR = new Creator<XxDistrict>() {
        @Override
        public XxDistrict createFromParcel(Parcel in) {
            return new XxDistrict(in);
        }

        @Override
        public XxDistrict[] newArray(int size) {
            return new XxDistrict[size];
        }
    };

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    //国家
    String country = "中国";
    //省
    String province;
    //市
    String city;
    //区
    String area;

    int provinceCode;

    int cityCode;

    int areaCode;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(country);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(area);
        dest.writeInt(provinceCode);
        dest.writeInt(cityCode);
        dest.writeInt(areaCode);
    }
}
