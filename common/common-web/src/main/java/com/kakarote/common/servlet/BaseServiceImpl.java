/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.hutool.core.bean.BeanUtil
 *  cn.hutool.core.collection.CollUtil
 *  cn.hutool.core.date.DateUtil
 *  cn.hutool.core.util.StrUtil
 *  com.baomidou.mybatisplus.annotation.IdType
 *  com.baomidou.mybatisplus.annotation.TableId
 *  com.baomidou.mybatisplus.core.conditions.Wrapper
 *  com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator
 *  com.baomidou.mybatisplus.core.metadata.TableInfo
 *  com.baomidou.mybatisplus.core.metadata.TableInfoHelper
 *  com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils
 *  com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
 *  com.baomidou.mybatisplus.extension.toolkit.SqlHelper
 *  org.apache.ibatis.session.Configuration
 *  org.apache.ibatis.session.ExecutorType
 *  org.apache.ibatis.session.SqlSession
 *  org.apache.ibatis.session.SqlSessionFactory
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.transaction.annotation.Isolation
 *  org.springframework.transaction.annotation.Transactional
 */
package com.kakarote.common.servlet;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.kakarote.common.constant.Const;
import com.kakarote.common.servlet.BaseMapper;
import com.kakarote.common.servlet.BaseService;
import com.kakarote.common.utils.UserUtil;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public class BaseServiceImpl<M extends BaseMapper<T>, T>
extends ServiceImpl<M, T>
implements BaseService<T> {
    protected static Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Transactional(rollbackFor={Exception.class}, isolation=Isolation.READ_COMMITTED)
    public boolean saveBatch(Collection<T> entityList) {
        return this.saveBatch(entityList, Const.BATCH_SAVE_SIZE);
    }

    @Transactional(rollbackFor={Exception.class}, isolation=Isolation.READ_COMMITTED)
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        if (CollUtil.isEmpty(entityList)) {
            return true;
        }
        T model = entityList.iterator().next();
        Class<?> tClass = model.getClass();
        TableInfo table = SqlHelper.table(tClass);
        List<Field> allFields = TableInfoHelper.getAllFields(tClass);
        SqlSessionFactory sessionFactory = GlobalConfigUtils.getGlobalConfig((Configuration)table.getConfiguration()).getSqlSessionFactory();
        Set<String> fieldSet = allFields.stream().filter(field -> {
            TableId tableId = field.getAnnotation(TableId.class);
            return tableId == null || !Objects.equals(tableId.type(), IdType.AUTO);
        }).map(field -> StrUtil.toUnderlineCase((CharSequence)field.getName())).collect(Collectors.toSet());
        List<Map<String, Object>> mapList = this.insertFill(entityList, table, fieldSet);
        StringBuilder sql = new StringBuilder();
        this.forModelSave(table, fieldSet, sql);
        log.debug("Preparing\uff1a{}", (Object)sql);
        int[] result = this.batch(sessionFactory, sql.toString(), StrUtil.join((CharSequence)",", fieldSet), mapList, batchSize);
        return result.length > 0;
    }

    public T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        List dataList = ((BaseMapper)this.getBaseMapper()).selectList(queryWrapper);
        if (dataList.isEmpty()) {
            return null;
        }
        return (T)dataList.iterator().next();
    }

    private List<Map<String, Object>> insertFill(Collection<T> entityList, TableInfo tableInfo, Set<String> keySet) {
        IdentifierGenerator identifierGenerator = GlobalConfigUtils.getGlobalConfig((Configuration)tableInfo.getConfiguration()).getIdentifierGenerator();
        ArrayList<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>(entityList.size());
        HashSet<String> existFieldSet = new HashSet<String>(keySet.size());
        String dateTime = DateUtil.formatDateTime((java.util.Date)new java.util.Date());
        for (T model : entityList) {
            Map attrs = BeanUtil.beanToMap(model, (boolean)true, (boolean)false);
            Object obj = attrs.get(tableInfo.getKeyColumn());
            if (obj == null) {
                if (tableInfo.getIdType() == IdType.ASSIGN_ID) {
                    if (Number.class.isAssignableFrom(tableInfo.getKeyType())) {
                        attrs.put(tableInfo.getKeyColumn(), identifierGenerator.nextId(model));
                    } else {
                        attrs.put(tableInfo.getKeyColumn(), identifierGenerator.nextId(model).toString());
                    }
                } else if (tableInfo.getIdType() == IdType.ASSIGN_UUID) {
                    attrs.put(tableInfo.getKeyColumn(), identifierGenerator.nextUUID(model));
                }
            }
            for (String key : keySet) {
                if (attrs.get(key) == null) {
                    switch (key) {
                        case "create_time": 
                        case "update_time": {
                            attrs.put(key, dateTime);
                            break;
                        }
                        case "create_user_id": 
                        case "update_user_id": {
                            attrs.put(key, UserUtil.getUserId());
                            break;
                        }
                        case "pool_id": {
                            attrs.put(key, UserUtil.getPoolId());
                            break;
                        }
                    }
                }
                if (attrs.get(key) == null) continue;
                existFieldSet.add(key);
            }
            mapList.add(attrs);
        }
        keySet.retainAll(existFieldSet);
        return mapList;
    }

    private void forModelSave(TableInfo table, Set<String> attrs, StringBuilder sql) {
        sql.append("insert into `").append(table.getTableName()).append("`(");
        CollUtil.newArrayList((Object[])table.getAllSqlSelect().split(","));
        StringBuilder temp = new StringBuilder(") values(");
        int index = 0;
        for (String key : attrs) {
            if (index++ != 0) {
                sql.append(", ");
                temp.append(", ");
            }
            sql.append('`').append(key).append('`');
            temp.append('?');
        }
        sql.append((CharSequence)temp).append(')');
    }

    private int[] batch(SqlSessionFactory sqlSessionFactory, String sql, String columns, Collection<Map<String, Object>> entityList, int batchSize) {
        int[] batch;
        Class tClass = this.currentModelClass();
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);){
            try {
                Connection conn = sqlSession.getConnection();
                batch = this.batch(conn, sql, columns, new ArrayList<Map<String, Object>>(entityList), batchSize);
                sqlSession.commit();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return batch;
    }

    private int[] batch(Connection conn, String sql, String columns, List<Map<String, Object>> list, int batchSize) throws SQLException {
        if (list == null || list.size() == 0) {
            return new int[0];
        }
        if (batchSize < 1) {
            throw new IllegalArgumentException("The batchSize must more than 0.");
        }
        String[] columnArray = columns.split(",");
        for (int i = 0; i < columnArray.length; ++i) {
            columnArray[i] = columnArray[i].trim();
        }
        int counter = 0;
        int pointer = 0;
        int size = list.size();
        int[] result = new int[size];
        PreparedStatement pst = conn.prepareStatement(sql);
        for (Map<String, Object> map : list) {
            int[] r;
            for (int j = 0; j < columnArray.length; ++j) {
                Object value = map.get(columnArray[j]);
                if (value instanceof java.util.Date) {
                    if (value instanceof Date) {
                        pst.setDate(j + 1, (Date)value);
                        continue;
                    }
                    if (value instanceof Timestamp) {
                        pst.setTimestamp(j + 1, (Timestamp)value);
                        continue;
                    }
                    java.util.Date d = (java.util.Date)value;
                    pst.setTimestamp(j + 1, new Timestamp(d.getTime()));
                    continue;
                }
                pst.setObject(j + 1, value);
            }
            pst.addBatch();
            if (++counter < batchSize) continue;
            counter = 0;
            for (int j : r = pst.executeBatch()) {
                result[pointer++] = j;
            }
        }
        if (counter != 0) {
            int[] r;
            for (int i : r = pst.executeBatch()) {
                result[pointer++] = i;
            }
        }
        try {
            pst.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

