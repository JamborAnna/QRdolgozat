package com.example.qrdolgozat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button btnScan,filbaIrat;
    private TextView textEredmeny;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("QR Code Scaning by app");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan(); // aktiválás
            }

        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Kiléptél a scannelésből!", Toast.LENGTH_SHORT).show();
            } else {
                textEredmeny.setText(" " + result.getContents());




                // ha van benne link akkor menjen rá

               /* Uri uri = Uri.parse(result.getContents());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);*/
            }
            filbaIrat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    file=new File(Environment.getExternalStorageDirectory(),("scanneldCodes.csv"));
                    try{

                        Date date = Calendar.getInstance().getTime();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss ");
                        String formatedDate = dateFormat.format(date);
                        String text =  formatedDate+ "," +  textEredmeny.getText().toString()+ "," + "\r\n";
                   /* PrintWriter writer= new PrintWriter(file);
                    writer.println(result.toString()+"\n\t");
                    writer.close();*/
                        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(file,true),1024);
                        bufferedWriter.write(String.valueOf(text));
                        Toast.makeText(MainActivity.this, "Kiírtatás sikeres volt!", Toast.LENGTH_SHORT).show();
                        bufferedWriter.close();
                    }catch (IOException e){

                        e.printStackTrace();

                    }
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);


    }




    private void init() {
        btnScan = findViewById(R.id.btnScan);
        textEredmeny = findViewById(R.id.textEredmeny);
        filbaIrat= findViewById(R.id.filbaIrat);
    }
}
