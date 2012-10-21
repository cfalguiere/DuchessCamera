package org.duchessfrance.duchessdroid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

public class Duchess {
	private Bitmap bitmap;  // the actual bitmap
	private int x;          // the X coordinate
	private int y;          // the Y coordinate
	private boolean touched;    // if duchess is touched/picked up
	private float rotation;
	private double angdeg;
	private float scale;

	public Duchess(Bitmap bitmap, int x, int y) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.scale = 1.0f;
	}


	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public double getAngdeg() {
		return angdeg;
	}

	public void setAngdeg(double angdeg) {
		this.angdeg = angdeg;
	}

	private static final String TAG = Duchess.class.getSimpleName();



	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isTouched() {
		return touched;
	}

	public void setTouched(boolean touched) {
		this.touched = touched;
	}

	public void draw(Canvas canvas, float ratio) {
		Log.d(TAG, "redraw duchess with rotation " + rotation);
		Matrix matrix = new Matrix();
		float s = scale * ratio;
		int cw = Math.round(bitmap.getWidth() * ratio / 2);
		int ch = Math.round(bitmap.getHeight() * ratio / 2);
		matrix.setScale(s, s, cw, ch);
		matrix.postRotate((float)angdeg /*rotation*/, cw, ch);
		matrix.postTranslate((x - bitmap.getWidth()) * ratio, 
				(y - bitmap.getHeight()) * ratio);
		canvas.drawBitmap(bitmap, matrix, null /*new Paint()*/);		   
	}

	public void handleActionDown(int eventX, int eventY) {
		if (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth()/2))) {
			if (eventY >= (y - bitmap.getHeight() / 2) && (y <= (y + bitmap.getHeight() / 2))) {
				// duchess touched
				setTouched(true);
			} else {
				setTouched(false);
			}
		} else {
			setTouched(false);
		}
	}

}
