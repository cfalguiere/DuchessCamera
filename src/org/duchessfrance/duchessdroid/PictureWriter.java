package org.duchessfrance.duchessdroid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public class PictureWriter implements PictureCallback {

	private static final String TAG = PictureWriter.class.getSimpleName();

	private Activity mActivity;
	private Duchess mDuchess;
	private ImageView mResultView;

	public PictureWriter(Activity pActivity, Duchess pDuchess, ImageView pResultView) {
		mResultView = pResultView;
		mActivity = pActivity;
		mDuchess = pDuchess;
	}

	public void onPictureTaken(byte[] data, Camera camera) {

		Bitmap photo = BitmapFactory.decodeByteArray(data, 0,data.length);

		// Create a new, empty bitmap with the original size.
		Bitmap bitmap = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), 
				photo.getConfig());

		final Canvas canvas = new Canvas(bitmap);

		// draw the original image to the canvas
		canvas.drawBitmap(photo, 0, 0, null);	    	   

		// Draw Duchess view
		DisplayMetrics metrics = new DisplayMetrics();
		((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE))
		  .getDefaultDisplay().getMetrics(metrics);


		float ratio = (float) photo.getHeight() / (float) metrics.heightPixels;
		mDuchess.draw(canvas, ratio);

		try {
			File pictureFile = getOutputMediaFile();
			if (pictureFile == null){
				Log.d(TAG, "Error creating media file, check storage permissions: " );
				return;
			}

			Log.d(TAG, "saving bitmap to file " + pictureFile);
			FileOutputStream fos = new FileOutputStream(pictureFile);

			if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)) {
				Log.d(TAG, "Could not write bitmap to file");
			}

			fos.flush();
			fos.close();

			addPictureToGalery(pictureFile.getPath());
			
		} catch (FileNotFoundException e) {
			Log.d(TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, "Error accessing file: " + e.getMessage());
		} catch (TechnicalException e) {
			Log.d(TAG, "TechnicalError: " + e.getMessage());
		}

		canvas.scale(0.2f, 0.2f, bitmap.getWidth()/2, bitmap.getHeight()/2);

		mResultView.setImageBitmap(bitmap);
		mResultView.invalidate();
		//TODO custom view to handle the on touch to close view or animate the move in gallery

	}



	/** Create a File for saving the image */
	private static File getOutputMediaFile() throws TechnicalException {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "DuchessCamera");
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				String message = "failed to create directory " + mediaStorageDir;
				Log.d("DuchessCamera", message);
				throw new TechnicalException(TAG, message);
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator +
				"IMG_"+ timeStamp + ".jpg");
		return mediaFile;
	}

	private void addPictureToGalery(String path) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		mActivity.sendBroadcast(mediaScanIntent);
	}


};
