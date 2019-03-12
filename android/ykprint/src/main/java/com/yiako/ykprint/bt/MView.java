package com.yiako.ykprint.bt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;


public class MView extends View {
    private TextPaint mTextPaint;

    public MView(Context context) {
        super(context);
    }

    public MView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawTemplate(canvas);
    }


    private int MULTIPLE = 8;
    private int line_width_border = 2;
    private int page_width = 75 * MULTIPLE;
    private int page_height = 48 * MULTIPLE;
    private int margin_horizontal = 2 * MULTIPLE;
    private int margin_vertical = 8 * MULTIPLE;
    private int top_left_x = margin_horizontal;
    //    private int top_left_y = margin_vertical;// 32
    private int top_left_y = 2 * MULTIPLE;// 32
    private int border_width = page_width - 2 * margin_horizontal;
    private int top_right_x = top_left_x + border_width;
    private int border_height = page_height - 2 * margin_vertical;
    private int bottom_left_y = top_left_y + border_height;
    private int bottom_right_y = bottom_left_y;
    private int bottom_right_x = top_right_x;
    private int row36_column1_width = 10 * MULTIPLE;
    private int row37_column3_width = 20 * MULTIPLE;
    private int row36_sep1_x = top_left_x + row36_column1_width;
    private int row37_sep2_x = top_right_x - row37_column3_width;
    private int[] row_height = {8 * MULTIPLE, 16 * MULTIPLE};




    private void drawTemplate(Canvas canvas) {


        this.mTextPaint = new TextPaint();
        this.mTextPaint.setColor(Color.BLACK);
        this.mTextPaint.setStyle(Paint.Style.FILL);

        this.mCanvas = canvas;
//        drawText(0, 0, canvas.getWidth(), canvas.getHeight(), "paint,draw paint指用颜色画,   如油 画颜 料、",
//                32, PAlign.ALIGN_RIGHT, 0);


        drawBarCode(0,0, getWidth(), getHeight(),"1234567890123456789");

        CanvasUtils utils = new CanvasUtils(page_width, page_height);



//        val escpos =new ESCPOS(socket)
//        val printBitmapBlackWhite = escpos.printBitmapBlackWhite(utils.bitmap, 14, 0)


//        String title = "箱分货清单";
//        utils.setFontSize(32.0F);
//        Rect r = new Rect();
//        utils.measureText(title, r);
//        utils.drawText(title, (float)(pWidth - r.width()) / 2.0F, 0.0F);
//        utils.setFontSize(22.0F);
//        float y = (float)r.height() + 24.0F;
//        float x = 8.0F;
//        utils.drawText("商品条码:2100000000101", x, y);
//        y += 36.0F;
//        utils.drawText("类型:8", x, y);
//        utils.drawText("余量:-8", x + (float)(pWidth * 3) / 5.0F, y);
//        float rv = 2.0F;
//        utils.setLineWidth(rv);
//        ArrayList data = CollectionsKt.arrayListOf(new ArrayList[]{CollectionsKt.arrayListOf(new String[]{"种区", "种号", "应播数量", "实播数量"}), CollectionsKt.arrayListOf(new String[]{"18", "181", "28"}), CollectionsKt.arrayListOf(new String[]{"18", "181", "28"})});
//        float lx = 8.0F;
//        float ly = y + (float)36;
//        float tWidth = (float)pWidth - lx * (float)2;
//        float dHeight = utils.getTextHeight() + (float)10 + rv;
//        float tempY = ly;
//        int i = 0;
//        int var16 = data.size();
//        if (i <= var16) {
//            while(true) {
//                utils.drawLine(lx, tempY, lx + tWidth, tempY);
//                tempY += dHeight;
//                if (i == var16) {
//                    break;
//                }
//
//                ++i;
//            }
//        }
//
//        float tempX = lx + rv / (float)2;
//        float tHeight = (utils.getTextHeight() + (float)10) * (float)data.size() + rv * (float)data.size() - (float)1;
//        float cWidth = (tWidth - (float)(((ArrayList)data.get(0)).size() + 1) * rv) / (float)4;
//        int j = 0;
//        int var19 = ((ArrayList)data.get(0)).size();
//        if (j <= var19) {
//            while(true) {
//                utils.drawLine(tempX, ly, tempX, ly + tHeight);
//                tempX += cWidth + rv;
//                if (j == var19) {
//                    break;
//                }
//
//                ++j;
//            }
//        }
//
//        tempX = lx + rv;
//        tempY = ly;
//        float ttx = 0.0F;
//        Rect rect = new Rect();
//        int k = 0;
//
//        for(int var21 = data.size(); k < var21; ++k) {
//            Object var10000 = data.get(k);
//            Intrinsics.checkExpressionValueIsNotNull(var10000, "data[k]");
//            ArrayList d = (ArrayList)var10000;
//            ttx = tempX;
//            int di = 0;
//
//            for(int var24 = d.size(); di < var24; ++di) {
//                utils.measureText((String)d.get(di), rect);
//                utils.drawText((String)d.get(di), ttx + (cWidth - (float)rect.width()) / (float)2, tempY + (float)5);
//                ttx += cWidth + rv;
//            }
//
//            tempY += dHeight + rv;
//        }

    }

    Canvas mCanvas;


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
        mCanvas.translate(dx,dy                );
        myStaticLayout.draw(mCanvas);
        mCanvas.restore();
    }

    private  Bitmap createCode128(int width, int hight, String codeText) {
        HashMap hints = new HashMap<EncodeHintType,Object>();
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

            for(int var10 = h; y < var10; ++y) {
                int x = 0;

                for(int var12 = w; x < var12; ++x) {
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

    private  void drawBarCode(int area_start_x, int area_start_y, int area_end_x, int area_end_y,
                                String text) {

        int w = area_end_x - area_start_x;
        int h = area_end_y - area_start_y;

        Bitmap bitmap = createCode128(w, h-28, text);
        mCanvas.drawBitmap(bitmap, null, new RectF(0, 0, w, h-28 ), mTextPaint);

        drawText(area_start_x, area_end_y-20, area_end_x, area_end_y, text,
                24, PAlign.ALIGN_CENTER, 0);
    }

}
