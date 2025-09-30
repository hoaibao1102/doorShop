/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;
import utils.PasswordUtils;

/**
 *
 * @author MSI PC
 */
public class AdminDAO implements IDAO<Admin, Integer> {

    private static final String GET_ALL = "SELECT * FROM dbo.Admins";
    private static final String GET_BY_ID = "SELECT * FROM dbo.Admins WHERE admin_id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM dbo.Admins WHERE username LIKE ?";
    private static final String CREATE
            = "INSERT INTO dbo.Admins (username, password_hash, email, phone) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_PASSWORD_BY_USERNAME
            = "UPDATE dbo.Admins SET password_hash = ? WHERE username = ?";

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
            st.setString(4, e.getPhone());
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
        a.setPhone(rs.getString("phone"));

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
        try {
            Connection conn = DBUtils.getConnection();
            PreparedStatement ps = conn.prepareStatement(UPDATE_PASSWORD_BY_USERNAME);
            ps.setString(1, newPassword);
            ps.setString(2, userName);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Admin getByUsername(String username) {
        Connection c = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            c = DBUtils.getConnection();
            String sql = "SELECT * FROM dbo.Admins WHERE username = ?";
            st = c.prepareStatement(sql);
            st.setString(1, username);
            rs = st.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(c, st, rs);
        }
        return null;
    }

    public boolean login(String userName, String rawPassword) {
        Admin admin = getByUsername(userName);
        if (admin == null) {
            return false;
        }

        String inputHash = PasswordUtils.encryptSHA256(rawPassword);

        return inputHash.equalsIgnoreCase(admin.getPassword_hash());
    }
}
