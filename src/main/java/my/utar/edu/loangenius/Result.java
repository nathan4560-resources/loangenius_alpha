package my.utar.edu.loangenius;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Result extends AppCompatActivity {

    SharedPreferences verdict;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    //PRINCIPAL DIVIDE BY NUMBER OF REPAYMENTS
    public static double principal_paid(float m1,float m3){
        return m1 / m3;
    }
    //INTEREST PAID: PRINCIPAL / NUMBER OF REPAYMENTS * INTEREST RATE
    public static double interest_paid(float m1,float m2,float m3){
        return (m1/m3) *m2;
    }
    //MONTHLY REPAYMENTS
    public static double monthly_repayment(double principal_paid,double interest_paid){
        return principal_paid + interest_paid;
    }

    //REMANING LOAN
    public static double balance(float m1,float m3){
        return m1 - principal_paid(m1,m3);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        verdict =  getSharedPreferences("initializeloan",MODE_PRIVATE);
        //PRINCIPAI
       Float m1 = verdict.getFloat("loanprincipal",0);
       //INTEREST RATE
       Float m2 = verdict.getFloat("loanpercentage",0);
       //NUMBER OF REPAYMENTS
       Integer m3 = verdict.getInt("loanrepaymentcounter",0);

       //WHEN DOES THE LOAN START
       Integer m4 = verdict.getInt("loanstartingmonths",0);
       Integer m5 = verdict.getInt("loanstartingyears",0);
       //RETURN 1 FOR PERSONAL LOAN, 2 FOR HOUSING LOAN
       Integer m6 = verdict.getInt("loanpaymentmodes",0);
       //FOR DEBUGGING PURPOSES
    Log.i("Result","Logged: " + m1);
    Log.i("Result","Logged: " + m2);
    Log.i("Result","Logged: " + m3);
    Log.i("Result","Logged: " + m4);
    Log.i("Result","Logged: " + m5);
    Log.i("Result","Logged: " + m6);


    }
}