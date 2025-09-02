/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.Admin;
import utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.PasswordUtils;

/**
 *
 * @author MSI PC
 */
public class AdminDAO implements IDAO<Admin, Integer> {

    private static final String GET_ALL = "SELECT * FROM dbo.Admin";
    private static final String GET_BY_ID = "SELECT * FROM dbo.Admin WHERE admin_id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM dbo.Admin WHERE username LIKE ?";
    private static final String CREATE
            = "INSERT INTO dbo.Admin (username, password_hash, email, full_name, phone) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_PASSWORD_BY_USERNAME =
        "UPDATE dbo.Admin SET password_hash = ? WHERE username = ?";
    
    @Override
    public boolean create(Admin e) {
        Connection c = null;
        PreparedStatement st = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(CREATE);
            st.setString(1, e.getUsername());
            st.setString(2, e.getPassword_hash());
            st.setString(3, e.getEmail());
            st.setString(4, e.getFull_name());
            st.setString(5, e.getPhone());
            return st.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(c, st, null);
        }
    }

    @Override
    public Admin getById(Integer id) {
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
    public List<Admin> getByName(String name) {
        List<Admin> list = new ArrayList<>();
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
    public List<Admin> getAll() {
        List<Admin> list = new ArrayList<>();
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

    private Admin map(ResultSet rs) throws SQLException {
        Admin a = new Admin();
        a.setAdmin_id(rs.getInt("admin_id"));
        a.setUsername(rs.getString("username"));
        a.setPassword_hash(rs.getString("password_hash"));
        a.setEmail(rs.getString("email"));
        a.setFull_name(rs.getString("full_name"));
        a.setPhone(rs.getString("phone"));

        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            a.setCreated_at(new java.util.Date(createdTs.getTime()));
        }

        return a;
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

    public boolean updatePassword(String userName, String newPassword) {
        Connection c = null;
        PreparedStatement st = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(UPDATE_PASSWORD_BY_USERNAME);

            // Hash trước khi lưu
            String hashed = PasswordUtils.encryptSHA256(newPassword);
            st.setString(1, hashed);
            st.setString(2, userName);

            return st.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(c, st, null);
        }
    }
}
