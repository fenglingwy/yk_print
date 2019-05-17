package com.yiako.ykprint.template;


import com.blankj.utilcode.util.ToastUtils;
import com.dascom.print.ESCPOS;
import com.yiako.ykprint.bt.CanvasUtils;
import com.yiako.ykprint.entity.PrintData2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PrintTemplate2New {
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

    public PrintTemplate2New(ESCPOS escpos) {
        super();
        this.escpos = escpos;
    }

    public final void doPrint(List<PrintData2> list) {
        if (list.size() == 0) return;

        new Thread() {
            public void run() {

                for (int i = 0; i < list.size(); i++) {
                    TreeMap<String, ArrayList<PrintData2.TraySowDtlsBean>> map = new TreeMap<>();
                    PrintData2 data = list.get(i);
                    for (PrintData2.TraySowDtlsBean item : data.getTraySowDtls()) {
                        String zone_id = item.getZone_id();
                        if (map.containsKey(zone_id)) {
                            map.get(zone_id).add(item);
                        } else {
                            ArrayList<PrintData2.TraySowDtlsBean> arrayList = new ArrayList<>();
                            arrayList.add(item);
                            map.put(zone_id, arrayList);
                        }
                    }

                    for (Map.Entry<String, ArrayList<PrintData2.TraySowDtlsBean>> entry : map.entrySet()) {
                        List<PrintData2.TraySowDtlsBean> traySowDtls = entry.getValue();

                        escpos.setJustification(Constants.TEXT_ALIGN_CENTER);
                        escpos.setEmphasizedMode(true);
                        escpos.setCharacterSize(1, 1);
                        escpos.printText("箱分货清单");
                        escpos.printFeedLines(2);

                        escpos.setJustification(Constants.TEXT_ALIGN_LEFT);
                        escpos.setEmphasizedMode(false);
                        escpos.setCharacterSize(0, 0);

                        escpos.setLineSpacing(40);
                        escpos.printText("箱号：" + data.getCase_num_id());
                        escpos.printLineFeed();

                        escpos.printText("商品编码：" + data.getBarcode());
                        escpos.printLineFeed();

                        escpos.printText("商家商品编码：" + data.getItemid());
                        escpos.printLineFeed();

                        escpos.printText("类型：" + data.getType_name());
                        escpos.setAbsolutePosition(330);
                        escpos.printText("余量：" + data.getLast_qty());

                        escpos.printFeedLines(1);
                        escpos.setToDefaultLineSpacing();
                        escpos.printText("- - - - - - - - - - - - - - - - - - - - - - - -");
                        escpos.printLineFeed();
                        escpos.setAbsolutePosition(50);
                        escpos.printText("种区");
                        escpos.setAbsolutePosition(190);
                        escpos.printText("种号");
                        escpos.setAbsolutePosition(305);
                        escpos.printText("应播数量");
                        escpos.setAbsolutePosition(443);
                        escpos.printText("实播数量");
                        escpos.printLineFeed();
                        escpos.printText("- - - - - - - - - - - - - - - - - - - - - - - -");
                        escpos.printLineFeed();
                        for (int lines = 0; lines < traySowDtls.size(); lines++) {
                            escpos.setAbsolutePosition(55);
                            escpos.printText(traySowDtls.get(lines).getZone_id());
                            escpos.setAbsolutePosition(190);
                            escpos.printText(traySowDtls.get(lines).getSow_id());
                            escpos.setAbsolutePosition(340);
                            escpos.printText(traySowDtls.get(lines).getQty());

                            escpos.printLineFeed();
                            escpos.printText("- - - - - - - - - - - - - - - - - - - - - - - -");
                            escpos.printLineFeed();
                        }
                        escpos.printFeedLines(4);

                    }
                }


            }
        }.start();
    }

    private void drawRowContent(CanvasUtils utils, PrintData2 printData, List<PrintData2.TraySowDtlsBean> traySowDtls) {
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
                top_left_y + row_height[0] * 2, "箱号：" + printData.getCase_num_id(),
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawText(top_left_x, top_left_y + row_height[0] * 2, top_right_x,
                top_left_y + row_height[0] * 3, "商品编码：" + printData.getBarcode(),
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawText(top_left_x, top_left_y + row_height[0] * 3, top_right_x,
                top_left_y + row_height[0] * 4, "商家商品编码：" + printData.getItemid(),
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawText(area_start_x_4_1, top_left_y + row_height[0] * 4, area_start_x_4_3,
                top_left_y + row_height[0] * 5, "类型：" + printData.getType_name(),
                24, CanvasUtils.PAlign.ALIGN_LEFT, 0);

        utils.drawText(area_start_x_4_3 + 4, top_left_y + row_height[0] * 4, top_right_x,
                top_left_y + row_height[0] * 5, "余量：" + printData.getLast_qty(),
                24, CanvasUtils.PAlign.ALIGN_CENTER, 0);


        // 打印表格头
        utils.drawText(area_start_x_4_1, top_left_y + row_height[0] * 5, area_start_x_4_2,
                top_left_y + row_height[0] * 6, "种区",
                24, CanvasUtils.PAlign.ALIGN_CENTER, 0);

        utils.drawText(area_start_x_4_2, top_left_y + row_height[0] * 5,
                area_start_x_4_3, top_left_y + row_height[0] * 6, "种号",
                24, CanvasUtils.PAlign.ALIGN_CENTER, 0);
        utils.drawText(area_start_x_4_3, top_left_y + row_height[0] * 5,
                area_start_x_4_4, top_left_y + row_height[0] * 6, "应播数量",
                24, CanvasUtils.PAlign.ALIGN_CENTER, 0);
        utils.drawText(area_start_x_4_4, top_left_y + row_height[0] * 5,
                top_right_x, top_left_y + row_height[0] * 6, " 实播数量",
                24, CanvasUtils.PAlign.ALIGN_CENTER, 0);

        //打印表格内容
//        List<PrintData2.TraySowDtlsBean> traySowDtls = printData.getTraySowDtls();
        for (int index = 0; index < traySowDtls.size(); index++) {
            utils.drawText(area_start_x_4_1, top_left_y + row_height[0] * (6 + index), area_start_x_4_2,
                    top_left_y + row_height[0] * (7 + index), traySowDtls.get(index).getZone_id(),
                    24, CanvasUtils.PAlign.ALIGN_CENTER, 0);

            utils.drawText(area_start_x_4_2, top_left_y + row_height[0] * (6 + index),
                    area_start_x_4_3, top_left_y + row_height[0] * (7 + index), traySowDtls.get(index).getSow_id(),
                    24, CanvasUtils.PAlign.ALIGN_CENTER, 0);
            utils.drawText(area_start_x_4_3, top_left_y + row_height[0] * (6 + index),
                    area_start_x_4_4, top_left_y + row_height[0] * (7 + index), traySowDtls.get(index).getQty(),
                    24, CanvasUtils.PAlign.ALIGN_CENTER, 0);
            utils.drawText(area_start_x_4_4, top_left_y + row_height[0] * (6 + index),
                    top_right_x, top_left_y + row_height[0] * (7 + index), " ",
                    24, CanvasUtils.PAlign.ALIGN_CENTER, 0);
        }
    }

    private void drawHorizontalSeparator(CanvasUtils utils, int lines, int start_x, int end_x) {

        int temp = top_left_y + row_height[0] * 5;
        for (int i = 0; i < lines; i++) {
            temp += row_height[0];
            utils.drawLine(start_x, temp, end_x, temp);
        }
    }

    //
    private void drawVerticalSeparator(CanvasUtils utils) {

        int start_x = top_left_x + border_width / 4;
        int start_y = top_left_y + row_height[0] * 5;
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
        utils.drawRectangle(top_left_x, top_left_y + row_height[0] * 5, bottom_right_x, bottom_right_y);
        drawHorizontalSeparator(utils, lines, top_left_x, bottom_right_x);
    }

}
