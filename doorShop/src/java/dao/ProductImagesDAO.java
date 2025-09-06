/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author MSI PC
 */
import dto.ProductImages;
import utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductImagesDAO implements IDAO<ProductImages, Integer> {

    private static final String GET_ALL = "SELECT * FROM dbo.ProductImages";
    private static final String GET_BY_ID = "SELECT * FROM dbo.ProductImages WHERE image_id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM dbo.ProductImages WHERE image_url LIKE ?";
    private static final String CREATE
            = "INSERT INTO dbo.ProductImages (product_id, image_url, caption, status) VALUES (?, ?, ?, ?)";

    @Override
    public boolean create(ProductImages e) {
        Connection c = null;
        PreparedStatement st = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(CREATE);
            st.setInt(1, e.getProduct_id());
            st.setString(2, e.getImage_url());
            st.setString(3, e.getCaption());
            st.setInt(4, e.getStatus());

            return st.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(c, st, null);
        }
    }

    @Override
    public ProductImages getById(Integer id) {
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
    public List<ProductImages> getByName(String name) {
        List<ProductImages> list = new ArrayList<>();
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
    public List<ProductImages> getAll() {
        List<ProductImages> list = new ArrayList<>();
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

    private ProductImages map(ResultSet rs) throws SQLException {
        ProductImages pi = new ProductImages();
        pi.setImage_id(rs.getInt("image_id"));
        pi.setProduct_id(rs.getInt("product_id"));
        pi.setImage_url(rs.getString("image_url"));
        pi.setStatus(rs.getInt("status"));

        // created_at
        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            pi.setCreated_at(new java.util.Date(createdTs.getTime()));
        }

        return pi;
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

    public ProductImages getByProductId(int productId) {
        ProductImages img = null;
        Connection c = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        String sql = "SELECT TOP 1 * FROM ProductImages WHERE product_id = ? AND status = '1' ORDER BY created_at ASC";

        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(sql);
            st.setInt(1, productId);
            rs = st.executeQuery();

            if (rs.next()) {
                img = map(rs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(c, st, rs);
        }

        return img;
    }

    public int createAndReturnId(ProductImages e) {
        int generatedId = -1;
        String sql = "INSERT INTO dbo.ProductImages (product_id, image_url, caption, status) VALUES (?, ?, ?, ?)";
        try ( Connection c = DBUtils.getConnection();  PreparedStatement st = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, e.getProduct_id());
            st.setString(2, e.getImage_url());
            st.setString(3, e.getCaption());
            st.setInt(4, e.getStatus());
            int rows = st.executeUpdate();
            if (rows > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    e.setImage_id(generatedId);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return generatedId;
    }

    public List<ProductImages> getListByProductId(int productId) {
        List<ProductImages> list = new ArrayList<>();
        Connection c = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM ProductImages WHERE product_id = ? AND status = '1' ORDER BY created_at ASC";
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(sql);
            st.setInt(1, productId);
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
}
