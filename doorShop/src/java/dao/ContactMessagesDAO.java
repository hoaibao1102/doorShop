/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author MSI PC
 */
import dto.ContactMessages;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class ContactMessagesDAO implements IDAO<ContactMessages, Integer> {

    private static final String GET_ALL = "SELECT * FROM dbo.ContactMessages";
    private static final String GET_BY_ID = "SELECT * FROM dbo.ContactMessages WHERE message_id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM dbo.ContactMessages WHERE name LIKE ?";
    private static final String CREATE
            = "INSERT INTO dbo.ContactMessages (name, email, phone, subject, message, status) VALUES (?, ?, ?, ?, ?, ?)";

    @Override
    public boolean create(ContactMessages e) {
        Connection c = null;
        PreparedStatement st = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(CREATE);
            st.setString(1, e.getName());
            st.setString(2, e.getEmail());
            st.setString(3, e.getPhone());
            st.setString(4, e.getSubject());
            st.setString(5, e.getMessage());
            st.setString(6, e.getStatus());
            return st.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(c, st, null);
        }
    }

    @Override
    public ContactMessages getById(Integer id) {
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
    public List<ContactMessages> getByName(String name) {
        List<ContactMessages> list = new ArrayList<>();
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
    public List<ContactMessages> getAll() {
        List<ContactMessages> list = new ArrayList<>();
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

    private ContactMessages map(ResultSet rs) throws SQLException {
        ContactMessages cm = new ContactMessages();
        cm.setMessage_id(rs.getInt("message_id"));
        cm.setName(rs.getString("name"));
        cm.setEmail(rs.getString("email"));
        cm.setPhone(rs.getString("phone"));
        cm.setSubject(rs.getString("subject"));
        cm.setMessage(rs.getString("message"));
        cm.setStatus(rs.getString("status"));

        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            cm.setCreated_at(new java.util.Date(createdTs.getTime()));
        }

        return cm;
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
