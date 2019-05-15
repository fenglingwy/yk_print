package com.yiako.ykprint.bt;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;


public class CanvasUtils {
    protected Canvas mCanvas;
    protected Paint mPaint;
    protected TextPaint mTextPaint;
    protected Bitmap mBitmap;
    protected float offset;
    protected float lineSpacing = 0.0F;

    public static final int FONT_SIZE_SMALL = 24;
    public static final int FONT_SIZE_NORMAL = 32;
    public static final int FONT_SIZE_BIG = 48;

    public CanvasUtils(int width, int height) {
        this.mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        this.mCanvas = new Canvas(this.mBitmap);
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mTextPaint = new TextPaint();
        this.mTextPaint.setColor(Color.BLACK);
        this.mTextPaint.setStyle(Paint.Style.FILL);
        Paint.FontMetrics metrics = this.mTextPaint.getFontMetrics();
        this.offset = metrics.leading - metrics.ascent;
    }


    public Paint getTextPaint() {
        return this.mTextPaint;
    }

    public void setGraphPaint(Paint paint) {
        this.mPaint = paint;
    }

    public Paint getGraphPaint() {
        return this.mPaint;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public Canvas getCanvas() {
        return this.mCanvas;
    }

    public void setPaintStyle(Paint.Style style) {
        this.mTextPaint.setStyle(style);
    }

    public float getTextHeight() {
        Paint.FontMetrics metrics = this.mTextPaint.getFontMetrics();
        return metrics.descent - metrics.ascent;
    }

    public void setFontSize(float fontSize) {
        this.mTextPaint.setTextSize(fontSize);
        Paint.FontMetrics metrics = this.mTextPaint.getFontMetrics();
        this.offset = metrics.leading - metrics.ascent;
    }

    public void measureText(String s, Rect bound) {
        this.mTextPaint.getTextBounds(s, 0, s.length(), bound);
    }

    @SuppressLint({"WrongConstant"})
    public void setPaintProperty(boolean isItalic, boolean isBold) {
        this.mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, (isBold ? 1 : 0) | (isItalic ? 2 : 0)));
    }

    public void setLineWidth(float width) {
        this.mPaint.setStrokeWidth(width);
    }

    public void setLineSpacing(float spacing) {
        this.lineSpacing = spacing;
    }

    public float getLineSpacing() {
        return this.lineSpacing;
    }

    public void drawText(String s, float x, float y) {
        this.mCanvas.drawText(s, x, y + this.offset, this.mTextPaint);
    }

    public void drawTextWrap(String s, float maxWidth, float x, float y) {
        Rect tBound = new Rect();
        this.mTextPaint.getTextBounds(s, 0, s.length() - 1, tBound);

        int count;
        for (int last = 0; (count = this.mTextPaint.breakText(s, last, s.length(), true, maxWidth, (float[]) null)) > 0; y += (float) tBound.height() + this.lineSpacing) {
            this.mCanvas.drawText(s, last, last + count, x, y + this.offset, this.mTextPaint);
            last += count;
        }

    }

    public void drawLine(float startX, float startY, float endX, float endY) {
        this.mCanvas.drawLine(startX, startY, endX, endY, this.mPaint);
    }

    public void drawRectangle(float left, float top, float right, float bottom) {
        this.mCanvas.drawRect(left, top, right, bottom, this.mPaint);
    }

    public void drawOval(float left, float top, float right, float bottom) {
        RectF rectF = new RectF(left, top, right, bottom);
        this.mCanvas.drawOval(rectF, this.mPaint);
    }

    public void drawBitmap(Bitmap bitmap, float x, float y) {
        this.mCanvas.drawBitmap(bitmap, x, y, (Paint) null);
    }


    public static enum PAlign {
        ALIGN_CENTER,
        /**
         * @hide
         */
        ALIGN_LEFT,
        /**
         * @hide
         */
        ALIGN_RIGHT,
    }


    public void drawText(int area_start_x, int area_start_y, int area_end_x, int area_end_y, String text, int fontSize, PAlign alignCenter, int bold) {
        mCanvas.save();
        mTextPaint.setTextSize(fontSize);
        mTextPaint.setFakeBoldText(bold==1);
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        switch (alignCenter) {
            case ALIGN_LEFT:
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
            case ALIGN_RIGHT:
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
                break;
            case ALIGN_CENTER:
                alignment = Layout.Alignment.ALIGN_CENTER;
                break;
        }
        StaticLayout myStaticLayout = new StaticLayout(text, mTextPaint, area_end_x - area_start_x, alignment, 1.0f, 0.0f, false);
        int dx = (-myStaticLayout.getWidth() >> 1) + (area_end_x + area_start_x >> 1);
        int dy = (-myStaticLayout.getHeight() >> 1) + (area_end_y + area_start_y >> 1);
        mCanvas.translate(dx, dy);
        myStaticLayout.draw(mCanvas);
        mCanvas.restore();
    }

    private Bitmap createCode128(int width, int hight, String codeText) {
        HashMap hints = new HashMap<EncodeHintType, Object>();
        EncodeHintType var6 = EncodeHintType.CHARACTER_SET;
        String var7 = "utf-8";
        hints.put(var6, var7);
//        var6 = EncodeHintType.MARGIN;
//        hints.put(var6, 20);

        try {
            BitMatrix var10000 = (new MultiFormatWriter()).encode(codeText, BarcodeFormat.CODE_128, width, hight, hints);
            BitMatrix matrix = var10000;
            int w = matrix.getWidth();
            int h = matrix.getHeight();
            int[] pixels = new int[w * h];
            int y = 0;

            for (int var10 = h; y < var10; ++y) {
                int x = 0;

                for (int var12 = w; x < var12; ++x) {
                    if (matrix.get(x, y)) {
                        pixels[y * w + x] = -16777216;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (WriterException var13) {
            var13.printStackTrace();
            return null;
        }
    }

    public void drawBarCode(int area_start_x, int area_start_y, int area_end_x, int area_end_y,
                            String text) {

        int w = area_end_x - area_start_x;
        int h = area_end_y - area_start_y;

        Bitmap bitmap = createCode128(w, h - 28, text);
        mCanvas.drawBitmap(bitmap, null, new RectF(area_start_x, area_start_y + 5, area_end_x, area_end_y - 28), mTextPaint);

        drawText(area_start_x, area_end_y - 20, area_end_x, area_end_y, text,
                24, PAlign.ALIGN_CENTER, 0);
    }
}

