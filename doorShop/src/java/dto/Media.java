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
public class Media {
    private int media_id;
    private String file_name;
    private String file_path;
    private Date uploaded_at;
    private Integer uploaded_by;

    public Media() {
    }

    public Media(int media_id, String file_name, String file_path, Date uploaded_at, Integer uploaded_by) {
        this.media_id = media_id;
        this.file_name = file_name;
        this.file_path = file_path;
        this.uploaded_at = uploaded_at;
        this.uploaded_by = uploaded_by;
    }

    public int getMedia_id() {
        return media_id;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public Date getUploaded_at() {
        return uploaded_at;
    }

    public void setUploaded_at(Date uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    public Integer getUploaded_by() {
        return uploaded_by;
    }

    public void setUploaded_by(Integer uploaded_by) {
        this.uploaded_by = uploaded_by;
    }

}