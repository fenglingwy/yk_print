package com.yiako.ykprint.template;

import com.blankj.utilcode.util.ToastUtils;
import com.dascom.print.ESCPOS;
import com.yiako.ykprint.bt.CanvasUtils;
import com.yiako.ykprint.entity.PrintData3;

public class PrintTemplate3New {
    private ESCPOS escpos;

    private static int MULTIPLE = 8;
    private static final int page_width = 70 * MULTIPLE;
    private static final int page_height = 79 * MULTIPLE;
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
    private static final int[] row_height = {6 * MULTIPLE, 14 * MULTIPLE};

    public PrintTemplate3New(ESCPOS escpos) {
        super();
        this.escpos = escpos;
    }

    public void doPrint(PrintData3 printData) {
        new Thread() {
            public void run() {
                print(printData);
            }
        }.start();

    }

    private void print(PrintData3 printData) {
        escpos.setJustification(Constants.TEXT_ALIGN_CENTER);

        escpos.setEmphasizedMode(true);
        escpos.setCharacterSize(1, 1);
        escpos.printText("出库箱唛头", 0, 0);
        escpos.printLineFeed();

        escpos.setJustification(Constants.TEXT_ALIGN_LEFT);
        escpos.setEmphasizedMode(false);
        escpos.setCharacterSize(0, 0);
        escpos.printText("出库单号：", 0, 10);
        printCode128(180, printData.getReserved_no());
        escpos.printLineFeed();

        escpos.printText("发货仓:" + printData.getPhysical_num_id(), 0, 0);

        escpos.setEmphasizedMode(true);
        escpos.setCharacterSize(1, 1);
        escpos.printText("收货仓:" + printData.getRec_physical_num_id(), 0, 20);

        escpos.printText("物流公司:" + printData.getShipTranCompany(), 0, 10);
        escpos.printLineFeed();

        escpos.setEmphasizedMode(false);
        escpos.setCharacterSize(0, 0);

        escpos.printText("发货日期：" + printData.getRec_date(), 0, 20);
        escpos.printLineFeed();

        escpos.printText("出库箱号： ", 0, 0);
        printCode128(180, printData.getContainer_labserlno());


        escpos.printFeedLines(2);
        escpos.setJustification(Constants.TEXT_ALIGN_CENTER);
        escpos.printText("---------------------------------------------");
        escpos.printFeedLines(2);

    }


    private void printCode128(int marginLeft, String code) {
        escpos.setAbsolutePosition(marginLeft);
        escpos.printCode128Setting(80, 1, (byte) '0', (byte) '0');
        escpos.printCode128((byte) 'B', code);
        escpos.printText(code, marginLeft + 20, 2);
    }
}
