package com.yiako.bluetoothprinter.template;

import android.util.Log;

import com.printer.sdk.PrinterConstants.LableFontSize;
import com.printer.sdk.PrinterConstants.LablePaperType;
import com.printer.sdk.PrinterConstants.PAlign;
import com.printer.sdk.PrinterConstants.PBarcodeType;
import com.printer.sdk.PrinterConstants.PRotate;
import com.printer.sdk.PrinterInstance;
import com.yiako.bluetoothprinter.entity.PrintData2;

import java.util.List;

public class PrintTemplate2 {

    private int MULTIPLE = 8;
    private int line_width_border = 2;
    private int page_width = 75 * MULTIPLE;
    private int page_height = 48 * MULTIPLE;
    private int margin_horizontal = 2 * MULTIPLE;
    private int top_left_x = margin_horizontal;
    private int margin_vertical = 8 * MULTIPLE;
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
    private String TAG = "PrintLabel";

    public void doPrint(final PrinterInstance iPrinter, PrintData2 printData) {
        List<PrintData2.TraySowDtlsBean> traySowDtls = printData.getTraySowDtls();
        new Thread() {
            public void run() {
                for (int i = 0; i < 1; i++) {
                    for (int lines = 0; lines < traySowDtls.size(); lines++) {
                        page_height += row_height[0];
                    }
                    border_height = page_height - 2 * margin_vertical;
                    bottom_left_y = top_left_y + border_height;
                    bottom_right_y = bottom_left_y;

                    iPrinter.pageSetup(LablePaperType.Size_80mm, page_width, page_height);
                    drawBox(iPrinter,traySowDtls.size());
                    drawVerticalSeparator(iPrinter);
                    drawRowContent(iPrinter,printData);
                    iPrinter.print(PRotate.Rotate_0, 0);
                }
            }
        }.start();

    }

    private void drawRowContent(PrinterInstance iPrinter, PrintData2 printData) {
        int area_start_x_4_1 = top_left_x;
        int area_start_x_4_2 = top_left_x + (top_right_x - top_left_x) / 4;
        int area_start_x_4_3 = top_left_x + (top_right_x - top_left_x) / 2;
        int area_start_x_4_4 = top_left_x + (top_right_x - top_left_x) * 3 / 4;
        int area_start_x_3_2 = top_left_x + (top_right_x - top_left_x) / 3;
        int area_start_x_3_3 = top_left_x + (top_right_x - top_left_x) / 3 * 2;

        int area_start_y = top_left_y;
        // 第一行内容
        iPrinter.drawText(top_left_x, top_left_y, bottom_right_x,
                top_left_y + row_height[0], PAlign.CENTER, PAlign.CENTER, "箱分货清单",
                LableFontSize.Size_32, 1, 0, 0, 0, PRotate.Rotate_0);

        // 第二行内容
        iPrinter.drawText(top_left_x, top_left_y + row_height[0], top_right_x,
                top_left_y + row_height[0] * 2, PAlign.START, PAlign.CENTER, "商品条码："+printData.getBarcode(),
                LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

        // 第三行内容
        iPrinter.drawText(area_start_x_4_1, top_left_y + row_height[0]*2, area_start_x_4_3,
                top_left_y + row_height[0] * 3, PAlign.START, PAlign.CENTER, "类型："+printData.getType_name(),
                LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
        iPrinter.drawText(area_start_x_4_3+4, top_left_y + row_height[0]*2, top_right_x,
                top_left_y + row_height[0] * 3, PAlign.CENTER, PAlign.CENTER, "余量："+printData.getLast_qty(),
                LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);


        // 打印表格头
        iPrinter.drawText(area_start_x_4_1, top_left_y + row_height[0]*3, area_start_x_4_2,
                top_left_y+ row_height[0] * 4, PAlign.CENTER, PAlign.CENTER, "种区",
                LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

        iPrinter.drawText(area_start_x_4_2, top_left_y +row_height[0]*3,
                area_start_x_4_3, top_left_y+row_height[0] * 4, PAlign.CENTER, PAlign.CENTER, "种号",
                LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
        iPrinter.drawText(area_start_x_4_3, top_left_y +row_height[0]*3,
                area_start_x_4_4, top_left_y+row_height[0] * 4, PAlign.CENTER, PAlign.CENTER, "应播数量",
                LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
        iPrinter.drawText(area_start_x_4_4, top_left_y +row_height[0]*3,
                top_right_x, top_left_y+row_height[0] * 4, PAlign.CENTER, PAlign.CENTER, " 实播数量",
                LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

        //打印表格内容
        List<PrintData2.TraySowDtlsBean> traySowDtls = printData.getTraySowDtls();
        for(int index = 0;index<traySowDtls.size();index++){
            iPrinter.drawText(area_start_x_4_1, top_left_y + row_height[0]*(4+index), area_start_x_4_2,
                    top_left_y+ row_height[0] *(5+index), PAlign.CENTER, PAlign.CENTER, traySowDtls.get(index).getZone_id(),
                    LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);

            iPrinter.drawText(area_start_x_4_2, top_left_y +row_height[0]*(4+index),
                    area_start_x_4_3, top_left_y+row_height[0] *(5+index), PAlign.CENTER, PAlign.CENTER, traySowDtls.get(index).getSow_id(),
                    LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
            iPrinter.drawText(area_start_x_4_3, top_left_y +row_height[0]*(4+index),
                    area_start_x_4_4, top_left_y+row_height[0] *(5+index), PAlign.CENTER, PAlign.CENTER, traySowDtls.get(index).getQty(),
                    LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
            iPrinter.drawText(area_start_x_4_4, top_left_y +row_height[0]*(4+index),
                    top_right_x, top_left_y+row_height[0] *(5+index), PAlign.CENTER, PAlign.CENTER, " ",
                    LableFontSize.Size_24, 1, 0, 0, 0, PRotate.Rotate_0);
        }
    }

    private void drawHorizontalSeparator(PrinterInstance iPrinter, int lines, int start_x, int end_x) {

        int temp = top_left_y + row_height[0] * 3;
        for (int i = 0; i < lines; i++) {
            temp += row_height[0];
            end_x = bottom_right_x;
            iPrinter.drawLine(2, start_x, temp, end_x, temp, true);
        }
    }

    //
    private void drawVerticalSeparator(PrinterInstance iPrinter) {

        int start_x = top_left_x + border_width / 4;
        int start_y = top_left_y + row_height[0] * 3;
        int end_x = start_x;
        int end_y = bottom_left_y;
        // 从左边数起第一条分割线
        iPrinter.drawLine(line_width_border, start_x, start_y, end_x, end_y, true);
        // 从左边数起第二条分割线
        start_x = top_left_x + border_width / 2;
        end_x = start_x;
        iPrinter.drawLine(line_width_border, start_x, start_y, end_x, end_y, true);
        // 从左边数起第三条分割线
        start_x = top_left_x + 3 * border_width / 4;
        end_x = start_x;
        iPrinter.drawLine(line_width_border, start_x, start_y, end_x, end_y, true);
    }

    private void drawBox(PrinterInstance iPrinter, int lines) {
        int border_top_left_y = top_left_y;
        iPrinter.drawBorder(3, top_left_x, border_top_left_y + row_height[0] * 3, bottom_right_x, bottom_right_y);
        drawHorizontalSeparator(iPrinter, lines,top_left_x, bottom_right_x);
    }

}
