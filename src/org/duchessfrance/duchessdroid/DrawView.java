package org.duchessfrance.duchessdroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class DrawView extends ImageView {

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	public void onDraw(Canvas canvas) {

		Paint mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		canvas.drawText("Test", 0, 50, mPaint);
	}

}
