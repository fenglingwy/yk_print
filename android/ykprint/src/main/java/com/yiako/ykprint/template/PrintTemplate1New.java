package com.yiako.ykprint.template;


import com.dascom.print.ESCPOS;
import com.yiako.ykprint.entity.PrintData1;

import java.util.List;

public class PrintTemplate1New {
    private ESCPOS escpos;

    public PrintTemplate1New(ESCPOS escpos) {
        super();
        this.escpos = escpos;
    }

    public final void doPrint(List<PrintData1> list) {

        new Thread() {
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    print(list.get(i));
                }
            }
        }.start();
    }

    private void print(PrintData1 printData1) {
        escpos.setJustification(Constants.TEXT_ALIGN_CENTER);
        escpos.setEmphasizedMode(true);
        escpos.setCharacterSize(1,1);
        escpos.printText("入库装箱单",0,0);
        escpos.printLineFeed();
        escpos.setJustification(Constants.TEXT_ALIGN_LEFT);
        escpos.setEmphasizedMode(false);
        escpos.setCharacterSize(0,0);
        escpos.printText("箱号：",0,10);
        printCode128(180,printData1.getTray_serlno());
        escpos.printLineFeed();

        escpos.setLineSpacing(50) ;

        escpos.printText("商家编码："+printData1.getBarcode(),0,0);

        escpos.setAbsolutePosition(400);
        escpos.printText("数量："+printData1.getQty());
        escpos.printLineFeed();

        escpos.printText("品名："+printData1.getItem_name(),0,0);
        escpos.printLineFeed();

        escpos.printText("供应商："+printData1.getSupplyer(),0,0);
        escpos.printLineFeed();

        escpos.printText("验收单号： ",0,0);
        printCode128(180, printData1.getReserved_no());
        escpos.printLineFeed();

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

        escpos.printText("主配标记："+mark,0,0);
        escpos.printFeedLines(2);
        escpos.setJustification(Constants.TEXT_ALIGN_CENTER);
        escpos.printText("---------------------------------------------");
        escpos.printFeedLines(2);

    }

    private void printCode128(int marginLeft ,String code) {
        escpos.setAbsolutePosition(marginLeft);
        escpos.printCode128Setting(80,1, (byte)'0', (byte)'0');
        escpos.printCode128((byte) 'B',code);
        escpos.printText(code,marginLeft+20,2);
    }




}
