package my.utar.edu.loangenius;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.lang.*;
import java.math.*;

public class Result extends AppCompatActivity {


    SharedPreferences verdict;


    public static DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        verdict = getSharedPreferences("initializeloan", MODE_PRIVATE);
        //BEGINNING BALANCE
        Float m1 = verdict.getFloat("loanprincipal", 0);
        //INTEREST RATE
        Float m2 = verdict.getFloat("loanpercentage", 0);
        //NUMBER OF REPAYMENTS
        Integer m3 = verdict.getInt("loanrepaymentcounter", 0);

        //WHEN DOES THE LOAN START
        Integer m4 = verdict.getInt("loanstartingmonths", 0);
        Integer m5 = verdict.getInt("loanstartingyears", 0);
        //RETURN 1 FOR PERSONAL LOAN, 2 FOR HOUSING LOAN
        Integer mode = verdict.getInt("loanpaymentmodes", 0);
        ListView listView = findViewById(R.id.generalView);
        //BOTH ALGORITHM DECLARATION
        float principal = m1;
        float monthlyRate = m2;
        Integer paymode = mode;
        Integer loanTenureMonths = m3;
        ArrayList<Integer> generalPaymentCounter = new ArrayList<Integer>();
        ArrayList<Double> generalBeginningBalance = new ArrayList<Double>();
        ArrayList<Double> generalMonthlyRepayment = new ArrayList<Double>();
        ArrayList<Double> generalInterestPaid = new ArrayList<Double>();
        ArrayList<Double> generalPrincipalPaid = new ArrayList<Double>();




        //PAYMODE 1
        double personalMonthlyInstalment =(principal * 1+(monthlyRate/100)) / loanTenureMonths;
        double personalPrincipalPay = principal * (1 + (monthlyRate /100 / 12 * 12) / loanTenureMonths);
        double personalInterestRate = personalMonthlyInstalment - personalPrincipalPay;

        //PAYMODE 2
        double monthlyInterestRate = monthlyRate / 12 / 100;
        double monthlyInstallment = calculateMonthlyInstallment(principal, monthlyInterestRate, loanTenureMonths);
        //PAYMODE BASED LIST


