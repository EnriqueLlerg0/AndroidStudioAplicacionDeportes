package com.example.registromisdeportes;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

public class CuentraAtrasConometro extends AsyncTask<String, String, String>
{
    TextView textView;
    EditText textView2;
    String numactual;
    String palabra="";
    Spinner spinner;
    Button button;

    public CuentraAtrasConometro(TextView textView, String numactual, EditText textView2, Spinner spinner, Button button)
    {
        this.textView=textView;
        this.numactual=numactual;
        this.textView2=textView2;
        this.spinner=spinner;
        this.button=button;
    }

    @Override
    protected String doInBackground(String... strings) {
        String[] num=numactual.split(":");
        int num1=Integer.parseInt(num[0]);
        Log.d("num1",""+num1);
        int num2=Integer.parseInt(num[1]);
        Log.d("num2",""+num2);

        while(num1>=0)
        {
            num2--;
            if(num2==0)
            {
                num1--;
                num2=59;
            }
            palabra=num1+":"+num2;
            if(num1<0)
            {
                palabra="00:00";
            }
            publishProgress(palabra);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        return null;

    }

    @Override
    protected void onProgressUpdate(String... values) {
        Log.d("tiempo",""+palabra);
        textView.setText(palabra);
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d("entro","si");
        textView2.setEnabled(true);
        spinner.setEnabled(true);
        button.setEnabled(true);


    }
}
