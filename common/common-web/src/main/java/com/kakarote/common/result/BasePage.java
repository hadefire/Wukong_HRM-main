/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.hutool.core.bean.BeanUtil
 *  com.baomidou.mybatisplus.core.metadata.IPage
 *  com.baomidou.mybatisplus.core.metadata.OrderItem
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  com.fasterxml.jackson.core.JsonGenerator
 *  com.fasterxml.jackson.databind.JsonSerializer
 *  com.fasterxml.jackson.databind.SerializerProvider
 *  com.fasterxml.jackson.databind.annotation.JsonSerialize
 */
package com.kakarote.common.result;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BasePage<T>
implements IPage<T>,
Serializable {
    private static final long serialVersionUID = 8545996863226528798L;
    private List<T> list = new ArrayList<T>();
    private long totalRow = 0L;
    private long pageSize = 15L;
    private long pageNumber = 1L;
    private final List<OrderItem> orders = new ArrayList<OrderItem>();
    private boolean optimizeCountSql = true;
    private Object extraData;

    public BasePage() {
    }

    public BasePage(long current, long size) {
        this(current, size, 0L);
    }

    public BasePage(long current, long size, long total) {
        if (current > 1L) {
            this.pageNumber = current;
        }
        this.pageSize = size;
        this.totalRow = total;
    }

    @JsonIgnore
    public List<T> getRecords() {
        return this.list;
    }

    public List<T> getList() {
        return this.list;
    }

    @JsonSerialize(using=NumberSerializer.class)
    public long getTotalRow() {
        return this.totalRow;
    }

    @JsonSerialize(using=NumberSerializer.class)
    public long getTotalPage() {
        if (this.getSize() == 0L) {
            return 0L;
        }
        long pages = this.getTotal() / this.getSize();
        if (this.getTotal() % this.getSize() != 0L) {
            ++pages;
        }
        return pages;
    }

    @JsonSerialize(using=NumberSerializer.class)
    public long getPageSize() {
        return this.pageSize;
    }

    @JsonSerialize(using=NumberSerializer.class)
    public long getPageNumber() {
        return this.pageNumber;
    }

    public boolean isFirstPage() {
        return this.pageNumber == 1L;
    }

    public boolean isLastPage() {
        return this.getTotal() == 0L || this.pageNumber == this.getTotalPage();
    }

    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public BasePage<T> setRecords(List<T> records) {
        this.list = records;
        return this;
    }

    @JsonIgnore
    public long getTotal() {
        return this.totalRow;
    }

    public BasePage<T> setTotal(long total) {
        this.totalRow = total;
        return this;
    }

    @JsonIgnore
    public long getSize() {
        return this.pageSize;
    }

    public BasePage<T> setSize(long size) {
        this.pageSize = size;
        return this;
    }

    @JsonIgnore
    public long getCurrent() {
        return this.pageNumber;
    }

    public BasePage<T> setCurrent(long current) {
        this.pageNumber = current;
        return this;
    }

    public BasePage<T> addOrder(OrderItem ... items) {
        this.orders.addAll(Arrays.asList(items));
        return this;
    }

    public List<OrderItem> orders() {
        return this.orders;
    }

    public boolean optimizeCountSql() {
        return this.optimizeCountSql;
    }

    public BasePage<T> setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
        return this;
    }

    public <R> BasePage<R> copy(Class<R> clazz) {
        return this.copy(clazz, obj -> BeanUtil.copyProperties(obj, clazz));
    }

    public <R> BasePage<R> copy(Class<R> clazz, Function<? super T, ? extends R> mapper) {
        BasePage<R> basePage = new BasePage<R>(this.getCurrent(), this.getSize(), this.getTotal());
        basePage.setRecords(this.getRecords().stream().map(mapper).collect(Collectors.toList()));
        return basePage;
    }

    @JsonIgnore
    public long getPages() {
        return this.getTotalPage();
    }

    public Object getExtraData() {
        return this.extraData;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }

    public static class NumberSerializer
    extends JsonSerializer<Long> {
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value.longValue());
        }
    }
}

