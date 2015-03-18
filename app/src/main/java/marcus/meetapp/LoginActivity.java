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


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameField = (EditText) findViewById(R.id.username);
                EditText passwordField = (EditText) findViewById(R.id.password);
                String usernameString = usernameField.getText().toString();
                String passwordString = passwordField.getText().toString();

                SharedPreferences prefs = getSharedPreferences("marcus.meetapp", Context.MODE_PRIVATE);
                String passwordValue = prefs.getString(usernameString + "login", null);

                System.err.println("--> " + passwordString + " --> " + passwordValue);
                if(passwordValue == null) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(usernameString + "login", passwordString);
                    editor.commit();
                } else if(passwordString.equals(passwordValue)){
                    System.err.println("INNE PASSWORD KORREKT");
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    EditText username = (EditText) findViewById(R.id.username);
                    i.putExtra("username", username.getText().toString());
                    startActivity(i);
                } else {
                    System.err.println("INNE PASSWORD FEL");
                    Context context = getApplicationContext();
                    CharSequence text = "Wrong password to that user!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }




            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_remove) {
            buildAlertDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void buildAlertDialog() {
        LayoutInflater linf = LayoutInflater.from(this);
        final View inflator = linf.inflate(R.layout.alert_dialog_remove, null);
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
