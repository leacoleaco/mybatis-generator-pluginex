/*
 * This file is generated by jOOQ.
 */
package com.fuyo.cloud.db.biz.test.jooq.test.tables.daos;


import com.fuyo.cloud.db.biz.test.jooq.test.tables.TTest1;
import com.fuyo.cloud.db.biz.test.jooq.test.tables.records.TTest1Record;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.SelectFieldOrAsterisk;
import org.jooq.SelectQuery;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Repository
public class TTest1Dao extends DAOImpl<TTest1Record, com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1, Integer> {

    /**
     * Create a new TTest1Dao without any configuration
     */
    public TTest1Dao() {
        super(TTest1.T_TEST1, com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1.class);
    }

    /**
     * Create a new TTest1Dao with an attached configuration
     */
    @Autowired
    public TTest1Dao(Configuration configuration) {
        super(TTest1.T_TEST1, com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1.class, configuration);
    }

    @Override
    public Integer getId(com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1 object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> fetchRangeOfId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(TTest1.T_TEST1.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> fetchById(Integer... values) {
        return fetch(TTest1.T_TEST1.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1 fetchOneById(Integer value) {
        return fetchOne(TTest1.T_TEST1.ID, value);
    }

    /**
     * Fetch records that have <code>name BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> fetchRangeOfName(String lowerInclusive, String upperInclusive) {
        return fetchRange(TTest1.T_TEST1.NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>name IN (values)</code>
     */
    public List<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> fetchByName(String... values) {
        return fetch(TTest1.T_TEST1.NAME, values);
    }

    /**
     * Fetch records that have <code>date_time BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> fetchRangeOfDateTime(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(TTest1.T_TEST1.DATE_TIME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>date_time IN (values)</code>
     */
    public List<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> fetchByDateTime(LocalDateTime... values) {
        return fetch(TTest1.T_TEST1.DATE_TIME, values);
    }

    /**
     * Fetch records that have <code>type BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> fetchRangeOfType(Byte lowerInclusive, Byte upperInclusive) {
        return fetchRange(TTest1.T_TEST1.TYPE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>type IN (values)</code>
     */
    public List<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> fetchByType(Byte... values) {
        return fetch(TTest1.T_TEST1.TYPE, values);
    }
     public SelectQuery<TTest1Record> createQuery() {
        return ctx().selectQuery(getTable());
    }
    public SelectQuery<TTest1Record> createQuery(Map<String, Object> params) {
        SelectQuery<TTest1Record> query = createQuery();
        query.addConditions(com.fuyo.cloud.db.biz.test.jooq.test.tables.TTest1.T_TEST1.buildCondition(params));
        query.addOrderBy(com.fuyo.cloud.db.biz.test.jooq.test.tables.TTest1.T_TEST1.buildOrderBy(params));
        return query;
    }
    public SelectQuery<TTest1Record> createQuery(Map<String, Object> params, SelectFieldOrAsterisk... fields) {
        SelectQuery<TTest1Record> query = createQuery(params);
        query.addSelect(fields);
        return query;
    }

    /**
     * 查询分页
     */
    public com.github.pagehelper.Page<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> fetchPage(Map<String, Object> params) {
        SelectQuery<TTest1Record> query = createQuery(params);
        return fetchPage(query, params);
    }

    /**
     * 查询分页
     */
    public com.github.pagehelper.Page<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> fetchPage(SelectQuery<?> query, Map<String, Object> params) {
        int page = (int) Optional.ofNullable(params.get("page")).map(Object::toString).map(Integer::parseInt).orElse(1);
        int limit = (int) Optional.ofNullable(java.util.Optional.ofNullable(params.get("limit")).orElse(params.get("pageSize"))).map(Object::toString).map(Integer::parseInt).orElse(10);
        return fetchPage(query, page, limit);
    }

    /**
     * 查询分页
     */
    public com.github.pagehelper.Page<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> fetchPage(SelectQuery<?> query, int pageNum, int pageSize) {
        com.github.pagehelper.Page<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> page = com.github.pagehelper.PageHelper.startPage(pageNum, pageSize, true, true, false);
        int total = ctx().fetchCount(query);
        page.setTotal(total);
        query.addLimit(page.getStartRow(), page.getPageSize());
        List<com.fuyo.cloud.db.biz.test.jooq.test.tables.pojos.TTest1> list = ctx().fetch(query).into(this.getType());
        page.clear();
        page.addAll(list);
        return page;
    }

    /**
     * 查询分页
     */
    public <M> com.github.pagehelper.Page<M> fetchPage(SelectQuery<?> query, Map<String, Object> params, Class<M> clazz) {
        int page = (int) Optional.ofNullable(params.get("page")).map(Object::toString).map(Integer::parseInt).orElse(1);
        int limit = (int) Optional.ofNullable(java.util.Optional.ofNullable(params.get("limit")).orElse(params.get("pageSize"))).map(Object::toString).map(Integer::parseInt).orElse(10);
        return fetchPage(query, page, limit, clazz);
    }

    /**
     * 查询分页
     */
    public <M> com.github.pagehelper.Page<M> fetchPage(SelectQuery<?> query, int pageNum, int pageSize, Class<M> clazz) {
        com.github.pagehelper.Page<M> page = com.github.pagehelper.PageHelper.startPage(pageNum, pageSize, true, true, false);
        int total = ctx().fetchCount(query);
        page.setTotal(total);
        query.addLimit(page.getStartRow(), page.getPageSize());
        List<M> list = ctx().fetch(query).into(clazz);
        page.clear();
        page.addAll(list);
        return page;
    }
}
