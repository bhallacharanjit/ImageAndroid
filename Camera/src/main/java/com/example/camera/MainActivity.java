package com.example.camera;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	SensorManager mSensorManager;
	Sensor accSensor;
	Sensor magnetSensor;
	float[] gravity;
	 float[] geoMagnetic;
	  float azimut,pitch,roll;
	  Button btn1,btn2,btn3;
	  int CAMERA_PIC_REQUEST = 2;
	  ImageView image1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
         accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
         magnetSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
         
         //I am using only this thing
btn1=(Button) findViewById(R.id.button1);
btn2=(Button) findViewById(R.id.button2);
btn3=(Button)findViewById(R.id.button3);
 image1=(ImageView) findViewById(R.id.image1);
image1.setImageResource(R.drawable.aman);
final EditText text1=(EditText) findViewById(R.id.editText1);
Toast.makeText(getApplicationContext(),"Anticlockwise use '-angle' and clokwise directon use 'angle' only", Toast.LENGTH_LONG).show();
    btn1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get an image from the camera
        
        	/*
        	Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            // request code

            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        	
            double sensorHeight=1.6;
			float dist = Math.abs((float) (1.4f * Math.tan(pitch * Math.PI / 180)));
			Toast.makeText(
                    getApplicationContext(),
                    "Distance = "
                            + String.valueOf(dist)
                                    + "m  Angle = "
                                    + String.valueOf(Math.toRadians(Math.abs(pitch))),
                    Toast.LENGTH_LONG).show();
*/
        	final String angle=text1.getText().toString();
        	Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aman);
        	Bitmap scaledBitmap;
        	// find the width and height of the screen:
        	Display d = getWindowManager().getDefaultDisplay();
        	int x = d.getWidth();
        	int y = d.getHeight();
        	 
        	// get a reference to the ImageView component that will display the image:
        
        	
        
        	 scaledBitmap = Bitmap.createScaledBitmap(myBitmap, y, x, true);
        	
        		
        	// create a matrix object
        	Matrix matrix = new Matrix();
        	matrix.postRotate( Integer.parseInt(angle)); 
        	 
        	// create a new bitmap from the original using the matrix to transform the result
        	Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
        	 
        	// display the rotated bitmap
        	image1.setImageBitmap(rotatedBitmap);
        	
        	

        }
    });
btn2.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		image1.setImageResource(R.drawable.aman);
	}
});
  
btn3.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	
		Intent intent1=new Intent(getApplicationContext(),CropActivity.class);
		startActivity(intent1);
	}
});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	    if( requestCode == CAMERA_PIC_REQUEST)
	    {
	    //  data.getExtras()
	        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
	        ImageView image =(ImageView) findViewById(R.id.image1);
	        image.setImageBitmap(thumbnail);
	    }
	    else 
	    {
	        Toast.makeText(getApplicationContext(), "Picture NOt taken", Toast.LENGTH_LONG);
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}
protected void onResume() {
    super.onResume();
    mSensorManager.registerListener(this, accSensor,
            SensorManager.SENSOR_DELAY_NORMAL);
    mSensorManager.registerListener(this, magnetSensor,
            SensorManager.SENSOR_DELAY_NORMAL);
}
		
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
	
		// TODO Auto-generated method stub
		 if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		        gravity = event.values.clone();
		   
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		        geoMagnetic = event.values.clone();
		    if (gravity != null && geoMagnetic != null) {
		        float R[] = new float[9];
		        float I[] = new float[9];
		        boolean success = SensorManager.getRotationMatrix(R, I, gravity,
		                geoMagnetic);
		        if (success) {
		            /* Orientation has azimuth, pitch and roll */
		            float orientation[] = new float[3];
		            //SensorManager.remapCoordinateSystem(R, 1, 3, orientation);
		            SensorManager.getOrientation(R, orientation);
		             azimut = 57.29578F * orientation[0];
		            pitch = 57.29578F * orientation[1];
		            roll = 57.29578F * orientation[2];
		        }
		    }
	}

	
	}

