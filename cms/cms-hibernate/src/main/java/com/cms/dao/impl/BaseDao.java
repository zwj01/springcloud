package com.cms.dao.impl;

import com.cms.dao.IBaseDao;
import com.cms.model.Pager;
import com.cms.model.SystemContext;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.loader.custom.sql.SQLQueryParser;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;

import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import java.util.Set;

@SuppressWarnings("unchecked")
public class BaseDao<T> implements IBaseDao<T> {

    private SessionFactory sessionFactory;

    /**
     * 创建一个Class对象来获取泛型的Class
     */
    private Class<T> clz;

    @SuppressWarnings("unchecked")
    public Class<T> getClz() {
        if (clz == null) {
            //获取泛型的class对象
            clz = ((Class<T>) (((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]));
        }
        return clz;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.openSession();
    }

    @Inject
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public T add(T t) {
        getSession().save(t);
        return t;
    }

    public void update(T t) {
        getSession().update(t);
    }

    public void delete(int id) {
        getSession().delete(id);
    }

    public T load(int id) {

        return null;
    }

    public List<T> list(String hql, Object[] args) {
        return this.list(hql, args, null);
    }

    public List<T> list(String hql, Object arg) {
        return this.list(hql, new Object[]{arg}, null);
    }

    public List<T> list(String hql) {
        return this.list(hql, null);
    }


    private String initSort(String hql) {

        String order = SystemContext.getOrder();
        String sort = SystemContext.getSort();
        if (sort != null && !"".equals(sort.trim())) {
            hql += " order by " + sort;
            if (!"desc".equals(order)) hql += " asc";
            else hql += " desc";
        }
        return hql;
    }

    @SuppressWarnings("rawtypes")
    private void setAliasParameter(Query query, Map<String, Object> alias) {
        if (alias != null) {
            Set<String> keys = alias.keySet();
            for (String key : keys) {
                Object val = alias.get(key);
                if (val instanceof Collection) {
                    //查询条件是列表
                    query.setParameterList(key, (Collection<Object>) val);
                } else {
                    query.setParameter(key, val);
                }
            }
        }
    }

    private void setParameter(Query query, Object[] args) {
        if (args != null && args.length > 0) {
            int index = 0;
            for (Object arg : args) {
                query.setParameter(index++, arg);
            }

        }
    }


    public List<T> list(String hql, Object[] args, Map<String, Object> alias) {
        hql = initSort(hql);
        Query query = getSession().createQuery(hql);
        setAliasParameter(query, alias);
        setParameter(query, args);
        return query.list();
    }

    public List<T> listByAlias(String hql, Map<String, Object> alias) {
        return this.list(hql, null, alias);
    }

    public Pager<T> find(String hql, Object[] args) {
        return this.find(hql, args, null);
    }

    public Pager<T> find(String hql, Object arg) {
        return this.find(hql, new Object[]{arg});
    }

    public Pager<T> find(String hql) {
        return this.find(hql, null);
    }

    private void setPagers(Query query, Pager pages) {
        Integer pageSize = SystemContext.getPageSize();
        Integer pageOffset = SystemContext.getPageOffset();
        if (pageOffset == null || pageOffset < 0) pageOffset = 0;
        if (pageSize == null || pageSize < 0) pageSize = 15;
        pages.setOffset(pageOffset);
        pages.setSize(pageSize);
        query.setFirstResult(pageOffset).setMaxResults(pageSize);
    }

    private String getCountHql(String hql, boolean isHql) {
        String e = hql.substring(hql.indexOf("from"));
        String c = "select count(*) " + e;
        if (isHql)
            c.replace("fetch", "");
        return c;
    }

    public Pager<T> find(String hql, Object[] args, Map<String, Object> alias) {
        hql = initSort(hql);
        String cq = getCountHql(hql, true);
        cq = initSort(cq);
        Query query = getSession().createQuery(hql);
        Query cquery = getSession().createQuery(cq);
        //设置别名参数
        setAliasParameter(query, alias);
        setAliasParameter(cquery, alias);
        //设置参数
        setParameter(query, args);
        setParameter(cquery, args);
        Pager<T> pages = new Pager<T>();
        setPagers(query, pages);
        List<T> datas = query.list();
        pages.setDatas(datas);
        Long total = (Long) cquery.uniqueResult();
        pages.setTotal(total);
        return pages;
    }

    public Pager<T> findByAlias(String hql, Map<String, Object> alias) {
        return this.find(hql, null, alias);
    }

    public Object queryObject(String hql, Object[] args) {

        return this.queryObject(hql, args, null);
    }

    public Object queryObject(String hql, Object[] args, Map<String, Object> alias) {
        Query query = getSession().createQuery(hql);
        setAliasParameter(query, alias);
        setParameter(query, args);
        return query.uniqueResult();
    }

    public Object queryObjectByAlias(String hql, Map<String, Object> alias) {
        return this.queryObject(hql, null, alias);
    }

    public Object queryObject(String hql, Object args) {
        return this.queryObject(hql, new Object[]{args});
    }

    public Object queryObject(String hql) {
        return this.queryObject(hql, null);
    }

    public void updateByHql(String hql, Object[] args) {
        Query query = getSession().createQuery(hql);
        query.setParameter(hql, args);
        query.executeUpdate();
    }

    public void updateByHql(String hql, Object args) {
        this.updateByHql(hql, new Object[]{args});
    }

    public void updateByHql(String hql) {
        this.updateByHql(hql, null);

    }

    public List<Object> queryBySql(String sql, Object[] args, Class<Object> clz, boolean isEntity) {
        return this.queryBySql(sql, args, null, clz, isEntity);
    }

    public List<Object> queryBySql(String sql, Object args, Class<Object> clz, boolean isEntity) {
        return this.queryBySql(sql, new Object[]{args}, null, clz, isEntity);
    }

    public List<Object> queryBySql(String sql, Class<Object> clz, boolean isEntity) {
        return this.queryBySql(sql, null, clz, isEntity);
    }

    public List<Object> queryBySql(String sql, Object[] args, Map<String, Object> alias, Class<Object> clz, boolean isEntity) {
        sql = initSort(sql);
        SQLQuery query = getSession().createSQLQuery(sql);
        setAliasParameter((Query) query, alias);
        setParameter((Query) query, args);
        if (isEntity) {
            query.addEntity(clz);
        } else {
            query.setResultTransformer(Transformers.aliasToBean(clz));
        }

        return query.list();
    }

    public List<Object> queryAliasBySql(String sql, Map<String, Object> alias, Class<Object> clz, boolean isEntity) {
        return this.queryBySql(sql, null, alias, clz, isEntity);
    }

    public Pager<Object> findBySql(String sql, Object[] args, Class<Object> clz, boolean isEntity) {

        return this.findBySql(sql, args, null, clz, isEntity);
    }

    public Pager<Object> findBySql(String sql, Object args, Class<Object> clz, boolean isEntity) {
        return this.findBySql(sql, new Object[]{args}, clz, isEntity);
    }

    public Pager<Object> findBySql(String sql, Class<Object> clz, boolean isEntity) {

        return this.findBySql(sql, null, clz, isEntity);
    }

    public Pager<Object> findBySql(String sql, Object[] args, Map<String, Object> alias, Class<Object> clz, boolean isEntity) {
        String cq = getCountHql(sql, false);
        cq = initSort(cq);
        sql = initSort(sql);
        SQLQuery cquery = (SQLQuery) getSession().createQuery(cq);
        SQLQuery query = getSession().createSQLQuery(sql);
        setAliasParameter((Query) cquery, alias);
        setAliasParameter((Query) query, alias);
        setParameter((Query) cquery, args);
        setParameter((Query) query, args);
        Pager<Object> pager = new Pager<Object>();
        setPagers((Query) query, pager);
        if (isEntity) {
            query.addEntity(clz);
        } else {

            query.setResultTransformer(Transformers.aliasToBean(clz));
        }
        List<Object> datas = query.list();
        pager.setDatas(datas);
        Long total = (Long) cquery.uniqueResult();
        pager.setTotal(total);
        return pager;
    }

    public Pager<Object> findAliasBySql(String sql, Map<String, Object> alias, Class<Object> clz, boolean isEntity) {
        return this.findBySql(sql, null, alias, clz, isEntity);
    }
}
