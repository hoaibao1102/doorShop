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
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

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
            st.setInt(2, e.getDiscount_percent());

            if (e.getStart_date() != null) {
                st.setDate(3, e.getStart_date());
            } else {
                st.setNull(3, Types.DATE);
            }

            if (e.getEnd_date() != null) {
                st.setDate(4, e.getEnd_date());
            } else {
                st.setNull(4, Types.DATE);
            }

            st.setString(5, e.getStatus());

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

    public List<Discounts> getActiveDiscount() {
        List<Discounts> list = new ArrayList<>();
        String sql = "SELECT * FROM Discounts WHERE status = 'active' AND end_date >= CAST(GETDATE() AS DATE)";
        Connection c = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(sql);
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

    public Discounts getByProductId(int productId) {
        String sql = "SELECT * FROM Discounts WHERE product_id = ? AND status = 'active'";
        Connection c = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(sql);
            rs = st.executeQuery();
            if (rs.next()) {
                Discounts d = new Discounts();
                d.setDiscount_id(rs.getInt("discount_id"));
                d.setProduct_id(rs.getInt("product_id"));
                d.setDiscount_percent(rs.getInt("discount_percent"));
                d.setStart_date(rs.getDate("start_date"));
                d.setEnd_date(rs.getDate("end_date"));
                d.setStatus(rs.getString("status"));
                return d;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(c, st, rs);
        }
        return null;
    }

    private Discounts map(ResultSet rs) throws SQLException {
        Discounts d = new Discounts();
        d.setDiscount_id(rs.getInt("discount_id"));
        d.setProduct_id(rs.getInt("product_id"));
        d.setDiscount_percent(rs.getInt("discount_percent"));

        java.sql.Date start = rs.getDate("start_date");
        if (start != null) {
            d.setStart_date(start);
        }

        java.sql.Date end = rs.getDate("end_date");
        if (end != null) {
            d.setEnd_date(end);
        }

        d.setStatus(rs.getString("status"));
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
