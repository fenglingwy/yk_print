package com.yiako.ykprint.template;


import com.dascom.print.ESCPOS;
import com.yiako.ykprint.entity.PrintData2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PrintTemplate2New {
    private ESCPOS escpos;

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
                        escpos.printText("本品应播总量：" + data.getAmount());
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
                        int num = 0;
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
                            num +=  Integer.parseInt(traySowDtls.get(lines).getQty());
                        }
                        escpos.setAbsolutePosition(50);
                        escpos.printText("小计");
                        escpos.setAbsolutePosition(340);
                        escpos.printText(""+ num);
                        escpos.printLineFeed();
                        escpos.printFeedLines(5);

                    }
                }


            }
        }.start();
    }

}
