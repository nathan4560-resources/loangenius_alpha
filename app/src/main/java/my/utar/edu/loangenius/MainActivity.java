package my.utar.edu.loangenius;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.content.SharedPreferences;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;
import android.content.DialogInterface;

import java.time.DateTimeException;
import java.lang.NumberFormatException;
import java.time.LocalDate;
import java.lang.*;
import java.time.Period;
import java.util.Calendar;
import java.util.IllegalFormatException;

public class MainActivity extends AppCompatActivity {
    private static final String SHARED_PREF_CONFIG = "config";
    private static final String KEY_TOTALMONTHS = "vault";
    EditText inputmonth, inputyear;
    Button button;
    String beforerock = LocalDate.now().toString();
    Integer startyr = Integer.parseInt(beforerock.substring(0, 4));
    Integer startmth = Integer.parseInt(beforerock.substring(5, 7));
    LocalDate startdate = LocalDate.of(startyr, startmth, 1);
    SharedPreferences sp;

    Calendar lginit = Calendar.getInstance();

    //KNOWN ISSUES: APP WILL FORCE QUIT EVEN EXCEPTION HANDLING IS IMPLEMENTED
    @Override
    protected void onCreate(Bundle savedInstanceState) throws NumberFormatException, DateTimeException, IllegalArgumentException, IllegalFormatException {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.letsrock);

        //CONFIGURING THE SHARED PREFERENCES
        sp = getSharedPreferences(SHARED_PREF_CONFIG, MODE_PRIVATE);
        String savedMonths = sp.getString(KEY_TOTALMONTHS, null);

        if (savedMonths != null) {
            Intent alternateIntent = new Intent(MainActivity.this, InsideActivity.class);
            startActivity(alternateIntent);
            finish();
        }


        //END OF SHARED PREFERENCES CONFIGURATION


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputmonth = findViewById(R.id.month);
                inputyear = findViewById(R.id.year);
                //DUMMY INFO
                Integer endmonth = 12;
                Integer endyear = 2000;
                if(!(TextUtils.isEmpty(inputyear.getText().toString()))){
                    endyear =  Integer.parseInt(inputyear.getText().toString());
                }
                if(!(TextUtils.isEmpty(inputyear.getText().toString()))) {
                    endmonth = Integer.parseInt(inputmonth.getText().toString());
                }

                LocalDate enddate = LocalDate.of(endyear, endmonth, 1);
                Period age = Period.between(enddate, startdate);
                Integer totalMonths = age.getMonths() + (12 * age.getYears());
                //IMPORTANT TO EXTRACT
                //FOR DEBUGING PURPOSE
                Log.i("MainActivity", "Age: " + age);
                Log.i("MainActivity", "TotalMonths: " + totalMonths);

                if (TextUtils.isEmpty(inputyear.getText().toString()) || TextUtils.isEmpty(inputmonth.getText().toString())) {
                    AlertDialog.Builder i0 = new AlertDialog.Builder(MainActivity.this);
                    i0.setCancelable(false);
                    i0.setTitle("Missing something?");
                    i0.setMessage("Your birth year and month is required to proceed.");
                    i0.setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.cancel();
                        }
                    });
                    final AlertDialog i00 = i0.create();
                    i00.show();
                }
                //FUTURE BORN DATE
                else if (totalMonths < 0) {
                    AlertDialog.Builder i1 = new AlertDialog.Builder(MainActivity.this);
                    i1.setCancelable(false);
                    i1.setTitle("Got your date correctly?");
                    i1.setMessage("Our time machine can't travel you to the future. Make sure is correct.");
                    i1.setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            dialogInterface.cancel();
                        }
                    });
                    final AlertDialog i01 = i1.create();
                    i01.show();
                } else if (totalMonths > 840) {
                    AlertDialog.Builder i2 = new AlertDialog.Builder(MainActivity.this);
                    i2.setTitle("Uh-oh");
                    i2.setMessage("Looks like you are a little too old to apply for a loan.");
                    i2.setCancelable(false);
                    i2.setNegativeButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    final AlertDialog i02 = i2.create();
                    i02.show();
                }
                //LESS THAN 13 YEARS OLD
                else if(totalMonths <=156){
                    AlertDialog.Builder i3 = new AlertDialog.Builder(MainActivity.this);
                    i3.setTitle("Uh-oh");
                    i3.setMessage("Looks like you are a little too young to apply for a loan.");
                    i3.setCancelable(false);
                    i3.setNegativeButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    final AlertDialog i03 = i3.create();
                    i03.show();
                }
                else {
                    AlertDialog.Builder lgpass = new AlertDialog.Builder(MainActivity.this);
                    lgpass.setTitle("Notice");
                    lgpass.setMessage("For Personal Loans: \n1. Borrow only what you can afford to repay. \n2. Understanding the loan terms and interest rates. \n3.Avoid using personal loans for unnecessary expenses.\n\n For Housing Loans:\n 1. Borrow responsibly.\n 2. Be aware of what you commit.\n 3. Choose a loan with terms you understand. \n\nStorage Access may be required for app's full functionality.");
                    lgpass.setCancelable(false);
                    lgpass.setPositiveButton("Get Started", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences.Editor rising = sp.edit();
                            rising.putString(KEY_TOTALMONTHS, String.valueOf(totalMonths));
                            rising.apply();
                            Intent secondIntent = new Intent(MainActivity.this, InsideActivity.class);
                            startActivity(secondIntent);
                            finish();
                        }
                    });
                    lgpass.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    final AlertDialog lgpassed = lgpass.create();
                    lgpassed.show();
                }
            }

        });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        //TOTALMONTHS.XML STORES THE TOTAL MONTHS
        super.onResume();
    }
}