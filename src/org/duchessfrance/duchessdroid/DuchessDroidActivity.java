package org.duchessfrance.duchessdroid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DuchessDroidActivity extends Activity {
	private static final String TAG = DuchessDroidActivity.class.getSimpleName();

	DrawView mDrawView;
	PreviewView mPreviewView;
	MaskView mMaskView;
	Duchess duchess;
    private ImageView imageView;

 
    private static SensorManager sensorManager;
    private OrientationSensorEventListener osel;
    
    //private GestureDetector gestureDetector;
    //private View.OnTouchListener gestureListener;

    
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
    	
    	setupSensor();

    	//TODO move to context
		DisplayMetrics metrics = new DisplayMetrics();
		((WindowManager) getSystemService(Context.WINDOW_SERVICE))
		  .getDefaultDisplay().getMetrics(metrics);
		int screenHeightPixels = metrics.heightPixels;
		int screenWidthPixels = metrics.widthPixels;

		Camera camera = Camera.open();
		Camera.Parameters parameters = camera.getParameters();
		Size photoSize = parameters.getPreviewSize();
		int photoWidth = photoSize.width;
		int photoHeight = photoSize.height;

		float xScale = ((float) screenWidthPixels) / photoWidth;
	    float yScale = ((float) screenHeightPixels) / photoHeight;
	    float scale = (xScale <= yScale) ? xScale : yScale;


      // Now change Frame dimensions to match the scaled image
	    
    	FrameLayout frame = (FrameLayout) findViewById(R.id.camera_frame);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frame.getLayoutParams();
        params.width = Math.round(photoWidth * scale);
        params.height = Math.round(photoHeight * scale);;
        frame.setLayoutParams(params);

    	// create duchess and load bitmap
    	duchess = new Duchess(
    			BitmapFactory.decodeResource(getResources(), R.drawable.duchessfr), 
    			width*1/3, height*2/3); 

		// Draw Duchess view
        
    	mDrawView = (DrawView) findViewById(R.id.draw_view);
    	mDrawView.duchess = duchess;
    	
    	mPreviewView = (PreviewView) findViewById(R.id.preview_view);
    	mPreviewView.mTargetView = mDrawView;
    	mPreviewView.mCamera = camera;
    	mPreviewView.mMaskView  = (MaskView) findViewById(R.id.preview_mask_view);



    	mMaskView = (MaskView) findViewById(R.id.preview_mask_view);
    	

         // Gesture detection
    	/*
        gestureDetector = new GestureDetector(new ScaleListener());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
            	Log.d(TAG, "received gesture in activity");
                return gestureDetector.onTouchEvent(event);
            }
        };*/
    	imageView = (ImageView) findViewById(R.id.img);
 
    }


    /** Register for the updates when Activity is in foreground */
    @Override
    protected void onResume() {
    	super.onResume();
    	Log.d(TAG, "onResume");
    	setupSensor(); 
    }

    /** Stop the updates when Activity is paused */
    @Override
    protected void onPause() {
    	super.onPause();
    	Log.d(TAG, "onPause");
    	sensorManager.unregisterListener(osel);
    }

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	sensorManager.unregisterListener(osel);
    } 
    
    public void whenSave(View view) {
        // get an image from the camera
    	try {
    		PictureWriter mJpegPictureCallback = new PictureWriter(this, duchess, imageView);
     		mPreviewView.takePicture(mJpegPictureCallback);
    	    fireSavedAlert();
    	} catch (Exception e) {
    		Log.d(TAG, "could not save picture");
    	}
     	
    	
     }


    
    public void whenScaleDown(View view) {
    	float currentScale = duchess.getScale();
    	if (currentScale>1) {
    		duchess.setScale(currentScale - 0.5f);
    		mDrawView.invalidate();
    	}
    }

    public void whenScaleUp(View view) {
    	float currentScale = duchess.getScale();
    	if (currentScale<4) {
    		duchess.setScale(currentScale + 0.5f);
    		mDrawView.invalidate();
    	}
    }

    public void whenRotate(View view) {
    	mDrawView.setMode(DrawView.Action.ROTATE);
   		mDrawView.invalidate();
    }

    public void whenDrag(View view) {
    	mDrawView.setMode(DrawView.Action.DRAG);
   		mDrawView.invalidate();
   }

    // unused
    private void setupSensor() {
    	sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    	sensorManager.registerListener(osel, 
    			sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), // Anonymous Sensors- no further use for them. 
    			SensorManager.SENSOR_DELAY_UI); 
    	sensorManager.registerListener(osel, 
    			sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
    			SensorManager.SENSOR_DELAY_UI); 
    }
    

	private void fireSavedAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.dialog_saved_message);
		builder.setTitle(R.string.dialog_saved_title);
		builder.setNeutralButton(R.string.button_text_ok, 
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});       
		AlertDialog dialog = builder.create();
		dialog.show();

	}


}