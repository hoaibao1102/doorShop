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
public class Products {

    private int product_id;
    private int category_id;
    private String name;
    private String sku;
    private double price;
    private String short_desc;
    private String spec_html;
    private String main_image;
    private String status;
    private Date created_at;
    private Date updated_at;

    public Products() {
    }

    public Products(int product_id, int category_id, String name,String sku, double price, String short_desc, 
                   String spec_html, String main_image, String status, Date created_at, Date updated_at) {
        this.sku = sku;
        this.product_id = product_id;
        this.category_id = category_id;
        this.name = name;
        this.price = price;
        this.short_desc = short_desc;
        this.spec_html = spec_html;
        this.main_image = main_image;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getSpec_html() {
        return spec_html;
    }

    public void setSpec_html(String spec_html) {
        this.spec_html = spec_html;
    }

    public String getMain_image() {
        return main_image;
    }

    public void setMain_image(String main_image) {
        this.main_image = main_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

}
