package com.example.camera;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class CropActivity extends Activity {

	protected static final int RESULT_LOAD_IMAGE = 1;
	ImageView imVCature_pic;
	 Button btnCapture,btn1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop);
		initializeControls();
		
	}
	private void initializeControls() {
		  imVCature_pic=(ImageView)findViewById(R.id.imVCature_pic);
		  btnCapture=(Button)findViewById(R.id.btnCapture);
		  btn1=(Button) findViewById(R.id.button1);
		  btnCapture.setOnClickListener(new OnClickListener() {

		   @Override
		   public void onClick(View v) {
		    /* create an instance of intent
		     * pass action android.media.action.IMAGE_CAPTURE 
		     * as argument to launch camera
		     */
		    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		    /*create instance of File with name img.jpg*/
		    File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
		    /*put uri as extra in intent object*/
		    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		    /*start activity for result pass intent as argument and request code */
		    startActivityForResult(intent, 3);
		   }
		  });
btn1.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	
		Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         
        startActivityForResult(i, RESULT_LOAD_IMAGE)
        ;
	}
});
		 }

	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  //if request code is same we pass as argument in startActivityForResult
	  if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
          Uri selectedImage = data.getData();
          String[] filePathColumn = { MediaStore.Images.Media.DATA };

          Cursor cursor = getContentResolver().query(selectedImage,
                  filePathColumn, null, null, null);
          cursor.moveToFirst();

          int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
          String picturePath = cursor.getString(columnIndex);
          cursor.close();
           
          
          imVCature_pic.setImageBitmap(BitmapFactory.decodeFile(picturePath));

   	  // File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
   	//File imgFile = new  File("/sdcard/Images/test_image.jpg�);
   			Uri selectedImageURI = data.getData();
   		File imageFile = new File(getRealPathFromURI(selectedImageURI));
          cropCapturedImage(Uri.fromFile(imageFile));
	 }
	  if(requestCode==3){
	   //create instance of File with same name we created before to get image from storage
	   File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
	   //Crop the captured image using an other intent
	   try {
	    /*the user's device may not support cropping*/
	    cropCapturedImage(Uri.fromFile(file));
	   }
	   catch(ActivityNotFoundException aNFE){
	    //display an error message if user device doesn't support
	    String errorMessage = "Sorry - your device doesn't support the crop action!";
	    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
	    toast.show();
	   }
	  }
	  if(requestCode==2&&data!=null){
	   //Create an instance of bundle and get the returned data
	   Bundle extras = data.getExtras();
	   //get the cropped bitmap from extras
	   Bitmap thePic = extras.getParcelable("data");
	   //set image bitmap to image view
	   imVCature_pic.setImageBitmap(thePic);
	  }
	  
	 
	}
	 //create helping method cropCapturedImage(Uri picUri)
	 public void cropCapturedImage(Uri picUri){
	  //call the standard crop action intent 
	  Intent cropIntent = new Intent("com.android.camera.action.CROP");
	  //indicate image type and Uri of image
	  cropIntent.setDataAndType(picUri, "image/*");
	  //set crop properties
	  cropIntent.putExtra("crop", "true");
	  //indicate aspect of desired crop
	  cropIntent.putExtra("aspectX", 1);
	  cropIntent.putExtra("aspectY", 1);
	  //indicate output X and Y
	  cropIntent.putExtra("outputX", 256);
	  cropIntent.putExtra("outputY", 256);
	  //retrieve data on return
	  cropIntent.putExtra("return-data", true);
	  //start the activity - we handle returning in onActivityResult

	  startActivityForResult(cropIntent, 2);
	 }
	 private String getRealPathFromURI(Uri contentURI) {
		    String result;
		    Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
		    if (cursor == null) { // Source is Dropbox or other similar local file path
		        result = contentURI.getPath();
		    } else { 
		        cursor.moveToFirst(); 
		        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
		        result = cursor.getString(idx);
		        cursor.close();
		    }
		    return result;
		}
	}

