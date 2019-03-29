package com.yiako.ykprint.entity;

import com.google.gson.annotations.SerializedName;

public class PrintData3 {

    /**
     * code : 0
     * container_labserlno : 190226000004
     * full_message : null
     * message : 成功
     * physical_num_id : 1167
     * rec_date : 1550678400000
     * rec_physical_num_id : 300101
     * reserved_no : 131902260040001
     */

    private String code = "";
    private String container_labserlno= "";
    private Object full_message= "";
    private String message= "";
    private String physical_num_id= "";
    private String rec_date= "";
    private String rec_physical_num_id= "";
    private String reserved_no= "";

    @SerializedName("ship_tran_company")
    private String shipTranCompany= "";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContainer_labserlno() {
        return container_labserlno;
    }

    public void setContainer_labserlno(String container_labserlno) {
        this.container_labserlno = container_labserlno;
    }

    public Object getFull_message() {
        return full_message;
    }

    public void setFull_message(Object full_message) {
        this.full_message = full_message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhysical_num_id() {
        return physical_num_id;
    }

    public void setPhysical_num_id(String physical_num_id) {
        this.physical_num_id = physical_num_id;
    }

    public String getRec_date() {
        return rec_date;
    }

    public void setRec_date(String rec_date) {
        this.rec_date = rec_date;
    }

    public String getRec_physical_num_id() {
        return rec_physical_num_id;
    }

    public void setRec_physical_num_id(String rec_physical_num_id) {
        this.rec_physical_num_id = rec_physical_num_id;
    }

    public String getReserved_no() {
        return reserved_no;
    }

    public void setReserved_no(String reserved_no) {
        this.reserved_no = reserved_no;
    }

    public String getShipTranCompany() {
        return shipTranCompany;
    }

    public void setShipTranCompany(String shipTranCompany) {
        this.shipTranCompany = shipTranCompany;
    }
}
