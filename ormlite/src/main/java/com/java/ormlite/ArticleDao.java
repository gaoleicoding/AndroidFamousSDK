package com.java.ormlite;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * 操作article表的DAO类
 */
public class ArticleDao {
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao dao;

    public ArticleDao(Context context) {
        try {
            this.dao = DatabaseHelper.getInstance(context).getDao(ArticleBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 添加数据
    public void insert(ArticleBean data) {
        try {
            dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除数据
    public void delete(ArticleBean data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修改数据
    public void update(ArticleBean data) {
        try {
            dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 通过ID查询一条数据
    public ArticleBean queryById(int id) {
        ArticleBean article = null;
        try {
            article = (ArticleBean) dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    public List queryByTitle(String query) throws SQLException {//查询
        QueryBuilder builder = dao.queryBuilder();
        return builder.where().eq("title", query).query();
    }

    // 通过条件查询文章集合（通过用户ID查找）
    public List queryByUserId(int user_id) {
        try {
            return dao.queryBuilder().where().eq(ArticleBean.COLUMNNAME_USER, user_id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}