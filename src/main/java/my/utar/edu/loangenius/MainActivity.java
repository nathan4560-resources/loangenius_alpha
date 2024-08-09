package my.utar.edu.loangenius;

import static android.app.PendingIntent.getActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.IllegalFormatException;

public class MainActivity extends AppCompatActivity  {
    private static final String SHARED_PREF_CONFIG = "config";
    private static final String KEY_TOTALMONTHS = "vault";
    EditText inputmonth,inputyear;
    Button button;
    String beforerock = LocalDate.now().toString();
    Integer startyr = Integer.parseInt(beforerock.substring(0,4));
    Integer startmth = Integer.parseInt(beforerock.substring(5,7));
    LocalDate startdate = LocalDate.of(startyr,startmth,1);
    SharedPreferences sp;


    //KNOWN ISSUES: APP WILL FORCE QUIT EVEN EXCEPTION HANDLING IS IMPLEMENTED
    @Override
    protected void onCreate(Bundle savedInstanceState) throws NumberFormatException, DateTimeException, IllegalArgumentException, IllegalFormatException {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.letsrock);

        //CONFIGURING THE SHARED PREFERENCES
        sp = getSharedPreferences(SHARED_PREF_CONFIG,MODE_PRIVATE);
        String savedMonths = sp.getString(KEY_TOTALMONTHS,null);

        if(savedMonths !=null){
            Intent alternateIntent = new Intent(MainActivity.this, InsideActivity.class);
            startActivity(alternateIntent);
            finish();
        }




        //END OF SHARED PREFERENCES CONFIGURATION


        button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view)  {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            inputmonth = findViewById(R.id.month);
            inputyear = findViewById(R.id.year);
            Integer endmonth = Integer.parseInt(inputmonth.getText().toString());
            Integer endyear = Integer.parseInt(inputyear.getText().toString());
            LocalDate enddate = LocalDate.of(endyear, endmonth, 1);
            Period age = Period.between(enddate,startdate);
            Integer totalMonths = age.getMonths() + (12*age.getYears());
            //IMPORTANT TO EXTRACT
            //FOR DEBUGING PURPOSE
            Log.i("MainActivity","Age: " + age);
            Log.i("MainActivity","TotalMonths: " + totalMonths);
            try{
                if(inputmonth.getText() == null || inputyear.getText() == null){
                    throw new NumberFormatException("Check your input!");
                }
                else if(totalMonths<0){
                    throw new DateTimeException("Check your input!");
                }
                else if (totalMonths > 840) {
                    builder.setTitle("Uh-oh");
                    builder.setMessage("Looks like you are a little too old to apply for a loan.");
                    builder.setCancelable(false);
                    builder.setNegativeButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                } else{

                    builder.setTitle("Notice");
                    builder.setMessage("For Personal Loans: \n 1. Borrow only what you can afford to repay. \n 2. Understanding the loan terms and interest rates. \n 3.Avoid using personal loans for unnecessary expenses.\n\n For Housing Loans:\n 1. Borrow responsibly.\n 2. Be aware of what you commit.\n 3. Choose a loan with terms you understand. \n\nStorage Access may be required for app's full functionality.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Get Started", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences.Editor rising = sp.edit();
                            rising.putString(KEY_TOTALMONTHS,String.valueOf(totalMonths));
                            rising.apply();
                            Intent secondIntent = new Intent(MainActivity.this, InsideActivity.class);
                            startActivity(secondIntent);
                            finish();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                }
            }
            catch (DateTimeException e2){
                Toast.makeText(MainActivity.this,"Check your input (LGE002)",Toast.LENGTH_SHORT).show();
            }
            catch(NumberFormatException e3){
                Toast.makeText(MainActivity.this,"Check your input (LGE003)",Toast.LENGTH_SHORT).show();
            }
            catch(IllegalArgumentException e4){
                Toast.makeText(MainActivity.this,"Check your input (LGE004)",Toast.LENGTH_SHORT).show();
            }
            finally{
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    });


    }

    @Override
    protected void onPause(){
      super.onPause();

    }
    @Override
    protected void onResume(){
        //TOTALMONTHS.XML STORES THE TOTAL MONTHS
        super.onResume();
    }
}