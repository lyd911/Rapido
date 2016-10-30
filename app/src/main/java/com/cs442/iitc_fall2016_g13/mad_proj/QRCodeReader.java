package com.cs442.iitc_fall2016_g13.mad_proj;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class QRCodeReader extends AppCompatActivity {


    private BarcodeDetector mBarcodeDetector;
    private CameraSource mCameraSource;
    private SurfaceView mCameraView;
    private TextView mTVBarcodeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_reader);


        mCameraView = (SurfaceView) findViewById(R.id.camera_view);
        mTVBarcodeInfo = (TextView) findViewById(R.id.tv_QRCodeInfo);

        mBarcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        mCameraSource = new CameraSource
                .Builder(this, mBarcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .build();

        mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mCameraSource.start(mCameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });

        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    mTVBarcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            mTVBarcodeInfo.setText(    // Update the TextView
                                    barcodes.valueAt(0).displayValue
                            );
                        }
                    });
                }
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();
        mCameraSource.release();
        mBarcodeDetector.release();
    }

}
