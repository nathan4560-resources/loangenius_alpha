package my.utar.edu.loangenius;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.RadioButton;


import java.text.DecimalFormat;
import java.math.RoundingMode;
import java.util.Calendar;
import java.time.LocalDate;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class InsideActivity extends AppCompatActivity {

    private static final String SHARED_PREF_CONFIG = "config";
    private static final String KEY_TOTALMONTHS = "vault";
    //3 STEPS FOR SHARED PREFERENCES
    SharedPreferences sp;
    //LOAN START DATE
    EditText loanmonths, loanyears;
    //BUTTON TO TRIGGER THE LISTENER TO SQLITE
    Button calca;
    //A TIME CHECKSUM
    Calendar c = Calendar.getInstance();
    Integer chkyr = c.get(Calendar.YEAR);
    //ADD A SHAREDPREF FOR EDITTEXT
    SharedPreferences loaninit;
    //RADIO BUTTON
    RadioButton r1;
    RadioButton r2;


    @Override
    protected void onCreate(Bundle savedInstanceState) throws NumberFormatException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside);
        sp = getSharedPreferences(SHARED_PREF_CONFIG, MODE_PRIVATE);
        String configuredmonths = sp.getString(KEY_TOTALMONTHS, null);
        int volt = Integer.parseInt(configuredmonths);
        Log.i("InsideActivity", "Volt: " + volt);
        //BUTTON ITEM
        calca = findViewById(R.id.calca);
        loanmonths = findViewById(R.id.lsmths);
        loanyears = findViewById(R.id.lsyrs);
        TextInputLayout t1 = findViewById(R.id.amtans1);
        TextInputLayout t2 = findViewById(R.id.percentageans1);
        TextInputLayout t3 = findViewById(R.id.rmptans1);
        //RADIO BUTTON CHECK
        r1 = findViewById(R.id.rb1);
        r2 = findViewById(R.id.rb2);
        Log.i("InsideActivity", "This year is: " + chkyr);

        calca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(loanmonths.getText().toString()) || TextUtils.isEmpty(loanyears.getText().toString())) {
                    AlertDialog.Builder a1 = new AlertDialog.Builder(InsideActivity.this);
                    a1.setCancelable(false);
                    a1.setTitle("Missing something?");
                    a1.setMessage("Starting month and year is required for calculation.");
                    a1.setNegativeButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    final AlertDialog a01 = a1.create();
                    a01.show();
                }
                //A TIME CHECKSUM
                else if ((Integer.parseInt(loanmonths.getText().toString()) >= 13) || Integer.parseInt(loanyears.getText().toString()) < chkyr) {
                    AlertDialog.Builder a2 = new AlertDialog.Builder(InsideActivity.this);
                    a2.setCancelable(false);
                    a2.setTitle("Uh-oh");
                    a2.setMessage("Check your input and try again.");
                    a2.setNegativeButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    final AlertDialog a02 = a2.create();
                    a02.show();
                } else {
                    Integer loanpaymode =0;
                    if(r1.isChecked()){
                        loanpaymode = 1;
                    }
                    else if(r2.isChecked()){
                        loanpaymode = 2;
                    }
                    else{
                        loanpaymode = 0;
                    }
                    Integer loanstartmonths = Integer.parseInt(loanmonths.getText().toString());
                    Integer loanstartyears = Integer.parseInt(loanyears.getText().toString());
                    Float z1 = Float.parseFloat(t1.getEditText().getText().toString());
                    Float z2 = Float.parseFloat(t2.getEditText().getText().toString());
                    Integer z3 = Integer.parseInt(t3.getEditText().getText().toString());
                    Log.i("InsideActivity","Principal is: "+ z1 + " And interest will be set in: " + z2 + ". Repayments set in " + z3 + " installments");
                    Log.i("InsideActivity", "Loan will start at month " + loanstartmonths + " at year " + loanstartyears);
                    //SHARED PREFERENCES IS LOANINIT, EDITOR IS LOANCOMMIT
                    loaninit = getSharedPreferences("initializeloan",MODE_PRIVATE);
                    SharedPreferences.Editor loancommit = loaninit.edit();
                    loancommit.putFloat("loanprincipal",z1);
                    loancommit.putFloat("loanpercentage", z2);
                    loancommit.putInt("loanrepaymentcounter",z3);
                    loancommit.putInt("loanstartingmonths",loanstartmonths);
                    loancommit.putInt("loanstartingyears",loanstartyears);
                    loancommit.putInt("loanpaymentmodes",loanpaymode);
                    loancommit.apply();
                    Intent verdict = new Intent(InsideActivity.this,Result.class);
                    startActivity(verdict);
                }
            }
        });


    }

}