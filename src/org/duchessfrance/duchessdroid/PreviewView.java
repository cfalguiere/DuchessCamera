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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

// TODO
/*
Note: A camera preview does not have to be in landscape mode. Starting in Android 2.2 (API Level 8), you can use the setDisplayOrientation() method to set the rotation of the preview image. In order to change preview orientation as the user re-orients the phone, within the surfaceChanged() method of your preview class, first stop the preview with Camera.stopPreview() change the orientation and then start the preview again with Camera.startPreview().
*/
public class PreviewView extends SurfaceView implements SurfaceHolder.Callback{
	private static final String TAG = PreviewView.class.getSimpleName();

   SurfaceHolder mHolder;
   Camera mCamera;
   
   boolean mPreviewRunning =false;
  public  boolean isScanning =false;

  public  boolean isEditingEtalan =false;
  
  private Uri imageUri;
  public static final int MEDIA_TYPE_IMAGE = 1;
  private static final int IMAGE_CAPTURE = 1234; 


  DrawView mTargetView;

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
          mPreviewRunning=true;
       } catch (IOException exception) {
           mCamera.release();
           mCamera = null;
       }
   }

   public void surfaceDestroyed(SurfaceHolder holder) {
       mCamera.stopPreview();
       mPreviewRunning=false;
       mCamera.release();
       mCamera = null;
   }

   
   public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

       if (mHolder.getSurface() == null){
         // preview surface does not exist
         return;
       }

       // stop camera preview
       if (mPreviewRunning) {
			mCamera.stopPreview();
			mPreviewRunning=false;
	   }
      
       // restart camera preview
       Camera.Parameters parameters = mCamera.getParameters();
       mTargetView.invalidate();
       mCamera.setParameters(parameters);
       mCamera.startPreview();
       
       mPreviewRunning=true;
   }

  

   public void takePicture(PictureCallback mJpegPictureCallback) {
	   mCamera.takePicture(null, null, mJpegPictureCallback);
   }

}
