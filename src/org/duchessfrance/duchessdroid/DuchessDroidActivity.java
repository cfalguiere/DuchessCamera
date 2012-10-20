package org.duchessfrance.duchessdroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.MediaStore;
import android.net.Uri;

import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.widget.ImageView;

public class DuchessDroidActivity extends Activity {
    DrawView mDrawView;
    PreviewView mPreviewView;
    
    private Duchess duchess;

    private static final String TAG = DuchessDroidActivity.class.getSimpleName();

/*    CameraPreview mCameraPreview;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Uri fileUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //create new Intent
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);  // create a file to save the video
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name

        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

        // start the Video Capture Intent
        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }
*/
    
    /** Called when the activity is first created. */
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "Entering onCreate");
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

        // create duchess and load bitmap
        duchess = new Duchess(
        		BitmapFactory.decodeResource(getResources(), R.drawable.duchessfr), 
        		width*2/3, height*2/3); //TODO actual size of the screen
        

		mDrawView = (DrawView) findViewById(R.id.draw_view);
		mDrawView.duchess = duchess;
		
		mPreviewView = (PreviewView) findViewById(R.id.preview_view);
		mPreviewView.mTargetView = mDrawView;
		//mPreviewView.mTargetView = iv;
		//preview.addView(cp); //cp is a reference to a camera preview object
		//preview.addView(iv);
	
	}

}