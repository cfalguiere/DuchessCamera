package org.duchessfrance.duchessdroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;

public class DuchessDroidActivity extends Activity {
	private static final String TAG = DuchessDroidActivity.class.getSimpleName();

	DrawView mDrawView;
	PreviewView mPreviewView;
	Duchess duchess;
 
    private static SensorManager sensorManager;
    private OrientationSensorEventListener osel;
    


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
    	
    	setupSensor();
    	
    	// create duchess and load bitmap
    	duchess = new Duchess(
    			BitmapFactory.decodeResource(getResources(), R.drawable.duchessfr), 
    			width*2/3, height*2/3); //TODO actual size of the screen


    	mDrawView = (DrawView) findViewById(R.id.draw_view);
    	mDrawView.duchess = duchess;

    	mPreviewView = (PreviewView) findViewById(R.id.preview_view);
    	mPreviewView.mTargetView = mDrawView;
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
    	//sensorManager.unregisterListener(this, sensor);
    	sensorManager.unregisterListener(osel);
    }

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();

    		sensorManager.unregisterListener(osel);
    } 
    
    private void setupSensor() {
    	sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    	sensorManager.registerListener(osel, 
    			sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), // Anonymous Sensors- no further use for them. 
    			SensorManager.SENSOR_DELAY_UI); 
    	sensorManager.registerListener(osel, 
    			sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
    			SensorManager.SENSOR_DELAY_UI); 
    }
}