package cn.wzbos.protocol.samplea.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wuzongbo on 2017/4/11.
 */
public class Broker implements Parcelable {
    private String sex;
    private int age;

    public Broker(String sex, int age) {
        this.sex = sex;
        this.age = age;
    }

    protected Broker(Parcel in) {
        sex = in.readString();
        age = in.readInt();
    }


    public static final Creator<Broker> CREATOR = new Creator<Broker>() {
        @Override
        public Broker createFromParcel(Parcel in) {
            return new Broker(in);
        }

        @Override
        public Broker[] newArray(int size) {
            return new Broker[size];
        }
    };

    public int getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sex);
        dest.writeInt(age);
    }

    @Override
    public String toString() {
        return "Broker{" +
                "sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }
}
