package marcus.meetapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends ActionBarActivity {

    ListPerson[] values;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Location paris = new Location("a");
        paris.setLatitude(48);
        paris.setLongitude(2);
        Location beijing = new Location("a");
        beijing.setLatitude(39);
        beijing.setLongitude(116);


        listview = (ListView) findViewById(R.id.list);
/*
        if(values == null) {
            values = new ListPerson[]{new ListPerson("Johan", new Location("a"), "person"),
                    new ListPerson("Paris", paris, "location"), new ListPerson("Linus", new Location("c"), "person"),
                    new ListPerson("Beijing", beijing, "location"), new ListPerson("Boriz", new Location("e"), "person"),
                    new ListPerson("Lena", new Location("e"), "person"), new ListPerson("Erling", new Location("e"), "person"),
                    new ListPerson("Markus", new Location("e"), "person"), new ListPerson("Hugo", new Location("e"), "person")};

        }
*/
        values = loadPrefs();
        final listItemAdapter adapter = new listItemAdapter(this, values);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(getApplicationContext(), MeetingActivity.class);
                ListPerson clickedPerson = values[position];
                i.putExtra("info", (Parcelable) clickedPerson);
                /// sending data to new activity
                //i.putExtra("List_items", List_items);
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

    public void removePosition(int position) {
        boolean positionPassed = false;
        ListPerson[] newArray = new ListPerson[values.length - 1];
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
        final listItemAdapter adapter = new listItemAdapter(this, values);
        listview.setAdapter(adapter);
        savePrefs();
    }

    public void savePrefs() {
        System.err.println("SAVE PREFS " + values.length);
        Set<String> set = new HashSet<String>();
        for(int i = 0; i < values.length; i++) {
            set.add(values[i].toString());
        }
        SharedPreferences prefs = getSharedPreferences("marcus.meetapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet("key", set);
        editor.commit();

        Set<String> ssette = prefs.getStringSet("key", null);
        System.err.println("END SAVE "+ ssette.size());
    }

    public ListPerson[] loadPrefs() {
        SharedPreferences prefs = getSharedPreferences("marcus.meetapp", Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet("key", null);

        System.err.println("LOAD PREFS " + set.size());

        Object[] returnList = set.toArray();
        ListPerson[] listArray = new ListPerson[set.size()];
        for(int i = 0 ; i < set.size() ;i++) {
            String extract = (String) returnList[i];
            String[] split = extract.split("\\^");
            System.err.println("->> " + split[0] + " : " + split[1]);
            ListPerson person = new ListPerson(split[0], split[1], split[2], split[3]);
            listArray[i] = person;
        }
        return listArray;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add_person:
                return true;
            case R.id.action_add_location:
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.promtp_location, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInputLocation = (EditText) promptsView
                        .findViewById(R.id.locationInput);

                final EditText userInputLatitude = (EditText) promptsView
                        .findViewById(R.id.latitudeInput);

                final EditText userInputLongitude = (EditText) promptsView
                        .findViewById(R.id.longitudeInput);


                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //result.setText(userInput.getText());
                                        Location newLocation = new Location("new");
                                        newLocation.setLatitude(Double.valueOf(userInputLatitude.getText().toString()));
                                        newLocation.setLongitude(Double.valueOf(userInputLongitude.getText().toString()));
                                        ListPerson[] newValues = new ListPerson[values.length + 1];
                                        for(int i = 0; i < values.length; i++) {
                                            newValues[i] = values[i];
                                        }
                                        newValues[values.length] = new ListPerson(userInputLocation.getText().toString() , newLocation, "location");
                                        values = newValues;
                                        final listItemAdapter adapter = new listItemAdapter(getBaseContext(), values);
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

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}
