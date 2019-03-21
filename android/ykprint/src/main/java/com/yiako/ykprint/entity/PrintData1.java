package com.yiako.ykprint.entity;

import com.google.gson.annotations.SerializedName;

public class PrintData1 {
    /**
     * reserved_no :
     * tray_serlno :
     * item_num_id :
     * barcode :
     * item_name :
     * qty :
     *
     * barcode: "2100000000101"
     * item_name: "测试商品101"
     * item_num_id: 101
     * mainMark: ""
     * main_distribution_type: 0
     * qty: 10
     * reserved_no: "191902250080002"
     * statuName: "本箱完成"
     * status_num_id: 2
     * tray_serlno: "190227000004"
     * tray_serlno_label: "190227000004"
     *
     */

    private String reserved_no = "";
    @SerializedName("tray_serlno_label")
    private String tray_serlno = "";
    private String item_num_id= "";
    private String barcode= "";
    private String item_name= "";
    private String qty= "";
    private String main_distribution_type = "";
    private String supplyer = "";

    public String getSupplyer() {
        return supplyer;
    }

    public void setSupplyer(String supplyer) {
        this.supplyer = supplyer;
    }



    public String getMain_distribution_type() {
        return main_distribution_type;
    }

    public void setMain_distribution_type(String main_distribution_type) {
        this.main_distribution_type = main_distribution_type;
    }

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
