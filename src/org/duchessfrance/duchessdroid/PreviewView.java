package org.duchessfrance.duchessdroid;
	

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

public class PreviewView extends SurfaceView implements SurfaceHolder.Callback{
	//création de  mes objets mHolder et mCamera
    SurfaceHolder mHolder;
   Camera mCamera;
   boolean mPreviewRunning =false;
  public  boolean isScanning =false;

  public  boolean isEditingEtalan =false;
  
  DrawView mTargetView;
  //ImageView mTargetView;

     public PreviewView(Context context, AttributeSet attrs) {
       super(context,attrs);
       mHolder = getHolder();
       mHolder.addCallback(this);
       mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
   }
     

   
   public void surfaceCreated(SurfaceHolder holder) {
       //ouverture de ma camera
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
       //arret de ma camera quand 
	   
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
	   if (mPreviewRunning) {
			mCamera.stopPreview();
			mPreviewRunning=false;
	   }
        Camera.Parameters parameters = mCamera.getParameters();
      
//      try{
//     Camera.Size s = parameters.getSupportedPreviewSizes().get(0);
//       if(s==null){
//    	   parameters.setPreviewSize(w, h);
//       }else{
//    	   parameters.setPreviewSize(s.width, s.height);
//       }
//      }catch (Exception e) {
//    	  parameters.setPreviewSize(w, h);
//	}
        
//        Build.VERSION.SDK_INT>
        //magic
        int widthCamera=parameters.getPreviewSize().width;
        int heightCamera=parameters.getPreviewSize().height;
      
//        samsoung
//        int widthCamera=800;
//        int heightCamera=480;
        
        
       
       mTargetView.invalidate();
       mCamera.setParameters(parameters);
       mCamera.startPreview();
       
       mPreviewRunning=true;
   }

		



}
