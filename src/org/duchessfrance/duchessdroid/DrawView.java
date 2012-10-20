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
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;

public class DrawView extends ImageView {

    private static final String TAG = DrawView.class.getSimpleName();

	public Duchess duchess;
	
	public enum Action {
	    NONE, ONGOING, MOVING, ROTATING 
	};
	private Action mode = Action.NONE;

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
        // make the GamePanel focusable so it can handle events
        setFocusable(true);
        setFocusableInTouchMode(true);
	}

	@Override
	public void onDraw(Canvas canvas) {

		//Paint paint = new Paint();
		//mPaint.setColor(Color.WHITE);
		//canvas.drawText("Test", 0, 50, paint);

		duchess.draw(canvas);
		
		switch(mode) {
		case MOVING :
			drawMovingFeedback(canvas);
			break;
		case ROTATING :
			drawRotatingFeedback(canvas);
			break;
		default :
			break;
		}

	}
	
	private void drawMovingFeedback(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setAlpha(128);
		int radius = duchess.getBitmap().getWidth() / 4;
		canvas.drawCircle(duchess.getX(), duchess.getY(), 
				radius, paint);
	}
	
	private void drawRotatingFeedback(Canvas canvas) {
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
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // delegating event handling to the duchess
            duchess.handleActionDown((int)event.getX(), (int)event.getY());
        	mode = Action.ONGOING;
        	Log.d(TAG,"down");
         } if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // the gestures
            if (duchess.isTouched()) {
            	mode = Action.MOVING;
                // the duchess was picked up and is being dragged
            	duchess.setX((int)event.getX());
            	duchess.setY((int)event.getY());
            	invalidate();
                Log.d(TAG,"move");
           } else {
        	   mode = Action.ROTATING;
        	   double angdeg = getDegreesFromTouchEvent(event.getX(), event.getY());
        	   duchess.setAngdeg(angdeg);
        	   invalidate();
        	   Log.d(TAG,"rotate");
           }
        } if (event.getAction() == MotionEvent.ACTION_UP) {
         	Log.d(TAG,"up");
        	if (mode != Action.NONE) {
         	   invalidate();
        	}
        	mode = Action.NONE;
        	// touch was released
        	if (duchess.isTouched()) {
        		duchess.setTouched(false);
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
		//TODO factorize
		double delta_x = x - (width) /2;
        double delta_y = (height) /2 - y;
        double radians = Math.atan2(delta_y, delta_x);

        return Math.toDegrees(radians);
    }
}
