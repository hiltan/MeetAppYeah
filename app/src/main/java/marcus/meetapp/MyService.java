package marcus.meetapp;

/**
 * Created by Hiltan on 15-03-11.
 */
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

    private GPSTracker gpsTracker;
    private Handler handler= new Handler();
    private Timer timer = new Timer();
    private Distance pastDistance = new Distance();
    private Distance currentDistance = new Distance();
    public static double DISTANCE;
    boolean flag = true ;
    private double totalDistance ;
    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
        gpsTracker = new GPSTracker(getBaseContext());
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if(flag){
                            pastDistance.setLatitude(gpsTracker.getLocation().getLatitude());
                            pastDistance.setLongitude(gpsTracker.getLocation().getLongitude());
                            flag = false;
                        }else{
                            currentDistance.setLatitude(gpsTracker.getLocation().getLatitude());
                            currentDistance.setLongitude(gpsTracker.getLocation().getLongitude());
                            flag = comapre_LatitudeLongitude();
                        }


                    }
                });


            }
        };

        timer.schedule(timerTask,0, 5000);

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }



    @Override
    public void onDestroy() {

        super.onDestroy();
        System.out.println("--------------------------------onDestroy -stop service ");
        timer.cancel();
        DISTANCE = totalDistance ;
    }
    public boolean comapre_LatitudeLongitude(){

        if(pastDistance.getLatitude() == currentDistance.getLatitude() && pastDistance.getLongitude() == currentDistance.getLongitude()){
            return false;
        }else{

            final double distance = distance(pastDistance.getLatitude(),pastDistance.getLongitude(),currentDistance.getLatitude(),currentDistance.getLongitude());
            System.out.println("Distance in mile :"+distance);
            handler.post(new Runnable() {

                @Override
                public void run() {
                    float kilometer=1.609344f;

                    totalDistance = totalDistance +  distance * kilometer;
                    DISTANCE = totalDistance;
                    //Toast.makeText(HomeFragment.HOMECONTEXT, "distance in km:"+DISTANCE, 4000).show();

                }
            });

            return true;
        }

    }

}