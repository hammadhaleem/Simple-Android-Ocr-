package com.example.photo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class PhotoPicker extends  Tess_ocr {
	public static int RESULT_LOAD_IMAGE = 1;

	   @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	         
	        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	            Cursor cursor = getContentResolver().query(selectedImage,
	                    filePathColumn, null, null, null);
	            cursor.moveToFirst();
	 
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String picturePath = cursor.getString(columnIndex);
	            cursor.close();
	             
	         //   ImageView imageView = (ImageView) findViewById(R.id.imageView1);
	         //   imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
	            onPhotoTaken(picturePath);
	        }
	     
	     
	    }
}


