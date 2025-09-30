/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.Categories;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author MSI PC
 */
public class CategoriesDAO implements IDAO<Categories, Integer> {

    private static final String GET_ALL = "SELECT * FROM dbo.Categories";
    private static final String GET_BY_ID = "SELECT * FROM dbo.Categories WHERE category_id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM dbo.Categories WHERE category_name LIKE ?";
    private static final String CREATE
            = "INSERT INTO dbo.Categories (category_name, description, status) VALUES (?, ?, ?)";

    @Override
    public boolean create(Categories e) {
        Connection c = null;
        PreparedStatement st = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(CREATE);
            st.setString(1, e.getCategory_name());
            st.setString(2, e.getDescription());
            st.setString(3, e.getStatus());
            return st.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(c, st, null);
        }
    }

    @Override
    public Categories getById(Integer id) {
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
    public List<Categories> getByName(String name) {
        List<Categories> list = new ArrayList<>();
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
    public List<Categories> getAll() {
        List<Categories> list = new ArrayList<>();
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

    private Categories map(ResultSet rs) throws SQLException {
        Categories c = new Categories();
        c.setCategory_id(rs.getInt("category_id"));
        c.setCategory_name(rs.getString("category_name"));
        c.setDescription(rs.getString("description"));
        c.setStatus(rs.getString("status"));
        return c;
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
