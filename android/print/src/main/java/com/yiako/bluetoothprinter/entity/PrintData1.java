package com.yiako.bluetoothprinter.entity;

public class PrintData1 {
    /**
     * code : 0
     * message :
     * traySerlnoPrintInfo : {"reserved_no":"","tray_serlno":"","item_num_id":"","barcode":"","item_name":"","qty":""}
     */

    private int code;
    private String message;
    private TraySerlnoPrintInfoBean traySerlnoPrintInfo;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TraySerlnoPrintInfoBean getTraySerlnoPrintInfo() {
        return traySerlnoPrintInfo;
    }

    public void setTraySerlnoPrintInfo(TraySerlnoPrintInfoBean traySerlnoPrintInfo) {
        this.traySerlnoPrintInfo = traySerlnoPrintInfo;
    }

    public static class TraySerlnoPrintInfoBean {
        /**
         * reserved_no :
         * tray_serlno :
         * item_num_id :
         * barcode :
         * item_name :
         * qty :
         */

        private String reserved_no;
        private String tray_serlno;
        private String item_num_id;
        private String barcode;
        private String item_name;
        private String qty;

        public String getReserved_no() {
            return reserved_no;
        }

        public void setReserved_no(String reserved_no) {
            this.reserved_no = reserved_no;
        }

        public String getTray_serlno() {
            return tray_serlno;
        }

        public void setTray_serlno(String tray_serlno) {
            this.tray_serlno = tray_serlno;
        }

        public String getItem_num_id() {
            return item_num_id;
        }

        public void setItem_num_id(String item_num_id) {
            this.item_num_id = item_num_id;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }
    }
    /**
     * code  Number  0表示执行成功
     * -1表示系统异常
     * message  String  描述
     * traySerlnoPrintInfo  Object  箱标签打印信息
     *   reserved_no  String  验收单号
     *   tray_serlno  String  箱号标签
     *   item_num_id  Number  商品编码
     *   barcode  String  商品条码
     *   item_name  String  商品名称
     *   qty  Number  数量
     *   '{"code":0,"message":"","traySerlnoPrintInfo":{"reserved_no":"",tray_serlno:"","item_num_id":"","barcode":"","item_name":"","qty":""}}'
     */


}
