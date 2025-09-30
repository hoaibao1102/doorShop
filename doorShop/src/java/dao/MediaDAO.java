/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author MSI PC
 */
import dto.Media;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class MediaDAO implements IDAO<Media, Integer> {

    private static final String GET_ALL = "SELECT * FROM dbo.Media";
    private static final String GET_BY_ID = "SELECT * FROM dbo.Media WHERE media_id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM dbo.Media WHERE file_name LIKE ?";
    private static final String CREATE
            = "INSERT INTO dbo.Media (file_name, file_path, uploaded_by) VALUES (?, ?, ?)";

    @Override
    public boolean create(Media e) {
        Connection c = null;
        PreparedStatement st = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(CREATE);
            st.setString(1, e.getFile_name());
            st.setString(2, e.getFile_path());
            if (e.getUploaded_by() != null) {
                st.setInt(3, e.getUploaded_by());
            } else {
                st.setNull(3, Types.INTEGER);
            }
            return st.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(c, st, null);
        }
    }

    @Override
    public Media getById(Integer id) {
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
    public List<Media> getByName(String name) {
        List<Media> list = new ArrayList<>();
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
    public List<Media> getAll() {
        List<Media> list = new ArrayList<>();
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

    private Media map(ResultSet rs) throws SQLException {
        Media m = new Media();
        m.setMedia_id(rs.getInt("media_id"));
        m.setFile_name(rs.getString("file_name"));
        m.setFile_path(rs.getString("file_path"));

        Timestamp uploadedTs = rs.getTimestamp("uploaded_at");
        if (uploadedTs != null) {
            m.setUploaded_at(new java.util.Date(uploadedTs.getTime()));
        }

        int uploadedByValue = rs.getInt("uploaded_by");
        if (!rs.wasNull()) {
            m.setUploaded_by(uploadedByValue);
        }

        return m;
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