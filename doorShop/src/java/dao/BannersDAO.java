/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dto.Banners;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

/**
 *
 * @author MSI PC
 */
public class BannersDAO implements IDAO<Banners, Integer> {

    private static final String GET_ALL = "SELECT * FROM dbo.Banners";
    private static final String GET_BY_ID = "SELECT * FROM dbo.Banners WHERE banner_id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM dbo.Banners WHERE title LIKE ?";
    private static final String CREATE
            = "INSERT INTO dbo.Banners (title, media_id, status) VALUES (?, ?, ?)";

    @Override
    public boolean create(Banners e) {
        Connection c = null;
        PreparedStatement st = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(CREATE);
            st.setString(1, e.getTitle());
            st.setInt(2, e.getMedia_id());
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
    public Banners getById(Integer id) {
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
    public List<Banners> getByName(String name) {
        List<Banners> list = new ArrayList<>();
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
    public List<Banners> getAll() {
        List<Banners> list = new ArrayList<>();
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

    public List<Banners> getVisibleBanners() {
        List<Banners> list = new ArrayList<>();
        String sql = "SELECT * FROM Banners WHERE status = 'visible'";
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(c, st, rs);
        }
        return list;
    }

    private Banners map(ResultSet rs) throws SQLException {
        Banners b = new Banners();
        b.setBanner_id(rs.getInt("banner_id"));
        b.setTitle(rs.getString("title"));
        b.setMedia_id(rs.getInt("media_id"));
        b.setStatus(rs.getString("status"));

        return b;
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
