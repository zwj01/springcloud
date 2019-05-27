package com.cms.dao;

import com.cms.model.Pager;

import java.util.List;
import java.util.Map;

/**
 * 公共的DAO处理对象，这个对象中包含了hibernate的所有基本操作和对SQL的操作
 * @param <T>
 */
public interface IBaseDao<T> {
    /**
     * 添加对象
     * @param t
     * @return
     */
    public T add(T t);

    /**
     * 更新对象
     * @param t
     */
    public void update(T t);

    /**
     * 根据id删除对象
     * @param id
     */
    public void delete(int id);

    /**
     * 根据id加载对象
     * @param id
     * @return
     */
    public T load(int id);

    /**
     *不分页类列表对象
     * @param hql 查询对象的hql
     * @param args 查询参数
     * @return 一组不分页的列表对象
     */
    public List<T> list(String hql,Object[] args);

    /**
     *
     * @param hql
     * @param arg
     * @return
     */
    public List<T> list(String hql,Object arg);

    /**
     *
     * @param hql
     * @return
     */
    public List<T> list(String hql);

    /**
     * 基于别名和混合参数的混合列表对象
     * @param hql
     * @param args
     * @param alias 别名对象
     * @return
     */
    public List<T> list(String hql,Object[] args,Map<String,Object> alias);

    public List<T> listByAlias(String hql,Map<String,Object> alias);

    /**
     *分页类列表对象
     * @param hql 查询对象的hql
     * @param args 查询参数
     * @return 一组不分页的列表对象
     */
    public Pager<T> find(String hql, Object[] args);

    /**
     *
     * @param hql
     * @param arg
     * @return
     */
    public Pager<T> find(String hql,Object arg);

    /**
     *
     * @param hql
     * @return
     */
    public Pager<T> find(String hql);

    /**
     * 基于别名和混合参数的混合列表对象
     * @param hql
     * @param args
     * @param alias 别名对象
     * @return
     */
    public Pager<T> find(String hql,Object[] args,Map<String,Object> alias);

    public Pager<T> findByAlias(String hql,Map<String,Object> alias);

    /**
     * 根据hql查询一组对象
     * @param hql
     * @param args
     * @return
     */
    public Object queryObject(String hql,Object[] args);
    public Object queryObject(String hql,Object args);
    public Object queryObject(String hql);
    public Object queryObject(String hql,Object[] args,Map<String,Object> alias);
    public Object queryObjectByAlias(String hql,Map<String,Object> alias);

    /**
     * 根据hql更新对象
     * @param hql
     * @param args
     */
    public void updateByHql(String hql,Object[] args);
    public void updateByHql(String hql,Object args);
    public void updateByHql(String hql);

    /**
     * 不分页查询
     * 根据sql查询对象，不包含关联对象
     * @param sql 查询的sql语句
     * @param args 查询条件
     * @param clz 查询的实体对象
     * @param isEntity 该对象是否是hibernate管理的一个实体对象，如果不是需要使用setResultTransform查询
     * @return 一组对象
     */
    public List<Object> queryBySql(String sql,Object[] args,Class<Object> clz,boolean isEntity);
    public List<Object> queryBySql(String sql,Object args,Class<Object> clz,boolean isEntity);
    public List<Object> queryBySql(String sql,Class<Object> clz,boolean isEntity);
    public List<Object> queryBySql(String sql,Object[] args,Map<String,Object> alias,Class<Object> clz,boolean isEntity);
    public List<Object> queryAliasBySql(String sql,Map<String,Object> alias,Class<Object> clz,boolean isEntity);

    /**
     * 分页查询
     * 根据sql查询对象，不包含关联对象
     * @param sql 查询的sql语句
     * @param args 查询条件
     * @param clz 查询的实体对象
     * @param isEntity 该对象是否是hibernate管理的一个实体对象，如果不是需要使用setResultTransform查询
     * @return 一组对象
     */
    public Pager<Object> findBySql(String sql,Object[] args,Class<Object> clz,boolean isEntity);
    public Pager<Object> findBySql(String sql,Object args,Class<Object> clz,boolean isEntity);
    public Pager<Object> findBySql(String sql,Class<Object> clz,boolean isEntity);
    public Pager<Object> findBySql(String sql,Object[] args,Map<String,Object> alias,Class<Object> clz,boolean isEntity);
    public Pager<Object> findAliasBySql(String sql,Map<String,Object> alias,Class<Object> clz,boolean isEntity);


}
