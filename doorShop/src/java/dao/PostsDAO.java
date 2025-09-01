/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author MSI PC
 */
import dto.Posts;
import utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostsDAO implements IDAO<Posts, Integer> {

    private static final String GET_ALL = "SELECT * FROM dbo.Posts";
    private static final String GET_BY_ID = "SELECT * FROM dbo.Posts WHERE id = ?";
    private static final String GET_BY_NAME = "SELECT * FROM dbo.Posts WHERE title LIKE ?";
    private static final String CREATE
            = "INSERT INTO dbo.Posts (author_id, title, main_image, content, caption, published_at, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

    @Override
    public boolean create(Posts e) {
        Connection c = null;
        PreparedStatement st = null;
        try {
            c = DBUtils.getConnection();
            st = c.prepareStatement(CREATE);
            st.setInt(1, e.getAuthor_id());
            st.setString(2, e.getTitle());
            st.setString(3, e.getMain_image());
            st.setString(4, e.getContent());
            st.setString(5, e.getCaption());

            if (e.getPublished_at() != null) {
                st.setTimestamp(6, new Timestamp(e.getPublished_at().getTime()));
            } else {
                st.setNull(6, Types.TIMESTAMP);
            }

            st.setInt(7, e.getStatus());

            return st.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(c, st, null);
        }
    }

    @Override
    public Posts getById(Integer id) {
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
    public List<Posts> getByName(String name) {
        List<Posts> list = new ArrayList<>();
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
    public List<Posts> getAll() {
        List<Posts> list = new ArrayList<>();
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

    private Posts map(ResultSet rs) throws SQLException {
        Posts p = new Posts();
        p.setId(rs.getInt("id"));
        p.setAuthor_id(rs.getInt("author_id"));
        p.setTitle(rs.getString("title"));
        p.setMain_image(rs.getString("main_image"));
        p.setContent(rs.getString("content"));
        p.setCaption(rs.getString("caption"));

        Timestamp publishedTs = rs.getTimestamp("published_at");
        if (publishedTs != null) {
            p.setPublished_at(new java.util.Date(publishedTs.getTime()));
        }

        Timestamp createdTs = rs.getTimestamp("created_at");
        if (createdTs != null) {
            p.setCreated_at(new java.util.Date(createdTs.getTime()));
        }

        Timestamp updatedTs = rs.getTimestamp("updated_at");
        if (updatedTs != null) {
            p.setUpdated_at(new java.util.Date(updatedTs.getTime()));
        }

        p.setStatus(rs.getInt("status"));

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
