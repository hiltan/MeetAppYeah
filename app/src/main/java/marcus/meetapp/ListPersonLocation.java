package marcus.meetapp;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class representing a person or a location that the user has saved.
 */
public class ListPersonLocation implements Parcelable {
    String name;
    Location location;
    String personOrLocation;

    /**
     * Constructor.
     * @param name
     * @param location
     * @param personOrLocation
     */
    public ListPersonLocation(String name, Location location, String personOrLocation) {
        this.name = name;
        this.location = location;
        this.personOrLocation = personOrLocation;
    }

    /**
     * Constructor using strings with latitude and longitude instead of a Location.
     * @param name
     * @param latitude
     * @param longitude
     * @param personOrLocation
     */
    public ListPersonLocation(String name, String latitude, String longitude, String personOrLocation) {
        this.name = name;
        Location newLocation = new Location("Construct");
        newLocation.setLatitude(Double.valueOf(latitude));
        newLocation.setLongitude(Double.valueOf(longitude));
        this.location = newLocation;
        this.personOrLocation = personOrLocation;
    }

    /**
     * Name getter.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Location getter.
     * @return
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Getter for info if its a person or location.
     * @return
     */
    public String getPersonOrLocation() {
        return personOrLocation;
    }

    /**
     * Constructor using a Parcel.
     * @param in
     */
    public ListPersonLocation(Parcel in){
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

    /**
     * Method writing info to a parcel.
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.name,
                String.valueOf(this.location.getLatitude()),
                String.valueOf(this.location.getLongitude()),
                personOrLocation});
    }

    /**
     * Method used when creating a ListPersonLocation from a parcel.
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ListPersonLocation createFromParcel(Parcel in) {
            return new ListPersonLocation(in);
        }

        public ListPersonLocation[] newArray(int size) {
            return new ListPersonLocation[size];
        }
    };

    /**
     * Redefined toString. Used when saving info to shared preferences.
     * @return
     */
    @Override
    public String toString() {
        return name + "^" + location.getLatitude() + "^" + location.getLongitude() + "^" + personOrLocation;
    }
}
