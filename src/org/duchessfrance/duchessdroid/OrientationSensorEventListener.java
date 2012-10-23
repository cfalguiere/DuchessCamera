package org.duchessfrance.duchessdroid;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.KeyEvent;

public class OrientationSensorEventListener implements SensorEventListener {

    private static final String TAG = OrientationSensorEventListener.class.getSimpleName();

    DuchessDroidActivity activity;
    
    private float mAzimuth; 
    private float[] mGravs = new float[3]; 
    private float[] mGeoMags = new float[3]; 
    private float[] mOrientation = new float[3]; 
    private float[] mRotationM = new float[9];    //9           // Use [16] to co-operate with android.opengl.Matrix 
    private float[] mRemapedRotationM = new float[9]; 

	public OrientationSensorEventListener(DuchessDroidActivity pActivity){
		activity = pActivity;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) { 
		case Sensor.TYPE_ACCELEROMETER: 
			/* 
			 * NOTE: The data must be copied off the event.values 
			 * as the system is reusing that array in all SensorEvents. 
			 * Simply assigning: 
			 * mGravs = event.values won't work. 
			 * 
			 * I use a member array in an attempt to reduce garbage production. 
			 */ 
			System.arraycopy(event.values, 0, mGravs, 0, 3); 
			break; 
		case Sensor.TYPE_MAGNETIC_FIELD: 
			// Here let's try another way: 
			for (int i=0;i<3;i++) mGeoMags[i] = event.values[i]; 
			break; 
		default: 
			return; 
		} 
		
		if (SensorManager.getRotationMatrix(mRotationM, null, mGravs, 
				mGeoMags)){ 
			//              Rotate to the camera's line of view (Y axis along the camera's axis) 
			//              TODO: Find how to use android.opengl.Matrix to rotate to an arbitrary coordinate system. 
			SensorManager.remapCoordinateSystem(mRotationM, 
					SensorManager.AXIS_X, 
					SensorManager.AXIS_Z, mRemapedRotationM); 
			SensorManager.getOrientation(mRemapedRotationM, mOrientation); 
			//              Convert the azimuth to degrees in 0.5 degree resolution. 
			mAzimuth = (float)Math.toDegrees(mOrientation[0]); 
			//              Adjust the range: 0 < range <= 360 (from: -180 < range <= 180). 
			mAzimuth = (mAzimuth+360)%360; // alternative: 
			//mAzimuth = mAzimuth>=0 ? mAzimuth : mAzimuth+360; 
		}
		
		Log.d(TAG, "Azimuth: " + mAzimuth);
		activity.duchess.setRotation(mAzimuth);
		activity.mDrawView.invalidate();
	}

	// TODO complete
/*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_CAMERA) {
	        if (!mIsPictureTaking) {
	            this.onClick(null);
	            return true;
	        }else{
	            //ignore buttons while taking pics.
	            return true;
	        }
	    }else{
	        return super.onKeyDown(keyCode, event);
	    }
	}
	*/
}