        switch (paymode) {
            case 1:
                List<PersonalLoan> calculatePersonalLoan = new ArrayList<>();
                break;
            case 2:
                List<AmortizationSchedule> calculateHousingLoan = new ArrayList<>();
                break;
        }
        if (paymode == 1) {

           List<PersonalLoan> personalSchedule = calculatePersonalLoan(principal,monthlyInterestRate, personalMonthlyInstalment, loanTenureMonths, monthlyRate);

            for (PersonalLoan schedule : personalSchedule) {
                Log.i("Result", schedule.toString());
                //use schedule.<getter> to retrive something precious
                /*
                USAGE:
                PERSONAL PAYMENT NUMBER = schedule.getPersonalPaymentNumber();
                PERSONAL BEGINNING BALANCE = schedule.getPersonalBeginningBalance();
                PERSONAL MONTHLY REPAYMENT = schedule.getPersonalMonthlyRepayment();
                PERSONAL INTEREST PAID = schedule.getPersonalInterestPaid();
                PERSONAL PRINCIPAL PAID = schedule.getPersonalPrincipalPaid();
                */
                generalPaymentCounter.add(schedule.getPersonalPaymentNumber());
                generalBeginningBalance.add(schedule.getPersonalBeginningBalance());
                generalMonthlyRepayment.add(schedule.getPersonalMonthlyRepayment());
                generalInterestPaid.add(schedule.getPersonalInterestPaid());
                generalPrincipalPaid.add(schedule.getPersonalPrincipalPaid());

            }

        } else if (paymode == 2) {
            List<AmortizationSchedule> amortizationSchedule = calculateAmortizationSchedule(principal, monthlyInterestRate, monthlyInstallment, loanTenureMonths);

            for (AmortizationSchedule schedule : amortizationSchedule) {
                Log.i("Result", schedule.toString());
                //use schedule.<getter> to retrive something precious
                /*
                USAGE:
                PERSONAL PAYMENT NUMBER = schedule.getHousingPaymentNumber();
                PERSONAL BEGINNING BALANCE = schedule.getHousingRemainingBalance();
                PERSONAL MONTHLY REPAYMENT = schedule.getHousingMonthlyRepayment();
                PERSONAL INTEREST PAID = schedule.getHousingInterestPaid();
                PERSONAL PRINCIPAL PAID = schedule.getHousingPrincipalPaid();
                */
                generalPaymentCounter.add(schedule.getHousingPaymentNumber());
                generalBeginningBalance.add(schedule.getHousingRemainingBalance());
                generalMonthlyRepayment.add(schedule.getHousingMonthlyRepayment());
                generalInterestPaid.add(schedule.getHousingInterestPaid());
                generalPrincipalPaid.add(schedule.getHousingPrincipalPaid());
            }
        }
        ArrayList<Double> combinedGeneralList = new ArrayList<>();
        combinedGeneralList.addAll(generalBeginningBalance);
        combinedGeneralList.addAll(generalMonthlyRepayment);
        combinedGeneralList.addAll(generalInterestPaid);
        combinedGeneralList.addAll(generalPrincipalPaid);
        ArrayAdapter<Double> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,combinedGeneralList);
        listView.setAdapter(adapter);

    }//ANDROID PROCESS

    public static double calculatePersonalLoan(double principal,int loanTenureMonths,double personalMonthlyInstalment,double personalPrincipalPay,float monthlyRate){
        return principal * (1 + monthlyRate /100 / 12 * 12) / loanTenureMonths;
    }
    public static List<PersonalLoan> calculatePersonalLoan(double principal,double personalInterestRate,double personalMonthlyInstalment,int loanTenureMonths,float monthlyRate) {
        List<PersonalLoan> personalSchedule = new ArrayList<>();
        double balance = principal;
        //PRINCIPAL PAID
        personalMonthlyInstalment =principal * (1 + monthlyRate /100 / 12 * 12) / loanTenureMonths;
        double personalPrincipalPay = (principal * 1+personalInterestRate/100) / loanTenureMonths;
        personalInterestRate = personalMonthlyInstalment - personalPrincipalPay;

        for (int i = 1; i <= loanTenureMonths; i++) {
            balance -=personalPrincipalPay;
            personalSchedule.add(new PersonalLoan(i,balance,personalMonthlyInstalment,personalInterestRate,personalPrincipalPay));
        }
        return personalSchedule;
    }


    static class PersonalLoan {
        int paymentNumber;
        double beginningBalance;
        double monthlyRepayment;
        double interestPaid;
        double principalPaid;

        public PersonalLoan(int paymentNumber, double beginningBalance, double monthlyRepayment, double interestPaid, double principalPaid) {
            this.paymentNumber = paymentNumber;
            this.beginningBalance = beginningBalance;
            this.monthlyRepayment = monthlyRepayment;
            this.interestPaid = interestPaid;
            this.principalPaid = principalPaid;
        }
        //PERSONAL LOAN EXTRACTION
        public String toString() {
            return "Payment number: " + paymentNumber +
                    ", Remaining balance: " + df.format(beginningBalance) +
                    ", Monthly repayment: " + df.format(monthlyRepayment) +
                    ", Interest paid: " + df.format(interestPaid) +
                    ", Principal paid: " + df.format(principalPaid);
        }

        public int getPersonalPaymentNumber(){
            return paymentNumber;
        }
        public void setPersonalPaymentNumber(int paymentNumber){
            this.paymentNumber = paymentNumber;
        }

        public double getPersonalBeginningBalance(){
            if(Double.parseDouble(df.format(beginningBalance))<=0){
                return 0.0;
            }
            else{
                return Double.parseDouble(df.format(beginningBalance));
            }
        }
        public void setPersonalBeginningBalance(double getPersonalBeginningBalance){
            this.beginningBalance = beginningBalance;
        }

        public double getPersonalMonthlyRepayment(){
            return Double.parseDouble(df.format(monthlyRepayment));
        }
        public void setPersonalMonthlyRepayment(double monthlyRepayment){
            this.monthlyRepayment = monthlyRepayment;
        }

        public double getPersonalInterestPaid(){
            return Double.parseDouble(df.format(interestPaid));
        }
        public void setPersonalInterestPaid(double interestPaid){
            this.interestPaid = interestPaid;
        }

        public double getPersonalPrincipalPaid(){
            return Double.parseDouble(df.format(principalPaid));
        }
        public void setPersonalPrincipalPaid(double principalPaid){
            this.principalPaid = principalPaid;
        }
    }//END OF CLASS DEFINITION FOR PERSONAL LOAN


    //HOUSING LOAN
    public static double calculateMonthlyInstallment(double principal, double monthlyInterestRate, int loanTenureMonths) {
        double numerator = principal * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, loanTenureMonths);
        double denominator = Math.pow(1 + monthlyInterestRate, loanTenureMonths) - 1;
        return numerator / denominator;
    }

    public static List<AmortizationSchedule> calculateAmortizationSchedule(double principal, double monthlyInterestRate, double monthlyInstallment, int loanTenureMonths) {
        List<AmortizationSchedule> schedule = new ArrayList<>();
        double balance = principal;
        for (int i = 1; i <= loanTenureMonths; i++) {
            double interestPaid = balance * monthlyInterestRate;
            double principalPaid = monthlyInstallment - interestPaid;
            balance -= principalPaid;
            schedule.add(new AmortizationSchedule(i, balance, monthlyInstallment, interestPaid, principalPaid));
        }
        return schedule;
    }

    static class AmortizationSchedule {
        int paymentNumber;
        double beginningBalance;
        double monthlyRepayment;
        double interestPaid;
        double principalPaid;

        public AmortizationSchedule(int paymentNumber, double beginningBalance, double monthlyRepayment, double interestPaid, double principalPaid) {
            this.paymentNumber = paymentNumber;
            this.beginningBalance = beginningBalance;
            this.monthlyRepayment = monthlyRepayment;
            this.interestPaid = interestPaid;
            this.principalPaid = principalPaid;
        }//arraylist declaration


        //HOUSING LOAN EXTRACTION
        @Override
        public String toString() {
            return "Payment number: " + paymentNumber +
                    ", Remaining balance: " + df.format(beginningBalance) +
                    ", Monthly repayment: " + df.format(monthlyRepayment) +
                    ", Interest paid: " + df.format(interestPaid) +
                    ", Principal paid: " + df.format(principalPaid);
        }

        public int getHousingPaymentNumber(){
            return paymentNumber;
        }
        public void setHousingPaymentNumber(int paymentNumber){
            this.paymentNumber = paymentNumber;
        }

        public double getHousingRemainingBalance(){
            if(Double.parseDouble(df.format(beginningBalance))<=0){
                return 0.0;
            }
            else{
                return Double.parseDouble(df.format(beginningBalance));
            }
        }
        public void setHousingRemainingBalance(double beginningBalance){
            this.beginningBalance = beginningBalance;
        }
        public double getHousingMonthlyRepayment(){
            return Double.parseDouble(df.format(monthlyRepayment));
        }
        public void setHousingMonthlyRepayment(double monthlyRepayment){
            this.monthlyRepayment = monthlyRepayment;
        }

        public double getHousingInterestPaid(){
            return Double.parseDouble(df.format(interestPaid));
        }
        public void setHousingInterestPaid(double interestPaid){
            this.interestPaid = interestPaid;
        }

        public double getHousingPrincipalPaid(){
            return Double.parseDouble(df.format(principalPaid));
        }
        public void setHousingPrincipalPaid(double principalPaid){
            this.principalPaid = principalPaid;
        }
    }//STRUCT

}