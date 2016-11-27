package com.cs442.iitc_fall2016_g13.mad_proj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cs442.iitc_fall2016_g13.mad_proj.Map_alert.MapsActivity_direction;
import com.cs442.iitc_fall2016_g13.mad_proj.ServerConnect.GlobalVariables;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCodeGenerator extends Activity {


    private Button mGenerateButton, Navigate;
    private EditText mEditTextQRCodeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);

        Intent getData = getIntent();
        String billNumber = getData.getStringExtra("BILL#");

        final Context context = this;


        Navigate=(Button)findViewById(R.id.navigation_button);
        Navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MapsActivity_direction.class);
                v.getContext().startActivity(i);
            }
        });

        mGenerateButton = (Button) this.findViewById(R.id.btn_QRCodeGenerate);
        mGenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text2Qr = GlobalVariables.orderID_QR;
                if(text2Qr.length() > 0){

                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,200,200);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        Intent intent = new Intent(context, QRActivity.class);
                        intent.putExtra("pic",bitmap);
                        context.startActivity(intent);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
