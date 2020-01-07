package com.jixuan.wavedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PorterDuffXfermodeView extends View {
    private Paint mPaint;
    private Bitmap dstBmp, srcBmp;
    private RectF dstRect, srcRect;
    public int bitmapWitdh;
    public int bitmapHeight;

    private Xfermode mXfermode;
    private PorterDuff.Mode mPorterDuffMode = PorterDuff.Mode.SRC_ATOP;


    public PorterDuffXfermodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        dstBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_bg);
        srcBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mXfermode = new PorterDuffXfermode(mPorterDuffMode);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景色设为白色，方便比较效果
        canvas.drawColor(Color.TRANSPARENT);
        //将绘制操作保存到新的图层，因为图像合成是很昂贵的操作，将用到硬件加速，这里将图像合成的处理放到离屏缓存中进行
        int saveCount = canvas.saveLayer(srcRect, mPaint, Canvas.ALL_SAVE_FLAG);
        //绘制目标图
        canvas.drawBitmap(dstBmp, 0, 0, mPaint);
        //设置混合模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        //绘制源图
        canvas.drawBitmap(srcBmp, 100, 0, mPaint);
        //清除混合模式
        mPaint.setXfermode(null);
        //还原画布
        canvas.restoreToCount(saveCount);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        bitmapWitdh = dstBmp.getWidth();
        bitmapHeight = dstBmp.getHeight();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(bitmapWitdh, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(bitmapHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
   /* @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = w <= h ? w : h;
        int centerX = w/2;
        int centerY = h/2;
        int quarterWidth = width /4;
        srcRect = new RectF(centerX-quarterWidth, centerY-quarterWidth, centerX+quarterWidth, centerY+quarterWidth);
        dstRect = new RectF(centerX-quarterWidth, centerY-quarterWidth, centerX+quarterWidth, centerY+quarterWidth);
    }*/
}
