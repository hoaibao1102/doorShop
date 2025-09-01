/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.Date;

/**
 *
 * @author MSI PC
 */
public class Discounts {
    private int discount_id;
    private int product_id;
    private double discount_percent;
    private Date start_date;
    private Date end_date;
    private int status;

    public Discounts() {
    }

    public Discounts(int discount_id, int product_id, double discount_percent, Date start_date, Date end_date, int status) {
        this.discount_id = discount_id;
        this.product_id = product_id;
        this.discount_percent = discount_percent;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int discount_id) {
        this.discount_id = discount_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public double getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(double discount_percent) {
        this.discount_percent = discount_percent;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    

}
