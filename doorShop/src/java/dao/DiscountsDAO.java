/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author MSI PC
 */
import dto.Discounts;
import utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscountsDAO implements IDAO<Discounts, Integer> {

    private static final String GET_ALL = "SELECT * FROM dbo.Discounts";
    private static final String GET_BY_ID = "SELECT * FROM dbo.Discounts WHERE discount_id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM dbo.Discounts WHERE product_id = ?"; // thay product_id thay cho "name"
    private static final String CREATE
            = "INSERT INTO dbo.Discounts (product_id, discount_percent, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";

    @Override
    public boolean create(Discounts e) {
        Connection c = null;
        PreparedStatement st = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(CREATE);
            st.setInt(1, e.getProduct_id());
            st.setDouble(2, e.getDiscount_percent());

            if (e.getStart_date() != null) {
                st.setTimestamp(3, new Timestamp(e.getStart_date().getTime()));
            } else {
                st.setNull(3, Types.TIMESTAMP);
            }

            if (e.getEnd_date() != null) {
                st.setTimestamp(4, new Timestamp(e.getEnd_date().getTime()));
            } else {
                st.setNull(4, Types.TIMESTAMP);
            }

            st.setInt(5, e.getStatus());

            return st.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(c, st, null);
        }
    }

    @Override
    public Discounts getById(Integer id) {
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

    // ở đây getByName không hợp lý, mình đổi thành getByProductId
    @Override
    public List<Discounts> getByName(String productId) {
        List<Discounts> list = new ArrayList<>();
        Connection c = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(GET_BY_NAME);
            st.setInt(1, Integer.parseInt(productId));
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
    public List<Discounts> getAll() {
        List<Discounts> list = new ArrayList<>();
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

    private Discounts map(ResultSet rs) throws SQLException {
        Discounts d = new Discounts();
        d.setDiscount_id(rs.getInt("discount_id"));
        d.setProduct_id(rs.getInt("product_id"));
        d.setDiscount_percent(rs.getDouble("discount_percent"));

        Timestamp start = rs.getTimestamp("start_date");
        if (start != null) {
            d.setStart_date(new java.util.Date(start.getTime()));
        }

        Timestamp end = rs.getTimestamp("end_date");
        if (end != null) {
            d.setEnd_date(new java.util.Date(end.getTime()));
        }

        d.setStatus(rs.getInt("status"));
        return d;
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
