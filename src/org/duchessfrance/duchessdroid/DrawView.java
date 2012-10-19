package org.duchessfrance.duchessdroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class DrawView extends ImageView {

    private static final String TAG = DrawView.class.getSimpleName();

	public Duchess duchess;

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
        // make the GamePanel focusable so it can handle events
        setFocusable(true);

	}

	@Override
	public void onDraw(Canvas canvas) {

		Paint mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		canvas.drawText("Test", 0, 50, mPaint);
		duchess.draw(canvas);

	}
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // delegating event handling to the droid
            duchess.handleActionDown((int)event.getX(), (int)event.getY());
         } if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // the gestures
            if (duchess.isTouched()) {
                // the droid was picked up and is being dragged
            	duchess.setX((int)event.getX());
            	duchess.setY((int)event.getY());
            }
        } if (event.getAction() == MotionEvent.ACTION_UP) {
            // touch was released
            if (duchess.isTouched()) {
            	duchess.setTouched(false);
            }
        }
        return true;
    }
 

}
