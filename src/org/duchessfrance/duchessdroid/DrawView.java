package org.duchessfrance.duchessdroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.WindowManager;
import android.widget.ImageView;

public class DrawView extends ImageView {

    private static final String TAG = DrawView.class.getSimpleName();

	public Duchess duchess;
	
	public enum Action {
	    NONE, ONGOING, DRAG, ROTATE, ZOOM 
	};
	private Action mode = Action.NONE;

	private float mScaleFactor = 1.0f;
	//TODO setup multi touch for scaling - need fix interaction with single touch
	//private /*Scale*/GestureDetector mScaleDetector;
	//private ScaleListener scaleListener;

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
        // make the Panel focusable so it can handle events
        setFocusable(true);
        setFocusableInTouchMode(true);
        
         // Create our ScaleGestureDetector
        //scaleListener = new ScaleListener();
        //mScaleDetector = new /*Scale*/GestureDetector(context, scaleListener);
 
	}

	@Override
	public void onDraw(Canvas canvas) {

		//Paint paint = new Paint();
		//mPaint.setColor(Color.WHITE);
		//canvas.drawText("Test", 0, 50, paint);
		canvas.scale(mScaleFactor, mScaleFactor);

		duchess.draw(canvas);
		
		switch(mode) {
		case DRAG :
			drawDragFeedback(canvas);
			break;
		case ROTATE :
			drawRotateFeedback(canvas);
			break;
		case ZOOM :
			drawZoomFeedback(canvas);
			break;
		default :
			break;
		}
		

	}
	
	private void drawDragFeedback(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setAlpha(128);
		int radius = duchess.getBitmap().getWidth() / 4;
		canvas.drawCircle(duchess.getX(), duchess.getY(), 
				radius, paint);
	}
	
	private void drawRotateFeedback(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setAlpha(128);
		int offset = 40;
		paint.setStrokeWidth(offset);
		paint.setStyle(Style.STROKE);
		int radius = duchess.getBitmap().getWidth() / 2 + offset;
		canvas.drawCircle(duchess.getX(), duchess.getY(), 
				radius, paint);
	}

	private void drawZoomFeedback(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setAlpha(128);
		int offset = 20;
		paint.setStrokeWidth(offset);
		paint.setStyle(Style.STROKE);
		int radius = duchess.getBitmap().getWidth() / 2 + offset;
		canvas.drawCircle(duchess.getX(), duchess.getY(), 
				radius, paint);
	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	final int action = event.getAction();

    	switch (action & MotionEvent.ACTION_MASK) {
    		case MotionEvent.ACTION_DOWN: {
                // delegating event handling to the duchess
                duchess.handleActionDown((int)event.getX(), (int)event.getY());
            	mode = Action.ONGOING;
            	Log.d(TAG,"down");
            	break;
    	    }
    	    case MotionEvent.ACTION_MOVE: {
    	    	if (duchess.isTouched()) {
    	    		mode = Action.DRAG;
    	    		// the duchess was picked up and is being dragged
    	    		duchess.setX((int)event.getX());
    	    		duchess.setY((int)event.getY());
    	    		invalidate();
    	    		Log.d(TAG,"move");
    	    	} else {
    	    		mode = Action.ROTATE;
    	    		double angdeg = getDegreesFromTouchEvent(event.getX(), event.getY());
    	    		duchess.setAngdeg(angdeg);
    	    		invalidate();
    	    		Log.d(TAG,"rotate");
    	    	}
    	    	break;
    	    }
    	    case MotionEvent.ACTION_UP: {
    	    	Log.d(TAG,"up");
    	    	if (mode != Action.NONE) {
    	    		invalidate();
    	    	}
    	    	mode = Action.NONE;
    	    	// touch was released
    	    	if (duchess.isTouched()) {
    	    		duchess.setTouched(false);
    	    	}
    	    	break;
    	    }        	
        }
        return true;
    }
 
    private double getDegreesFromTouchEvent(float x, float y){
		double delta_x = x - duchess.getX();
        double delta_y = duchess.getY() - y;
        double radians = Math.atan2(delta_y, delta_x);

        return Math.toDegrees(Math.PI*2 - radians);
    }

    private double getDegreesFromTouchEvent2(float x, float y){
    	WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		double delta_x = x - (width) /2;
        double delta_y = (height) /2 - y;
        double radians = Math.atan2(delta_y, delta_x);

        return Math.toDegrees(radians);
    }
    
    // accessors and mutators
    
	public Action getMode() {
		return mode;
	}

	public void setMode(Action mode) {
		this.mode = mode;
	}

}
