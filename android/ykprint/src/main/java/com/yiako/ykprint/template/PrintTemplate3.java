package com.yiako.ykprint.template;

import com.blankj.utilcode.util.ToastUtils;
import com.dascom.print.ESCPOS;
import com.yiako.ykprint.bt.CanvasUtils;
import com.yiako.ykprint.entity.PrintData3;

public class PrintTemplate3 {
    private ESCPOS escpos;

    private static int MULTIPLE = 8;
    private static final int line_width_border = 2;
    private static final int page_width = 70 * MULTIPLE;
    private static final int page_height = 85 * MULTIPLE;
    private static final int margin_horizontal = 2 * MULTIPLE;
    private static final int top_left_x = margin_horizontal;
    private static final int margin_vertical = 8 * MULTIPLE;
//    private static final int top_left_y = margin_vertical;// 32
    private static final int top_left_y = 2 * MULTIPLE;// 32
    private static final int border_width = page_width - 2 * margin_horizontal;
    private static final int border_height = page_height - 2 * margin_vertical;
    private static final int top_right_x = top_left_x + border_width;
    private static final int bottom_left_y = top_left_y + border_height;
    private static final int bottom_right_y = bottom_left_y;
    private static final int bottom_right_x = top_right_x;
    private static final int row36_column1_width = 10 * MULTIPLE;
    private static final int row37_column3_width = 20 * MULTIPLE;
    private static final int row36_sep1_x = top_left_x + row36_column1_width;
    private static final int row37_sep2_x = top_right_x - row37_column3_width;
    private static final int[] row_height = {8 * MULTIPLE, 16 * MULTIPLE, 10 * MULTIPLE, 10 * MULTIPLE,
            10 * MULTIPLE, 10 * MULTIPLE};

    public PrintTemplate3(ESCPOS escpos) {
        super();
        this.escpos = escpos;
    }

    public void doPrint( PrintData3 printData) {
        new Thread() {
            public void run() {
                for (int i = 0; i < 1; i++) {
                    CanvasUtils utils = new CanvasUtils(page_width, page_height);
                    drawBox(utils);
//					drawVerticalSeparator(utils);
                    drawRowContent(utils,printData);

                    boolean b = escpos.printBitmapBlackWhite(utils.getBitmap(), 14, 0);
                    if(b){
                        ToastUtils.showShort("打印成功！");
                    }else{
                        ToastUtils.showShort("打印失败！");
                    }
                }
            }

            ;
        }.start();

    }

    private void drawRowContent(CanvasUtils utils , PrintData3 printData) {

        int area_start_x = top_left_x + (top_right_x - top_left_x) / 2;
        int area_start_y = top_left_y;
        int area_start_x_3_2 = top_left_x + (top_right_x - top_left_x) / 3;
        int area_start_x_3_3 = top_left_x + (top_right_x - top_left_x) / 3 * 2;
        // 第一行内容
        utils.drawText(top_left_x, top_left_y, bottom_right_x,
                top_left_y + row_height[0],  "出库箱唛头",
                32,  CanvasUtils.PAlign.ALIGN_CENTER,0);

        // 第二行内容
        utils.drawText(top_left_x, top_left_y + row_height[0], bottom_right_x,
                top_left_y + row_height[0] + row_height[1], " 出库单号：",
                24,  CanvasUtils.PAlign.ALIGN_LEFT,0);

        // 打印第2行条码
        utils.drawBarCode(top_left_x + row36_column1_width+60, top_left_y + row_height[0],
                top_right_x, top_left_y + row_height[0] + row_height[1],
                printData.getReserved_no()  );

//        // 打印第2行条码下方的文字
//        utils.drawText(top_left_x + row36_column1_width, top_left_y + row_height[0] + 120,
//                top_right_x, top_left_y + row_height[0] + row_height[1],
//                printData.getReserved_no(),  24, CanvasUtils.PAlign.ALIGN_LEFT,0);

        // 第三行内容 左边栏内容
        utils.drawText(top_left_x, top_left_y + row_height[0] + row_height[1], top_right_x,
                top_left_y + row_height[1] + row_height[0] * 2,  " 发货仓："+printData.getPhysical_num_id(),
                 24, CanvasUtils.PAlign.ALIGN_LEFT,0);

        // 第三行内容 右边栏内容
        utils.drawText(top_left_x, top_left_y + row_height[1] + row_height[0]*2,
                top_right_x, top_left_y + row_height[1] + row_height[0] * 3,  " 收货仓："+printData.getRec_physical_num_id(),
                 24,  CanvasUtils.PAlign.ALIGN_LEFT,0);

        // 第4行内容 左边栏内容
        utils.drawText(top_left_x, top_left_y + row_height[0] * 3 + row_height[1], top_right_x,
                top_left_y + row_height[1] + row_height[0] * 4,   " 发货日期："+printData.getRec_date(),
                 24,  CanvasUtils.PAlign.ALIGN_LEFT,0);


        utils.drawText(top_left_x, top_left_y + row_height[0] * 4 + row_height[1], top_right_x,
                top_left_y + row_height[1]*2 + row_height[0] * 4,   " 出库箱号：",
                 24,  CanvasUtils.PAlign.ALIGN_LEFT,0);

        // 打印出库箱号
        utils.drawBarCode(top_left_x + row36_column1_width+60, top_left_y + row_height[0] * 4+ row_height[1],
                top_right_x, top_left_y + row_height[0]*4 + row_height[1]*2,
                printData.getContainer_labserlno());

//        utils.drawText(top_left_x + row36_column1_width, top_left_y + row_height[0] * 4 + row_height[1] + 120,
//                top_right_x, top_left_y + row_height[0]*5 + row_height[1],
//                printData.getContainer_labserlno(), 24, CanvasUtils.PAlign.ALIGN_LEFT,0);

    }

    private void drawBox( CanvasUtils utils ) {
        int border_top_left_y = top_left_y;
        utils.drawRectangle( top_left_x, border_top_left_y, bottom_right_x, bottom_right_y);
    }

}
