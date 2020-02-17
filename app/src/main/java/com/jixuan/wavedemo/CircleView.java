package com.jixuan.wavedemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.LinearInterpolator;





import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import static android.animation.ValueAnimator.REVERSE;

public class CircleView extends View {

    Paint mWavePaint;
    PaintFlagsDrawFilter mPaintFlagsDrawFilter;
    public Bitmap bitmap;
    private Path mPath = new Path();

    public int bitmapWitdh;
    public int bitmapHeight;

    public int mMoveX = 0;
    public int initHeight = 30;
    public int count = 3;
    public int dx;

    public boolean firstStart = false;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        bitmapWitdh = bitmap.getWidth() ;
        dx = bitmapWitdh / 8;
        bitmapHeight = bitmap.getHeight() ;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(bitmapWitdh, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(bitmapHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    int i = 0;

    public void init() {
        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_bg, options);

        Matrix matrix = new Matrix();
        matrix.postScale(0.4f, 0.4f);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),bitmap.getHeight(), matrix, true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        Log.d("onDraw", "+++++++++++++");
        mWavePaint.setColor(Color.parseColor("#ff7474"));
        mWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        int c = canvas.saveLayer(new RectF(0, 0, bitmapWitdh, bitmapHeight), mWavePaint);
        canvas.drawBitmap(bitmap, 0, 0, mWavePaint);//dest视图

        mWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));

        mWavePaint.setColor(Color.parseColor("#ff7474"));
        mPath.moveTo(0 - mMoveX, bitmapHeight - initHeight);
        int x = 0 - mMoveX;
        for (int i = 0; i < count; i++) {
            mPath.quadTo(x + dx - mMoveX, (bitmapHeight - initHeight) - 20, x + 2 * dx - mMoveX, bitmapHeight - initHeight);
            mPath.quadTo(x + 3 * dx - mMoveX, (bitmapHeight - initHeight) + 20, x + 4 * dx - mMoveX, bitmapHeight - initHeight);
            x = 4 * (i + 1) * dx;
        }
        mPath.lineTo(bitmapWitdh, bitmapHeight);
        mPath.lineTo(0, bitmapHeight);
        mPath.close();
        canvas.drawPath(mPath, mWavePaint);
        canvas.restoreToCount(c);
        mWavePaint.setXfermode(null);


        if (!firstStart) {
            startAnimation();
            firstStart = true;
        }

    }


    private void startAnimation() {
        ValueAnimator mAnimator = ValueAnimator.ofInt(0, bitmapWitdh / 2);
        mAnimator.setDuration(2000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMoveX = (int) animation.getAnimatedValue();
                Log.d("onDraw1", "mMoveX: " + mMoveX);
                postInvalidate();
                initHeight = initHeight + 1;
                if (initHeight >= bitmapHeight) {
                    initHeight = 30;
                }
            }
        });
        mAnimator.start();
    }

}
