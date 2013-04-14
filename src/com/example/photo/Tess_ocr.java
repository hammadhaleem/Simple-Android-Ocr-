package com.example.photo;

import java.io.IOException;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.googlecode.tesseract.android.TessBaseAPI;
public class Tess_ocr extends  Activity{
	public static final String PACKAGE_NAME = "com.example.photo";
	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/photo/";
	public static final String lang = "eng";
	public static final String TAG = "MainActivity.java";
	protected Button _button;
	protected Button button1 ;
	protected ImageView _image;
	protected EditText _field;
	private  String _path;
	protected boolean _taken;
	protected static final String PHOTO_TAKEN = "photo_taken";

	protected void onPhotoTaken(String Path) {
		_taken = true;
		_path=Path;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		System.out.println("Path for file is "+_path );
		Bitmap bitmap = BitmapFactory.decodeFile(_path, options);

		try {
			ExifInterface exif = new ExifInterface(_path);
			int exifOrientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			Log.v(TAG, "Orient: " + exifOrientation);

			int rotate = 0;

			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}

			Log.v(TAG, "Rotation: " + rotate);

			if (rotate != 0) {

				int w = bitmap.getWidth();
				int h = bitmap.getHeight();
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}

			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

		} catch (IOException e) {
			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
		}
		

		 _image.setImageBitmap( bitmap );
		
		Log.v(TAG, "Before baseApi");

		TessBaseAPI baseApi = new TessBaseAPI();
		baseApi.setDebug(true);
		baseApi.init(DATA_PATH, lang);
		baseApi.setImage(bitmap);
		
		String recognizedText = baseApi.getUTF8Text();
		
		baseApi.end();


		Log.v(TAG, "OCRED TEXT: " + recognizedText);

		if ( lang.equalsIgnoreCase("eng") ) {
			recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
		}
		
		recognizedText = recognizedText.trim();

		if ( recognizedText.length() != 0 ) {
			_field.setText(_field.getText().toString().length() == 0 ? recognizedText : _field.getText() + " " + recognizedText);
			_field.setSelection(_field.getText().toString().length());
		}
		
	}
	
	
}
