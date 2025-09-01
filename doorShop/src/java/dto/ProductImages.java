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
public class ProductImages {
    private int image_id;
    private int product_id;
    private String image_url;
    private Date caption;
    private Date created_at;
    private int status;

    public ProductImages() {
    }

    public ProductImages(int image_id, int product_id, String image_url, Date caption, Date created_at, int status) {
        this.image_id = image_id;
        this.product_id = product_id;
        this.image_url = image_url;
        this.caption = caption;
        this.created_at = created_at;
        this.status = status;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Date getCaption() {
        return caption;
    }

    public void setCaption(Date caption) {
        this.caption = caption;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
}
