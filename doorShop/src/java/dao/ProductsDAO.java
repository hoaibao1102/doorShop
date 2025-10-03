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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class ProductsDAO implements IDAO<Products, Integer> {

    private static final String GET_ALL = "SELECT * FROM dbo.Products";
    private static final String GET_BY_ID = "SELECT * FROM dbo.Products WHERE product_id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM dbo.Products WHERE name LIKE ?";
    private static final String CREATE

            = "INSERT INTO dbo.Products (category_id, name, sku, price, short_desc, spec_html, main_image, status) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"; 


    @Override
    public boolean create(Products e) {
        Connection c = null;
        PreparedStatement st = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(CREATE);
            st.setInt(1, e.getCategory_id());
            st.setString(2, e.getName());
            st.setString(3, e.getSku());
            st.setDouble(4, e.getPrice());
            st.setString(5, e.getShort_desc());
            st.setString(6, e.getSpec_html());
            st.setString(7, e.getMain_image());
            st.setString(8, e.getStatus());

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
            System.out.println("Searching: %" + name + "%");
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
    
    public List<Products> getByCategoryId(int categoryId) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE category_id = ?";
        Connection c = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(sql);
            st.setInt(1, categoryId);
            rs = st.executeQuery();
            while (rs.next()){
                list.add(map(rs));
            }
        }catch (Exception ex) {
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
        p.setName(rs.getString("name"));
        p.setSku(rs.getString("sku"));
        p.setPrice(rs.getDouble("price"));
        p.setShort_desc(rs.getString("short_desc"));
        p.setSpec_html(rs.getString("spec_html"));
        p.setMain_image(rs.getString("main_image"));
        p.setStatus(rs.getString("status"));

        // created_at
        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            p.setCreated_at(new java.util.Date(createdTs.getTime()));
        }

        // updated_at
        Timestamp updatedTs = rs.getTimestamp("updated_at");
        if (updatedTs != null) {
            p.setUpdated_at(new java.util.Date(updatedTs.getTime()));
        }

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
