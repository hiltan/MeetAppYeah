package marcus.meetapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The class that handles the info shown in the screen when a user has selected
 * a person or location that hen wants to know the direction and distance to.
 */
public class MeetingActivity extends ActionBarActivity implements SensorEventListener {

    private ImageView image;

    private float currentDegree = 0f;

    private SensorManager sensorManager;

    TextView distanceToTarget;

    TextView targetCoordinates;
    TextView myCoordinates;

    private String coordinateString;
    private String myCoordinateString;

    private GPSTracker gpsTracker;


    ListPersonLocation clickedPerson;

    /**
     * On create this class gets info from the intent about the person or
     * location used to calculate bearing and distance. The coordinates of
     * the device and the target is shown.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        image = (ImageView) findViewById(R.id.imageViewCompass);

        distanceToTarget = (TextView) findViewById(R.id.tvHeading);

        targetCoordinates = (TextView) findViewById(R.id.target_coordinates);
        myCoordinates = (TextView) findViewById(R.id.my_coordinates);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Intent i = getIntent();
        clickedPerson = i.getParcelableExtra("info");

        coordinateString = String.format(getResources().getString(R.string.target_coordinates),
                clickedPerson.getLocation().getLatitude(), clickedPerson.getLocation().getLongitude());

        targetCoordinates.setText(coordinateString);

        gpsTracker = new GPSTracker(this);


        Location myLocation = gpsTracker.getLocation();

        myCoordinateString = String.format(getResources().getString(R.string.my_coordinates),
                myLocation.getLatitude(), myLocation.getLongitude());

        myCoordinates.setText(myCoordinateString);
    }

    /**
     * Method using the GPS tracker to get the location of the device and then
     * calculates the bearing to the target.
     * @return
     */
    private float calculateBearing() {
        return gpsTracker.getLocation().bearingTo(clickedPerson.getLocation());
    }

    /**
     * Method using the GPS tracker to get the location of the device and then
     * calculates the distance to the target.
     * @return
     */
    private float calculateDistance() {
        return gpsTracker.getLocation().distanceTo(clickedPerson.getLocation());
    }

    /**
     * Method called if the activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * On pause the sensor manager unregisters this to stop the sensors and
     * save battery.
     */
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    /**
     * When the sensor changes this method is used to update the distance
     * and direction pointer shown in this activity.
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        float bearing = calculateBearing();

        float distance = calculateDistance();

        float degree = Math.round(event.values[0] - bearing);

        distanceToTarget.setText("Distance: " + Float.toString(distance) + " meters ");

        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        ra.setDuration(210);

        ra.setFillAfter(true);

        image.startAnimation(ra);
        currentDegree = -degree;

    }

    /**
     * Not used.
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}