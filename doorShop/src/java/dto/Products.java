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
    private int brand_id;
    private String name;
    private double price;
    private String spec_html;
    private int main_image_id;
    private String status;
    private Date created_at;
    private Date updated_at;
    private ProductImages image;

    public ProductImages getImage() {
        return image;
    }

    public void setImage(ProductImages image) {
        this.image = image;
    }

    public Products() {
    }

    public Products(int product_id, int category_id, int brand_id, String name, double price, String spec_html, int main_image_id, String status, Date created_at, Date updated_at) {
        this.product_id = product_id;
        this.category_id = category_id;
        this.brand_id = brand_id;
        this.name = name;
        this.price = price;
        this.spec_html = spec_html;
        this.main_image_id = main_image_id;
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

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
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

    public String getSpec_html() {
        return spec_html;
    }

    public void setSpec_html(String spec_html) {
        this.spec_html = spec_html;
    }

    public int getMain_image_id() {
        return main_image_id;
    }

    public void setMain_image_id(int main_image_id) {
        this.main_image_id = main_image_id;
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

}
