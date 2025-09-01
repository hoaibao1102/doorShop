/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author MSI PC
 */
import dto.Products;
import utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDAO implements IDAO<Products, Integer> {

    private static final String GET_ALL = "SELECT * FROM dbo.Products";
    private static final String GET_BY_ID = "SELECT * FROM dbo.Products WHERE product_id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM dbo.Products WHERE name LIKE ?";
    private static final String CREATE
            = "INSERT INTO dbo.Products (category_id, brand_id, name, price, spec_html, main_image_id, caption, status) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public boolean create(Products e) {
        Connection c = null;
        PreparedStatement st = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(CREATE);
            st.setInt(1, e.getCategory_id());
            st.setInt(2, e.getBrand_id());
            st.setString(3, e.getName());
            st.setDouble(4, e.getPrice());
            st.setString(5, e.getSpec_html());
            st.setInt(6, e.getMain_image_id());

            if (e.getCaption() != null) {
                st.setTimestamp(7, new Timestamp(e.getCaption().getTime()));
            } else {
                st.setNull(7, Types.TIMESTAMP);
            }

            st.setInt(8, e.getStatus());

            return st.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(c, st, null);
        }
    }

    @Override
    public Products getById(Integer id) {
        Connection c = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(GET_BY_ID);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(c, st, rs);
        }
        return null;
    }

    @Override
    public List<Products> getByName(String name) {
        List<Products> list = new ArrayList<>();
        Connection c = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(GET_BY_NAME);
            st.setString(1, "%" + name + "%");
            rs = st.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(c, st, rs);
        }
        return list;
    }

    @Override
    public List<Products> getAll() {
        List<Products> list = new ArrayList<>();
        Connection c = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(GET_ALL);
            rs = st.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(c, st, rs);
        }
        return list;
    }

    private Products map(ResultSet rs) throws SQLException {
        Products p = new Products();
        p.setProduct_id(rs.getInt("product_id"));
        p.setCategory_id(rs.getInt("category_id"));
        p.setBrand_id(rs.getInt("brand_id"));
        p.setName(rs.getString("name"));
        p.setPrice(rs.getDouble("price"));
        p.setSpec_html(rs.getString("spec_html"));
        p.setMain_image_id(rs.getInt("main_image_id"));

        Timestamp captionTs = rs.getTimestamp("caption");
        if (captionTs != null) {
            p.setCaption(new java.util.Date(captionTs.getTime()));
        }

        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            p.setCreated_at(new java.util.Date(createdTs.getTime()));
        }

        p.setStatus(rs.getInt("status"));

        return p;
    }

    private void close(Connection c, PreparedStatement st, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception ignore) {
        }
        try {
            if (st != null) {
                st.close();
            }
        } catch (Exception ignore) {
        }
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception ignore) {
        }
    }
}
