package org.duchessfrance.duchessdroid;
	

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// TODO
/*
Note: A camera preview does not have to be in landscape mode. Starting in Android 2.2 (API Level 8), you can use the setDisplayOrientation() method to set the rotation of the preview image. In order to change preview orientation as the user re-orients the phone, within the surfaceChanged() method of your preview class, first stop the preview with Camera.stopPreview() change the orientation and then start the preview again with Camera.startPreview().
*/
public class PreviewView extends SurfaceView implements SurfaceHolder.Callback{
	private static final String TAG = PreviewView.class.getSimpleName();

	SurfaceHolder mHolder;
	Camera mCamera;

	boolean mIsPreviewRunning = false;
	public  boolean isScanning = false;

	public  boolean isEditingEtalan = false;

	public static final int MEDIA_TYPE_IMAGE = 1;


	DrawView mTargetView;
	MaskView mMaskView;

	public PreviewView(Context context, AttributeSet attrs) {
		super(context,attrs);
		mHolder = getHolder();
		mHolder.addCallback(this);
		//mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}



	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
		try {
			mCamera.setPreviewDisplay(holder);
			mIsPreviewRunning=true;
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mIsPreviewRunning=false;
		mCamera.release();
		mCamera = null;
	}


	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

		if (mHolder.getSurface() == null){
			// preview surface does not exist
			return;
		}

		// stop camera preview
		if (mIsPreviewRunning) {
			mCamera.stopPreview();
			mIsPreviewRunning=false;
		}

		// restart camera preview
		Camera.Parameters parameters = mCamera.getParameters();
		Size p = mCamera.getParameters().getPreviewSize();
		mMaskView.setPreviewSize(p);
		mMaskView.invalidate();
		//drawMask(holder, p, w, h);

		// work fine for display but captured image is not rotated 
		// and also lags when rotating device*/
		/*
        Display display = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if(display.getRotation() == Surface.ROTATION_0)
        {
            parameters.setPreviewSize(h, w);                           
            mCamera.setDisplayOrientation(90);
        }

        if(display.getRotation() == Surface.ROTATION_90)
        {
            parameters.setPreviewSize(w, h);                           
        }

        if(display.getRotation() == Surface.ROTATION_180)
        {
            parameters.setPreviewSize(h, w);               
        }

        if(display.getRotation() == Surface.ROTATION_270)
        {
            parameters.setPreviewSize(w, h);
            mCamera.setDisplayOrientation(180);
        } */


		mTargetView.invalidate();
		mCamera.setParameters(parameters);
		previewCamera();
	}
	
	private void previewCamera()
	{        
	    try 
	    {           
	        mCamera.setPreviewDisplay(mHolder);          
	        mCamera.startPreview();
	        mIsPreviewRunning = true;
	    }
	    catch(Exception e)
	    {
	        Log.d(TAG, "Cannot start preview", e);    
	    }
	}
/*
	private void drawMask(SurfaceHolder holder, Size photoSize, int w, int h) {
		if(holder.getSurface().isValid()){
			Canvas canvas = holder.lockCanvas();
			if (canvas!=null) {
				Paint maskPaint = new Paint();
				maskPaint.setColor(Color.rgb(0, 0, 0));
				canvas.drawRect(photoSize.width, 0, w-photoSize.width, h, maskPaint);
			}

			holder.unlockCanvasAndPost(canvas);
		}

	}

*/
	
	public void takePicture(PictureCallback mJpegPictureCallback) {
		mCamera.takePicture(null, null, mJpegPictureCallback);
	}

}
