package marcus.meetapp;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


public class MeetingActivity extends ActionBarActivity implements SensorEventListener {

    // define the display assembly compass picture
    private ImageView image;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;

    TextView tvHeading;

    TextView targetCoordinates;
    TextView myCoordinates;

    private String coordinateString;
    private String myCoordinateString;

    private GPSTracker gpsTracker;

    private MyService myService;

    ListPerson clickedPerson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        //
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.tvHeading);

        targetCoordinates = (TextView) findViewById(R.id.target_coordinates);
        myCoordinates = (TextView) findViewById(R.id.my_coordinates);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Intent i = getIntent();
        // getting attached intent data
        clickedPerson = i.getParcelableExtra("info");

        coordinateString = String.format(getResources().getString(R.string.target_coordinates),
                clickedPerson.getLocation().getLatitude(), clickedPerson.getLocation().getLongitude());

        targetCoordinates.setText(coordinateString);

        gpsTracker = new GPSTracker(this);

        myService = new MyService();

        Location myLocation = gpsTracker.getLocation();
        System.out.println("LOCATION " + myLocation.getLatitude() + " " + myLocation.getLongitude());
        myCoordinateString = String.format(getResources().getString(R.string.my_coordinates),
                myLocation.getLatitude(), myLocation.getLongitude());

        myCoordinates.setText(myCoordinateString);


    }

    private float calculateBearing() {
        return gpsTracker.getLocation().bearingTo(clickedPerson.getLocation());
    }

    private float calculateDistance() {
        return gpsTracker.getLocation().distanceTo(clickedPerson.getLocation());
    }


    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated, simply add nr of degrees
        float bearing = calculateBearing();

        float distance = calculateDistance();

        float degree = Math.round(event.values[0] - bearing);

        tvHeading.setText("Distance: " + Float.toString(distance) + " meters ");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}