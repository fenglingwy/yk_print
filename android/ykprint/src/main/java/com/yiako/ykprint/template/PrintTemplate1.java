package com.yiako.ykprint.template;


import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.dascom.print.ESCPOS;
import com.yiako.ykprint.bt.CanvasUtils;
import com.yiako.ykprint.entity.PrintData1;

import java.util.List;

public class PrintTemplate1 {
    private ESCPOS escpos;

    private int MULTIPLE = 8;
    private int line_width_border = 2;
    private int page_width = 70 * MULTIPLE;
    private int page_height = 74 * MULTIPLE;
    private int margin_horizontal = MULTIPLE;
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
    private int[] row_height = {6 * MULTIPLE, 14 * MULTIPLE};
    private String TAG = "PrintLabel";

    public PrintTemplate1(ESCPOS escpos) {
        super();
        this.escpos = escpos;
    }

    public final void doPrint(List<PrintData1> list) {

        new Thread() {
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    CanvasUtils utils = new CanvasUtils(page_width, page_height);
                    drawBox(utils);
//					drawVerticalSeparator(iPrinter);
                    drawRowContent(utils, list.get(i));
                    Log.d("Tag","打印开始");
                    boolean b = escpos.printBitmapBlackWhite(utils.getBitmap(), 14, 0);
                    Log.d("Tag","打印结束");
                    if(b){
                        ToastUtils.showShort("打印成功！");
                    }else{
                        ToastUtils.showShort("打印失败！");
                    }
                }
            }
        }.start();
    }

    private void drawRowContent(CanvasUtils utils, PrintData1 printData1) {
        int area_start_x = top_left_x + (top_right_x - top_left_x) / 2;
        int area_start_y = top_left_y;
        int area_start_x_3_2 = top_left_x + (top_right_x - top_left_x) / 3;
        int area_start_x_3_3 = top_left_x + (top_right_x - top_left_x) / 3 * 2;

        utils.drawText(top_left_x, top_left_y, bottom_right_x,
                top_left_y + row_height[0], "入库装箱单",
                32, CanvasUtils.PAlign.ALIGN_CENTER, 1);

        utils.drawText(top_left_x, top_left_y + row_height[0], bottom_right_x,
                top_left_y + row_height[0] + row_height[1], " 箱号：",
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawBarCode(top_left_x + row36_column1_width+60, top_left_y + row_height[0],
                top_right_x, top_left_y + row_height[0] + row_height[1], printData1.getTray_serlno());

        utils.drawText(top_left_x, top_left_y + row_height[0] + row_height[1], area_start_x_3_3,
                top_left_y + row_height[1] + row_height[0] * 2, "商家编码：" + printData1.getBarcode(),
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawText(area_start_x_3_3, top_left_y + row_height[1] + row_height[0],
                top_right_x, top_left_y + row_height[1] + row_height[0] * 2, " 数量：" + printData1.getQty(),
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawText(top_left_x, top_left_y + row_height[0] * 2 + row_height[1], top_right_x,
                top_left_y + row_height[1] + row_height[0] * 3, " 品名：" + printData1.getItem_name(),
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawText(top_left_x, top_left_y + row_height[0] * 3 + row_height[1], top_right_x,
                top_left_y + row_height[1] + row_height[0] * 4, " 供应商：" + printData1.getSupplyer(),
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawText(top_left_x, top_left_y + row_height[0] * 4 + row_height[1], top_left_x + row36_column1_width + 60,
                top_left_y + row_height[1] * 2 + row_height[0] * 4, " 验收单号：",
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawBarCode(top_left_x + row36_column1_width + 60, top_left_y + row_height[0] * 4 + row_height[1],
                top_right_x, top_left_y + row_height[1] * 2 + row_height[0] * 4, printData1.getReserved_no());

        String mark = "";
        switch (printData1.getMain_distribution_type()) {
            //新品主配，补货主配，留仓主配   1,2,3
            case "1":
                mark = "新品主配";
                break;
            case "2":
                mark = "补货主配";
                break;
            case "3":
                mark = "留仓主配";
                break;
        }

        utils.drawText(top_left_x, top_left_y + row_height[1] * 2 + row_height[0] * 4,
                top_right_x, top_left_y + row_height[1] * 2 + row_height[0] * 5, " 主配标记：" + mark,
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);
    }

    private void drawBox(CanvasUtils utils) {
        utils.drawRectangle(top_left_x, top_left_y, bottom_right_x, bottom_right_y);
    }


}
