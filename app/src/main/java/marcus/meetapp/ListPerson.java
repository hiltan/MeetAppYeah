package marcus.meetapp;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Hiltan on 15-03-08.
 */
public class ListPerson implements Parcelable {
    String name;
    Location location;
    String personOrLocation;

    public ListPerson(String name, Location location, String personOrLocation) {
        this.name = name;
        this.location = location;
        this.personOrLocation = personOrLocation;
    }

    public ListPerson(String name, String latitude, String longitude, String personOrLocation) {
        this.name = name;
        Location newLocation = new Location("Construct");
        newLocation.setLatitude(Double.valueOf(latitude));
        newLocation.setLongitude(Double.valueOf(longitude));
        this.location = newLocation;
        this.personOrLocation = personOrLocation;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getPersonOrLocation() {
        return personOrLocation;
    }

    public ListPerson(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        location = new Location("provider");
        this.name = data[0];
        this.location.setLatitude(Double.valueOf(data[1]));
        this.location.setLongitude(Double.valueOf(data[2]));
        this.personOrLocation = data[3];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.name,
                String.valueOf(this.location.getLatitude()),
                String.valueOf(this.location.getLongitude()),
                personOrLocation});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ListPerson createFromParcel(Parcel in) {
            return new ListPerson(in);
        }

        public ListPerson[] newArray(int size) {
            return new ListPerson[size];
        }
    };

    @Override
    public String toString() {
        return name + "^" + location.getLatitude() + "^" + location.getLongitude() + "^" + personOrLocation;
    }
}
