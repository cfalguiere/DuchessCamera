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

	private Paint dragPaint = new Paint();
	private Paint rotatePaint = new Paint();
	private Paint zoomPaint = new Paint();
	int rotateFeedbackRadius;
	
	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
        // make the Panel focusable so it can handle events
        setFocusable(true);
        setFocusableInTouchMode(true);
        
         // Create our ScaleGestureDetector
        //scaleListener = new ScaleListener();
        //mScaleDetector = new /*Scale*/GestureDetector(context, scaleListener);
 
	}
	
	private void initializePainters() {
		dragPaint = new Paint();
		dragPaint.setColor(Color.GREEN);
		dragPaint.setAlpha(128);

		rotatePaint = new Paint();
		rotatePaint.setColor(Color.GREEN);
		rotatePaint.setAlpha(128);
		int offset = 40;
		rotatePaint.setStrokeWidth(offset);
		rotatePaint.setStyle(Style.STROKE);
		rotateFeedbackRadius = duchess.getBitmap().getWidth() / 2 + offset;

	}

	@Override
	public void onDraw(Canvas canvas) {

		//Paint paint = new Paint();
		//mPaint.setColor(Color.WHITE);
		//canvas.drawText("Test", 0, 50, paint);
		canvas.scale(mScaleFactor, mScaleFactor);

		duchess.draw(canvas, 1);
		
		switch(mode) {
		case DRAG :
			drawDragFeedback(canvas);
			break;
		case ROTATE :
			drawRotateFeedback(canvas);
			break;
		default :
			break;
		}
		

	}
	
	private void drawDragFeedback(Canvas canvas) {
		int radius = duchess.getBitmap().getWidth() / 4;
		canvas.drawCircle(duchess.getX(), duchess.getY(), 
				radius, dragPaint);
	}
	
	private void drawRotateFeedback(Canvas canvas) {
		canvas.drawCircle(duchess.getX(), duchess.getY(), 
				rotateFeedbackRadius, rotatePaint);
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
    	    
    	    // TODO pointer management for multitouch
        }
        return true;
    }
 
    private double getDegreesFromTouchEvent(float x, float y){
		double delta_x = x - duchess.getX();
        double delta_y = duchess.getY() - y;
        double radians = Math.atan2(delta_y, delta_x);

        return Math.toDegrees(Math.PI*2 - radians);
    }
    
    // accessors and mutators
    
	public Action getMode() {
		return mode;
	}

	public void setMode(Action mode) {
		this.mode = mode;
	}

}
