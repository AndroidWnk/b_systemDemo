package com.etrans.jt.bluetooth.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class XxCircleRotateView extends ImageView {

        private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

        private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
        private static final int COLORDRAWABLE_DIMENSION = 1;

        private static final int DEFAULT_BORDER_WIDTH = 0;
        private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

        private final RectF mDrawableRect = new RectF();
        private final RectF mBorderRect = new RectF();

        private final Matrix mShaderMatrix = new Matrix();
        private final Paint mBitmapPaint = new Paint();
        private final Paint mBorderPaint = new Paint();

        private int mBorderColor = DEFAULT_BORDER_COLOR;
        private int mBorderWidth = DEFAULT_BORDER_WIDTH;

        private Bitmap mBitmap;
        private BitmapShader mBitmapShader;
        private int mBitmapWidth;
        private int mBitmapHeight;

        private float mDrawableRadius;
        private float mBorderRadius;

        private boolean mReady;
        private boolean mSetupPending;

        private float mRotateDegree = 0;
        private Camera mCamera = new Camera();
        
        private Thread mRotateThread;
        private boolean isPause = false;
        private boolean isStop = true;

        public void setDegree(float degree) {
                mRotateDegree = degree;
                postInvalidate();
        }

        public float getDegree() {
                return mRotateDegree;
        }

        public XxCircleRotateView(Context context) {
                super(context);
        }

        public XxCircleRotateView(Context context, AttributeSet attrs) {
                this(context, attrs, 0);
        }

        public XxCircleRotateView(Context context, AttributeSet attrs, int defStyle) {
                super(context, attrs, defStyle);
                super.setScaleType(SCALE_TYPE);

                mBorderWidth = 0;
                mBorderColor = 0;

                mReady = true;

                if (mSetupPending) {
                        setup();
                        mSetupPending = false;
                }
        }

        @Override
        public ScaleType getScaleType() {
                return SCALE_TYPE;
        }

        @Override
        public void setScaleType(ScaleType scaleType) {
                if (scaleType != SCALE_TYPE) {
                        throw new IllegalArgumentException(String.format(
                                        "ScaleType %s not supported.", scaleType));
                }
        }

        @SuppressLint("NewApi")
        @Override
        protected void onDraw(Canvas canvas) {
                if (getDrawable() == null) {
                        return;
                }
                Matrix matrix = canvas.getMatrix();
                matrix.postRotate(mRotateDegree, getWidth() / 2, getHeight() / 2);
                canvas.concat(matrix);
                
                mBitmapPaint.setAntiAlias(true);
                mBorderPaint.setAntiAlias(true);
                
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius,
                                mBitmapPaint);
                
                
                if (mBorderWidth != 0) {
                        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius,
                                        mBorderPaint);
                }
        }
        
        public void start()
        {
        	isStop = false;
        	isPause = false;
        	
        	if (mRotateThread == null)
        	{
        		mRotateThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (!isStop) {
                       	 
                       	 	 if (!isPause)
                       	 	 {
                       	 		 setDegree(getDegree() + 0.3f);
                                    try {
                                            Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                            e.printStackTrace();
                                    }
                       	 	 }
                       	 	 else
                       	 	 {
                       	 		try {
   									Thread.sleep(500);
   								} catch (InterruptedException e) {
   									// TODO 自动生成的 catch 块
   									e.printStackTrace();
   								}
                       	 	 }                           
                        }                     
                        
                    }
                    
                    
           	});
           	
           	mRotateThread.start();
        	}
        	
        }
        
        public void pause()
        {
        	if (isPause == false)
        		isPause = true;
        }
        
        public void resume()
        {
        	if (mRotateThread == null)
        	{
        		start();
        	}
        	else
        	{
        		if (isPause == true)
            		isPause = false;
			}        	
        }
        
        public void stop()
        {
        	setDegree(0);
        	if (isPause == false)
        	{
        		isPause = true;            	
        	}        	
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);
                setup();
        }

        public int getBorderColor() {
                return mBorderColor;
        }

        public void setBorderColor(int borderColor) {
                if (borderColor == mBorderColor) {
                        return;
                }

                mBorderColor = borderColor;
                mBorderPaint.setColor(mBorderColor);
                invalidate();
        }

        public int getBorderWidth() {
                return mBorderWidth;
        }

        public void setBorderWidth(int borderWidth) {
                if (borderWidth == mBorderWidth) {
                        return;
                }

                mBorderWidth = borderWidth;
                setup();
        }

        @Override
        public void setImageBitmap(Bitmap bm) {
                super.setImageBitmap(bm);
                mBitmap = bm;
                setup();
        }

        @Override
        public void setImageDrawable(Drawable drawable) {
                super.setImageDrawable(drawable);
                mBitmap = getBitmapFromDrawable(drawable);
                setup();
        }

        @Override
        public void setImageResource(int resId) {
                super.setImageResource(resId);
                mBitmap = getBitmapFromDrawable(getDrawable());
                setup();
        }

        private Bitmap getBitmapFromDrawable(Drawable drawable) {
                if (drawable == null) {
                        return null;
                }

                if (drawable instanceof BitmapDrawable) {
                        return ((BitmapDrawable) drawable).getBitmap();
                }

                try {
                        Bitmap bitmap;

                        if (drawable instanceof ColorDrawable) {
                                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
                                                COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
                        } else {
                                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                                                drawable.getIntrinsicHeight(), BITMAP_CONFIG);
                        }

                        Canvas canvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                        return bitmap;
                } catch (OutOfMemoryError e) {
                        return null;
                }
        }

        private void setup() {
                if (!mReady) {
                        mSetupPending = true;
                        return;
                }

                if (mBitmap == null) {
                        return;
                }

                mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
                                Shader.TileMode.CLAMP);

                mBitmapPaint.setAntiAlias(true);
                mBitmapPaint.setShader(mBitmapShader);

                mBorderPaint.setStyle(Paint.Style.STROKE);
                mBorderPaint.setAntiAlias(true);
                mBorderPaint.setColor(mBorderColor);
                mBorderPaint.setStrokeWidth(mBorderWidth);

                mBitmapHeight = mBitmap.getHeight();
                mBitmapWidth = mBitmap.getWidth();

                mBorderRect.set(0, 0, getWidth(), getHeight());
                mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2,
                                (mBorderRect.width() - mBorderWidth) / 2);

                mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width()
                                - mBorderWidth, mBorderRect.height() - mBorderWidth);
                mDrawableRadius = Math.min(mDrawableRect.height() / 2,
                                mDrawableRect.width() / 2);

                updateShaderMatrix();
                invalidate();
        }

        private void updateShaderMatrix() {
                float scale;
                float dx = 0;
                float dy = 0;

                mShaderMatrix.set(null);

                if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
                                * mBitmapHeight) {
                        scale = mDrawableRect.height() / (float) mBitmapHeight;
                        dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
                } else {
                        scale = mDrawableRect.width() / (float) mBitmapWidth;
                        dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
                }

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth,
                                (int) (dy + 0.5f) + mBorderWidth);

                mBitmapShader.setLocalMatrix(mShaderMatrix);
        }

}