package org.duchessfrance.duchessdroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MaskView extends ImageView {

	private Size previewSize = null;
	private Paint mPaint;
	
	public MaskView(Context context, AttributeSet attrs) {
		super(context, attrs);
        setFocusable(false);
        setFocusableInTouchMode(false);
 
        mPaint = new Paint();
        mPaint.setColor(Color.rgb(0,0,0));
 	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (previewSize != null) {
			canvas.drawRect(previewSize.width, 0, canvas.getWidth(), canvas.getHeight(), mPaint);
		}
	}

	
	// getters and setters 
	
	public Size getPreviewSize() {
		return previewSize;
	}

	public void setPreviewSize(Size previewSize) {
		this.previewSize = previewSize;
	}

	
}
