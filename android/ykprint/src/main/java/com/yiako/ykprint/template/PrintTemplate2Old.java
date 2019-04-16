package com.yiako.ykprint.template;


import com.blankj.utilcode.util.ToastUtils;
import com.dascom.print.ESCPOS;
import com.yiako.ykprint.bt.CanvasUtils;
import com.yiako.ykprint.entity.PrintData2;

import java.util.List;

public class PrintTemplate2Old {
    private ESCPOS escpos;

    private int MULTIPLE = 8;
    private int page_width = 70 * MULTIPLE;
    private int page_height = 0;
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
    private int[] row_height = {6 * MULTIPLE, 14 * MULTIPLE};
    private String TAG = "PrintLabel";

    public PrintTemplate2Old(ESCPOS escpos) {
        super();
        this.escpos = escpos;
    }

    public final void doPrint(List<PrintData2> list) {

        new Thread() {
            public void run() {
                for (int idx = 0; idx < list.size(); idx++) {
                    page_height = 46 * MULTIPLE;
                    PrintData2 printData = list.get(idx);
                    List<PrintData2.TraySowDtlsBean> traySowDtls = printData.getTraySowDtls();
                    for (int i = 0; i < 1; i++) {
                        for (int lines = 0; lines < traySowDtls.size(); lines++) {
                            page_height += row_height[0];
                        }
                        border_height = page_height - 2 * margin_vertical;
                        bottom_left_y = top_left_y + border_height;
                        bottom_right_y = bottom_left_y;

                        CanvasUtils utils = new CanvasUtils(page_width, page_height);
                        drawBox(utils, traySowDtls.size());
                        drawVerticalSeparator(utils);
                        drawRowContent(utils, printData);


                        boolean b = escpos.printBitmapBlackWhite(utils.getBitmap(), 0, 0);
                        if (b) {
                            ToastUtils.showShort("打印成功！");
                        } else {
                            ToastUtils.showShort("打印失败！");
                        }
                    }
                }
            }
        }.start();
    }

    private void drawRowContent(CanvasUtils utils, PrintData2 printData) {
        int area_start_x_4_1 = top_left_x;
        int area_start_x_4_2 = top_left_x + (top_right_x - top_left_x) / 4;
        int area_start_x_4_3 = top_left_x + (top_right_x - top_left_x) / 2;
        int area_start_x_4_4 = top_left_x + (top_right_x - top_left_x) * 3 / 4;
        int area_start_x_3_2 = top_left_x + (top_right_x - top_left_x) / 3;
        int area_start_x_3_3 = top_left_x + (top_right_x - top_left_x) / 3 * 2;

        int area_start_y = top_left_y;

        utils.drawText(top_left_x, top_left_y, bottom_right_x,
                top_left_y + row_height[0], "箱分货清单",
                32, CanvasUtils.PAlign.ALIGN_CENTER, 1);

        utils.drawText(top_left_x, top_left_y + row_height[0], top_right_x,
                top_left_y + row_height[0] * 2, "商品编码：" + printData.getBarcode(),
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawText(top_left_x, top_left_y + row_height[0] * 2, top_right_x,
                top_left_y + row_height[0] * 3, "商家商品编码：" + printData.getItemid(),
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawText(area_start_x_4_1, top_left_y + row_height[0] * 3, area_start_x_4_3,
                top_left_y + row_height[0] * 4, "类型：" + printData.getType_name(),
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawText(area_start_x_4_3 + 4, top_left_y + row_height[0] * 3, top_right_x,
                top_left_y + row_height[0] * 4, "余量：" + printData.getLast_qty(),
                24, CanvasUtils.PAlign.ALIGN_CENTER, 0);


        // 打印表格头
        utils.drawText(area_start_x_4_1, top_left_y + row_height[0] * 4, area_start_x_4_2,
                top_left_y + row_height[0] * 5, "种区",
                24, CanvasUtils.PAlign.ALIGN_CENTER, 0);

        utils.drawText(area_start_x_4_2, top_left_y + row_height[0] * 4,
                area_start_x_4_3, top_left_y + row_height[0] * 5, "种号",
                24, CanvasUtils.PAlign.ALIGN_CENTER, 0);
        utils.drawText(area_start_x_4_3, top_left_y + row_height[0] * 4,
                area_start_x_4_4, top_left_y + row_height[0] * 5, "应播数量",
                24, CanvasUtils.PAlign.ALIGN_CENTER, 0);
        utils.drawText(area_start_x_4_4, top_left_y + row_height[0] * 4,
                top_right_x, top_left_y + row_height[0] * 5, " 实播数量",
                24, CanvasUtils.PAlign.ALIGN_CENTER, 0);

        //打印表格内容
        List<PrintData2.TraySowDtlsBean> traySowDtls = printData.getTraySowDtls();
        for (int index = 0; index < traySowDtls.size(); index++) {
            utils.drawText(area_start_x_4_1, top_left_y + row_height[0] * (5 + index), area_start_x_4_2,
                    top_left_y + row_height[0] * (6 + index), traySowDtls.get(index).getZone_id(),
                    24, CanvasUtils.PAlign.ALIGN_CENTER, 0);

            utils.drawText(area_start_x_4_2, top_left_y + row_height[0] * (5 + index),
                    area_start_x_4_3, top_left_y + row_height[0] * (6 + index), traySowDtls.get(index).getSow_id(),
                    24, CanvasUtils.PAlign.ALIGN_CENTER, 0);
            utils.drawText(area_start_x_4_3, top_left_y + row_height[0] * (5 + index),
                    area_start_x_4_4, top_left_y + row_height[0] * (6 + index), traySowDtls.get(index).getQty(),
                    24, CanvasUtils.PAlign.ALIGN_CENTER, 0);
            utils.drawText(area_start_x_4_4, top_left_y + row_height[0] * (5 + index),
                    top_right_x, top_left_y + row_height[0] * (6 + index), " ",
                    24, CanvasUtils.PAlign.ALIGN_CENTER, 0);
        }
    }

    private void drawHorizontalSeparator(CanvasUtils utils, int lines, int start_x, int end_x) {

        int temp = top_left_y + row_height[0] * 4;
        for (int i = 0; i < lines; i++) {
            temp += row_height[0];
            utils.drawLine(start_x, temp, end_x, temp);
        }
    }

    //
    private void drawVerticalSeparator(CanvasUtils utils) {

        int start_x = top_left_x + border_width / 4;
        int start_y = top_left_y + row_height[0] * 4;
        int end_x = start_x;
        int end_y = bottom_left_y;

        // 从左边数起第一条分割线
        utils.drawLine(start_x, start_y, end_x, end_y);

        // 从左边数起第二条分割线
        start_x = top_left_x + border_width / 2;
        end_x = start_x;
        utils.drawLine(start_x, start_y, end_x, end_y);

        // 从左边数起第三条分割线
        start_x = top_left_x + 3 * border_width / 4;
        end_x = start_x;
        utils.drawLine(start_x, start_y, end_x, end_y);
    }

    private void drawBox(CanvasUtils utils, int lines) {
        utils.drawRectangle(top_left_x, top_left_y + row_height[0] * 4, bottom_right_x, bottom_right_y);
        drawHorizontalSeparator(utils, lines, top_left_x, bottom_right_x);
    }

}
