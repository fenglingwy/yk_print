package com.yiako.ykprint.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PrintData2 {
    /**
     * Barcode :
     * type_name :
     * last_qty :
     * traySowDtls : [{"zone_id":"","sow_id":"","qty":""}]
     *
     *     * barcode: " 1234567890"
     *      * main_distribution_type: 0
     *      * remain_qty: 2000
     *      * sow_zone_qty_list: Array(1)
     *      * 0: {zone_num_id: 232000, sow_num_id: 232001, qty: 0}
     */

    @SerializedName("item_num_id")
    private String Barcode= "";
    private String itemid= "";
    @SerializedName("main_distribution_type")
    private String type_name= "";
    @SerializedName("remain_qty")
    private String last_qty= "";
    private String amount= "";

    private String case_num_id= "";
    @SerializedName("sow_zone_qty_list")
    private List<TraySowDtlsBean> traySowDtls = new ArrayList<>();

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String Barcode) {
        this.Barcode = Barcode;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getLast_qty() {
        return last_qty;
    }

    public void setLast_qty(String last_qty) {
        this.last_qty = last_qty;
    }

    public List<TraySowDtlsBean> getTraySowDtls() {
        return traySowDtls;
    }

    public String getCase_num_id() {
        return case_num_id;
    }

    public void setCase_num_id(String case_num_id) {
        this.case_num_id = case_num_id;
    }

    public void setTraySowDtls(List<TraySowDtlsBean> traySowDtls) {
        this.traySowDtls = traySowDtls;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public static class TraySowDtlsBean {
        /**
         * zone_id :
         * sow_id :
         * qty :
         * {zone_num_id: 232000, sow_num_id: 232001, qty: 0}
         */

        @SerializedName("zone_num_id")
        private String zone_id = "";
        @SerializedName("sow_num_id")
        private String sow_id = "";
        private String qty = "";

        public String getZone_id() {
            return zone_id;
        }

        public void setZone_id(String zone_id) {
            this.zone_id = zone_id;
        }

        public String getSow_id() {
            return sow_id;
        }

        public void setSow_id(String sow_id) {
            this.sow_id = sow_id;
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
     * Barcode  String  商品条码
     * type_name  String  主配类型
     * last_qty  Double  剩余数量
     * List<traySowDtl> traySowDtls  traySowDtl  箱分货明细
     * traySowDtl-> zone_id  String   种区简码
     * traySowDtl-> sow_id  String   种号简码
     * traySowDtl-> qty  Double  应捡数量
     *
     * '{"code":0,"message":"","Barcode":"","type_name":"","last_qty":"","traySowDtls":[{"zone_id":"S","sow_id":"91","qty":"20"},{"zone_id":"A","sow_id":"11","qty":"21"},{"zone_id":"A","sow_id":"1","qty":"2"},{"zone_id":"C","sow_id":"32","qty":"1"},{"zone_id":"D","sow_id":"11","qty":"211"},{"zone_id":"F","sow_id":"12","qty":"11"},]}'
     */


}
