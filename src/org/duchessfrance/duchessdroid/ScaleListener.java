package org.duchessfrance.duchessdroid;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

//private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
public class ScaleListener extends GestureDetector.SimpleOnGestureListener {

    private static final String TAG = ScaleListener.class.getSimpleName();

	public ScaleListener() {
       	Log.d(TAG, "creating scale listener");
	}
	
	@Override
	public boolean onDoubleTap(MotionEvent e) {
    	Log.d(TAG, "onDoubleTap");
		// TODO Auto-generated method stub
		return super.onDoubleTap(e);
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
    	Log.d(TAG, "onDoubleTapEvent");
		// TODO Auto-generated method stub
		return super.onDoubleTapEvent(e);
	}

	@Override
	public boolean onDown(MotionEvent e) {
    	Log.d(TAG, "onDown");
		// TODO Auto-generated method stub
		return super.onDown(e);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
    	Log.d(TAG, "onFling");
		// TODO Auto-generated method stub
		return super.onFling(e1, e2, velocityX, velocityY);
	}

	@Override
	public void onLongPress(MotionEvent e) {
       	Log.d(TAG, "onLongPress");
		// TODO Auto-generated method stub
		super.onLongPress(e);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2,
			float distanceX, float distanceY) {
       	Log.d(TAG, "onScroll");
		// TODO Auto-generated method stub
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
       	Log.d(TAG, "onShowPress");
		super.onShowPress(e);
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
       	Log.d(TAG, "onSingleTapConfirmed");
		// TODO Auto-generated method stub
		return super.onSingleTapConfirmed(e);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
       	Log.d(TAG, "onSingleTapUp");
		// TODO Auto-generated method stub
		return super.onSingleTapUp(e);
	}
	/*
	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		mode = Action.ZOOM;
    	Log.d(TAG, "scaling begin");
		return true;
	}

	@Override
    public boolean onScale(ScaleGestureDetector detector) {
    	Log.d(TAG, "scaling");
        mScaleFactor *= detector.getScaleFactor();
        
        // Don't let the object get too small or too large.
        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

        invalidate();
        return true;
    }

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
		mode = Action.NONE;
		super.onScaleEnd(detector);
	}
	*/
	
}