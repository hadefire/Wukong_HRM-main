/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModelProperty
 */
package com.kakarote.common.result;

import com.kakarote.common.result.BasePage;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

public class PageEntity {
    @ApiModelProperty(value="\u5f53\u524d\u9875\u6570")
    private Integer page = 1;
    @ApiModelProperty(value="\u6bcf\u9875\u5c55\u793a\u6761\u6570")
    private Integer limit = 15;
    @ApiModelProperty(value="\u662f\u5426\u5206\u9875\uff0c0:\u4e0d\u5206\u9875 1 \u5206\u9875", allowableValues="0,1")
    private Integer pageType = 1;

    public <T> BasePage<T> parse() {
        BasePage page = new BasePage(this.getPage().intValue(), this.getLimit().intValue());
        if (Objects.equals(0, this.pageType)) {
            page.setSize(10000L);
        }
        return page;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
        if (pageType == 0) {
            this.limit = 10000;
        }
    }

    public Integer getLimit() {
        if (this.limit > 1000 && 1 == this.pageType) {
            this.limit = 100;
        }
        return this.limit;
    }

    public Integer getPage() {
        return this.page;
    }

    public Integer getPageType() {
        return this.pageType;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}

