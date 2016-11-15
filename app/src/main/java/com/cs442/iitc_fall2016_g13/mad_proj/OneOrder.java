package com.cs442.iitc_fall2016_g13.mad_proj;

/**
 * Created by lyd on 2016/11/10.
 */

public class OneOrder {

    String order_id;
    String rest_id;
    String cust_id;
    String menu_list;
    String Status;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRest_id() {
        return rest_id;
    }

    public void setRest_id(String rest_id) {
        this.rest_id = rest_id;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getMenu_list() {
        return menu_list;
    }

    public void setMenu_list(String menu_list) {
        this.menu_list = menu_list;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
