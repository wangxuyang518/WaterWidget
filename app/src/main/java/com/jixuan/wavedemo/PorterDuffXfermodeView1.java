package com.jixuan.wavedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PorterDuffXfermodeView1 extends View {
    private Paint mWavePaint;
    private Bitmap dstBmp, srcBmp;
    private RectF dstRect, srcRect;
    public int bitmapWitdh;
    public int bitmapHeight;
    private Xfermode mXfermode;
    private PorterDuff.Mode mPorterDuffMode = PorterDuff.Mode.MULTIPLY;


    public PorterDuffXfermodeView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mWavePaint = new Paint();
        dstBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_bg);
        srcBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mXfermode = new PorterDuffXfermode(mPorterDuffMode);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int saveCount = canvas.saveLayer(srcRect, mWavePaint, Canvas.ALL_SAVE_FLAG);
        mWavePaint.setColor(Color.parseColor("#ff7474"));
        mWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRect(new Rect(0, 0, 200, 200), mWavePaint);//dest试图

        //canvas.drawBitmap(dstBmp,0,0,mWavePaint);

        mWavePaint.setColor(Color.parseColor("#1976D2"));
        mWavePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        canvas.drawCircle(180, 180, 50, mWavePaint);
        // canvas.drawBitmap(srcBmp,0,0,mWavePaint);
        canvas.restoreToCount(saveCount);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        bitmapWitdh = dstBmp.getWidth();
        bitmapHeight = dstBmp.getHeight();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(400, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(400, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w <= h ? w : h;
        int centerX = w / 2;
        int centerY = h / 2;
        int quarterWidth = width / 4;
        srcRect = new RectF(centerX - quarterWidth, centerY - quarterWidth, centerX + quarterWidth, centerY + quarterWidth);
        dstRect = new RectF(centerX - quarterWidth, centerY - quarterWidth, centerX + quarterWidth, centerY + quarterWidth);
    }
}
