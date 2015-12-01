package marcus.meetapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

/**
 * Class that defines what happens on the login activity.
 */
public class LoginActivity extends ActionBarActivity {

    /**
     * On create this method sets the listener to the login button.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * When clicking the login button this listener is called. It will
             * check if the user exists and if so the password and either log
             * in or decline the request. If the user does not exist the user
             * will be created with the given password. Users and password are
             * stored in the shared preferences.
             * @param v
             */
            @Override
            public void onClick(View v) {
                EditText usernameField = (EditText) findViewById(R.id.username);
                EditText passwordField = (EditText) findViewById(R.id.password);
                String usernameString = usernameField.getText().toString();
                String passwordString = passwordField.getText().toString();

                SharedPreferences prefs = getSharedPreferences("marcus.meetapp", Context.MODE_PRIVATE);
                String passwordValue = prefs.getString(usernameString + "login", null);

                if(passwordValue == null) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(usernameString + "login", passwordString);
                    editor.commit();
                } else if(passwordString.equals(passwordValue)){
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    EditText username = (EditText) findViewById(R.id.username);
                    i.putExtra("username", username.getText().toString());
                    startActivity(i);
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Wrong password to that user!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }

    /**
     * Adds the remove user option to the actionbar.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    /**
     * If the remove user button in the action bar is clicked a alert dialog
     * will appear where the user can enter username and password to delete
     * a user.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_remove) {
            buildAlertDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method used to build the alert dialog used when pushing the remove user
     * button.
     */
    private void buildAlertDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View inflator = layoutInflater.inflate(R.layout.alert_dialog_remove, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Remove");

        alert.setView(inflator);

        final EditText username = (EditText) inflator.findViewById(R.id.alert_username);
        final EditText password = (EditText) inflator.findViewById(R.id.alert_password);

        alert.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();
                SharedPreferences prefs = getSharedPreferences("marcus.meetapp", Context.MODE_PRIVATE);
                String passwordValue = prefs.getString(usernameString + "login", null);
                if(passwordString.equals(passwordValue)) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove(usernameString + "login");
                    editor.remove(usernameString);
                    editor.commit();
                    Context context = getApplicationContext();
                    CharSequence text = "User removed!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else if(passwordValue == null) {
                    Context context = getApplicationContext();
                    CharSequence text = "That user does not exist!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Wrong password to that user or that user!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.show();
    }
}
