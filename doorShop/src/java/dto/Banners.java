/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author MSI PC
 */
public class Banners {
    private int banner_id;
    private String title;
    private int media_id;
    private String status;

    public Banners() {
    }

    public Banners(int banner_id, String title, int media_id, String status) {
        this.banner_id = banner_id;
        this.title = title;
        this.media_id = media_id;
        this.status = status;
    }

    public int getBanner_id() {
        return banner_id;
    }

    public void setBanner_id(int banner_id) {
        this.banner_id = banner_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMedia_id() {
        return media_id;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
