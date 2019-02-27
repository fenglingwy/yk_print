package com.yiako.bluetoothprinter.template;

import android.util.Log;

import com.printer.sdk.PrinterConstants.LableFontSize;
import com.printer.sdk.PrinterConstants.LablePaperType;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PBarcodeType;
import com.printer.sdk.PrinterConstants.PRotate;
import com.printer.sdk.PrinterInstance;
import com.yiako.bluetoothprinter.entity.PrintData3;

public class PrintTemplate3 {

    private static int MULTIPLE = 8;
    private static final int line_width_border = 2;
    private static final int page_width = 75 * MULTIPLE;
    private static final int page_height = 75 * MULTIPLE;
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
    private static final String TAG = "PrintLabel";

    public void doPrint(final PrinterInstance iPrinter, final PrintData3 printData) {
        new Thread() {
            public void run() {
                for (int i = 0; i < 1; i++) {
                    iPrinter.pageSetup(LablePaperType.Size_80mm, page_width, page_height);
                    drawBox(iPrinter);
//					drawVerticalSeparator(iPrinter);
                    drawRowContent(iPrinter,printData);
                    iPrinter.print(PRotate.Rotate_0, 0);
                }
            }

            ;
        }.start();

    }

    private void drawRowContent(PrinterInstance iPrinter, PrintData3 printData) {

        int area_start_x = top_left_x + (top_right_x - top_left_x) / 2;
        int area_start_y = top_left_y;
        int area_start_x_3_2 = top_left_x + (top_right_x - top_left_x) / 3;
        int area_start_x_3_3 = top_left_x + (top_right_x - top_left_x) / 3 * 2;
        // 第一行内容
        iPrinter.drawText(top_left_x, top_left_y, bottom_right_x,
                top_left_y + row_height[0], PAlign.CENTER, PAlign.CENTER, "出库箱唛头",
                LableFontSize.Size_32, 1, 0, 0, 0, PRotate.Rotate_0);

        // 第二行内容
        iPrinter.drawText(top_left_x, top_left_y + row_height[0], bottom_right_x,
                top_left_y + row_height[0] + row_height[1], PAlign.START, PAlign.CENTER, " 出库单号：",
                LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

        // 打印第2行条码
        iPrinter.drawBarCode(top_left_x + row36_column1_width, top_left_y + row_height[0],
                top_right_x, top_left_y + row_height[0] + row_height[1], PAlign.CENTER, PAlign.CENTER, 0, 0,
                printData.getReserved_no(), PBarcodeType.CODE128, 1, 80, PRotate.Rotate_0);

        // 打印第2行条码下方的文字
        iPrinter.drawText(top_left_x + row36_column1_width, top_left_y + row_height[0] + 120,
                top_right_x, top_left_y + row_height[0] + row_height[1], PAlign.CENTER, PAlign.CENTER,
                printData.getReserved_no(), LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

        // 第三行内容 左边栏内容
        iPrinter.drawText(top_left_x, top_left_y + row_height[0] + row_height[1], area_start_x,
                top_left_y + row_height[1] + row_height[0] * 2, PAlign.START, PAlign.CENTER, " 发货仓："+printData.getPhysical_num_id(),
                LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

        // 第三行内容 右边栏内容
        iPrinter.drawText(area_start_x, top_left_y + row_height[1] + row_height[0],
                top_right_x, top_left_y + row_height[1] + row_height[0] * 2, PAlign.START, PAlign.CENTER, " 收货仓："+printData.getRec_physical_num_id(),
                LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

        // 第4行内容 左边栏内容
        iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 2 + row_height[1], top_right_x,
                top_left_y + row_height[1] + row_height[0] * 3, PAlign.START, PAlign.CENTER, " 发货日期："+printData.getRec_date(),
                LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);


        iPrinter.drawText(top_left_x, top_left_y + row_height[0] * 3 + row_height[1], top_right_x,
                top_left_y + row_height[1] + row_height[0] * 4, PAlign.START, PAlign.CENTER, " 出库箱号：",
                LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

        // 打印出库箱号
        iPrinter.drawBarCode(top_left_x + row36_column1_width, top_left_y + row_height[0] * 3+ row_height[1],
                top_right_x, top_left_y + row_height[0]*4 + row_height[1], PAlign.CENTER, PAlign.CENTER, 0, 0,
                printData.getContainer_labserlno(), PBarcodeType.CODE128, 1, 80, PRotate.Rotate_0);

        iPrinter.drawText(top_left_x + row36_column1_width, top_left_y + row_height[0] * 3 + row_height[1] + 120,
                top_right_x, top_left_y + row_height[0]*4 + row_height[1], PAlign.CENTER, PAlign.CENTER,
                printData.getContainer_labserlno(), LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

    }

    private void drawBox(PrinterInstance iPrinter) {
        int border_top_left_y = top_left_y;
        iPrinter.drawBorder(3, top_left_x, border_top_left_y, bottom_right_x, bottom_right_y);
//		drawHorizontalSeparator(iPrinter, top_left_x, bottom_right_x);
    }

}
