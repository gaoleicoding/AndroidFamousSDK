package com.java.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * ArticleBean实体类，存储article数据表中的数据
 * 数据库中的article表和user表是关联的，因此我们需要在article表中配置外键
 * <p>
 * foreignColumnName：外键约束指向的类中的属性名
 * foreign：当前字段是否是外键
 * foreignAutoRefresh：如果这个属性设置为true，在关联查询的时候就不需要再调用refresh()方法了
 */
@DatabaseTable(tableName = "article")
public class ArticleBean {
    // article表中各个字段的名称
    public static final String COLUMNNAME_ID = "id";
    public static final String COLUMNNAME_TITLE = "title";
    public static final String COLUMNNAME_CONTENT = "content";
    public static final String COLUMNNAME_USER = "user_id";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false, unique = true)
    private String title;
    @DatabaseField
    private String content;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true,
            foreignColumnName = UserBean.COLUMNNAME_ID)
    private UserBean user;

    public ArticleBean() {
    }

    public ArticleBean(String title, String content, UserBean user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ArticleBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", user=" + user +
                '}';
    }
}