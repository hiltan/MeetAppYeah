package marcus.meetapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.HashSet;
import java.util.Set;

/**
 * Activity showing the list of persons and locations a user has added.
 * In this version it adds a few locations when you create a user just
 * to display the look without having to add locations and/or people the
 * first thing creating a user.
 */
public class MainActivity extends ActionBarActivity {

    ListPersonLocation[] values;
    ListView listview;
    String username;

    /**
     * Sets up the view loading user preferences stored in shared preferences.
     * Also sets listeners for the ListView.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.list);

        Intent i = getIntent();
        username = i.getStringExtra("username");
        values = loadPrefs();
        savePrefs();
        final ListItemAdapter adapter = new ListItemAdapter(this, values);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(getApplicationContext(), MeetingActivity.class);
                ListPersonLocation clickedPerson = values[position];
                i.putExtra("info", (Parcelable) clickedPerson);
                savePrefs();
                startActivity(i);
            }


        });


        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ImageButton checker = (ImageButton) view.findViewById(R.id.removeChecker);
                ImageButton cross = (ImageButton) view.findViewById(R.id.removeCross);
                ImageView icon = (ImageView) view.findViewById(R.id.icon);
                checker.setVisibility(View.VISIBLE);
                cross.setVisibility(View.VISIBLE);

                int deleteId = getResources().getIdentifier("marcus.meetapp:drawable/ic_delete_red", null, null);
                icon.setImageDrawable(getResources().getDrawable(deleteId));
                return true;
            }
        });


    }

    /**
     * Removes a position in the list by creating a new array without the
     * location or person in the position given as argument. Then using the
     * adapter to show the new list.
     * @param position
     */
    public void removePosition(int position) {
        boolean positionPassed = false;
        ListPersonLocation[] newArray = new ListPersonLocation[values.length - 1];
        for(int i = 0; i < values.length ; i++) {
            if(i != position && !positionPassed) {
                newArray[i] = values[i];
            } else if(i != position && positionPassed) {
                newArray[i-1] = values[i];
            } else if(i == position) {
                positionPassed = true;
            }
        }
        values = newArray;
        final ListItemAdapter adapter = new ListItemAdapter(this, values);
        listview.setAdapter(adapter);
        savePrefs();
    }

    /**
     * Saves locations and persons to shared preferences.
     */
    public void savePrefs() {
        Set<String> set = new HashSet<String>();
        for(int i = 0; i < values.length; i++) {
            set.add(values[i].toString());
        }
        SharedPreferences prefs = getSharedPreferences("marcus.meetapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(username, set);
        editor.commit();
    }

    /**
     * Loads locations and persons from shared preferences.
     * @return
     */
    public ListPersonLocation[] loadPrefs() {
        SharedPreferences prefs = getSharedPreferences("marcus.meetapp", Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet(username, null);
        if(set == null) {
            Location paris = new Location("a");
            paris.setLatitude(48);
            paris.setLongitude(2);
            Location beijing = new Location("a");
            beijing.setLatitude(39);
            beijing.setLongitude(116);
            Location johan = new Location("a");
            johan.setLatitude(63.820269);
            johan.setLongitude(20.307408);

            ListPersonLocation[] listArray = new ListPersonLocation[] {
                                     new ListPersonLocation("Auto generated: Beijing", beijing, "location"),
                                     new ListPersonLocation("Auto generated: Lena", johan, "person"),
                                     new ListPersonLocation("Auto generated: Paris", paris, "location"),
                                     new ListPersonLocation("Auto generated: Johan", johan, "person")};
            return listArray;
        }

        Object[] returnList = set.toArray();
        ListPersonLocation[] listArray = new ListPersonLocation[set.size()];
        for(int i = 0 ; i < set.size() ;i++) {
            String extract = (String) returnList[i];
            String[] split = extract.split("\\^");
            if(split[0].contains("Auto generated: ")) {
                String newSplit0 = split[0].replaceFirst("Auto generated: ", "");
                split[0] = newSplit0;
            }
            ListPersonLocation person = new ListPersonLocation(split[0], split[1], split[2], split[3]);
            listArray[i] = person;
        }
        return listArray;
    }

    /**
     * Inflates the actionbar.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Defines the actions when a user clicks the icons in the actionbar.
     * Settings option is only for display at the moment.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add_person:
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                View personView = layoutInflater.inflate(R.layout.promtp_person, null);

                buildAlertDialog(personView, "person");
                return true;
            case R.id.action_add_location:
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.promtp_location, null);

                buildAlertDialog(promptsView, "location");
                return true;
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Builds the alert dialog used when adding a person or a location.
     * @param promptsView
     */
    private void buildAlertDialog(View promptsView, final String personOrLocation) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInputLocation = (EditText) promptsView
                .findViewById(R.id.locationInput);

        final EditText userInputLatitude = (EditText) promptsView
                .findViewById(R.id.latitudeInput);

        final EditText userInputLongitude = (EditText) promptsView
                .findViewById(R.id.longitudeInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Location newLocation = new Location("new");
                                newLocation.setLatitude(Double.valueOf(userInputLatitude.getText().toString()));
                                newLocation.setLongitude(Double.valueOf(userInputLongitude.getText().toString()));
                                ListPersonLocation[] newValues = new ListPersonLocation[values.length + 1];
                                for(int i = 0; i < values.length; i++) {
                                    newValues[i] = values[i];
                                }
                                newValues[values.length] = new ListPersonLocation(userInputLocation.getText().toString() , newLocation, personOrLocation);
                                values = newValues;
                                final ListItemAdapter adapter = new ListItemAdapter(getBaseContext(), values);
                                listview.setAdapter(adapter);
                                savePrefs();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }


}
