/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.hutool.core.bean.BeanUtil
 *  cn.hutool.core.date.DateField
 *  cn.hutool.core.date.DateTime
 *  cn.hutool.core.date.DateUtil
 *  cn.hutool.core.util.StrUtil
 *  io.swagger.annotations.ApiModelProperty
 */
package com.kakarote.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.kakarote.common.result.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BiTimeUtil {
    public static BiTimeEntity analyzeTime(BiParams biParams) {
        DateTime beginDate = DateUtil.date();
        DateTime endDate = DateUtil.date();
        int cycleNum = 12;
        String sqlDateFormat = "%Y%m";
        String dateFormat = "yyyyMM";
        String type = biParams.getType();
        String startTime = biParams.getStartTime();
        String endTime = biParams.getEndTime();
        if (StrUtil.isNotEmpty((CharSequence)type)) {
            String regex = "^[\\d]{8}$";
            if (type.matches(regex)) {
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
            }
            switch (type) {
                case "year": {
                    beginDate = DateUtil.beginOfYear((Date)DateUtil.date());
                    endDate = DateUtil.endOfYear((Date)DateUtil.date());
                    break;
                }
                case "lastYear": {
                    beginDate = DateUtil.beginOfYear((Date)DateUtil.offsetMonth((Date)DateUtil.date(), (int)-12));
                    endDate = DateUtil.endOfYear((Date)DateUtil.offsetMonth((Date)DateUtil.date(), (int)-12));
                    break;
                }
                case "quarter": {
                    beginDate = DateUtil.beginOfQuarter((Date)DateUtil.date());
                    endDate = DateUtil.endOfQuarter((Date)DateUtil.date());
                    break;
                }
                case "lastQuarter": {
                    beginDate = DateUtil.beginOfQuarter((Date)DateUtil.offsetMonth((Date)DateUtil.date(), (int)-3));
                    endDate = DateUtil.endOfQuarter((Date)DateUtil.offsetMonth((Date)DateUtil.date(), (int)-3));
                    break;
                }
                case "month": {
                    beginDate = DateUtil.beginOfMonth((Date)DateUtil.date());
                    endDate = DateUtil.endOfMonth((Date)DateUtil.date());
                    sqlDateFormat = "%Y%m%d";
                    dateFormat = "yyyyMMdd";
                    break;
                }
                case "lastMonth": {
                    beginDate = DateUtil.beginOfMonth((Date)DateUtil.offsetMonth((Date)DateUtil.date(), (int)-1));
                    endDate = DateUtil.endOfMonth((Date)DateUtil.offsetMonth((Date)DateUtil.date(), (int)-1));
                    sqlDateFormat = "%Y%m%d";
                    dateFormat = "yyyyMMdd";
                    break;
                }
                case "week": {
                    beginDate = DateUtil.beginOfWeek((Date)DateUtil.date());
                    endDate = DateUtil.endOfWeek((Date)DateUtil.date());
                    sqlDateFormat = "%Y%m%d";
                    dateFormat = "yyyyMMdd";
                    break;
                }
                case "lastWeek": {
                    beginDate = DateUtil.beginOfWeek((Date)DateUtil.offsetWeek((Date)DateUtil.date(), (int)-1));
                    endDate = DateUtil.endOfWeek((Date)DateUtil.offsetWeek((Date)DateUtil.date(), (int)-1));
                    sqlDateFormat = "%Y%m%d";
                    dateFormat = "yyyyMMdd";
                    break;
                }
                case "today": {
                    beginDate = DateUtil.beginOfDay((Date)DateUtil.date());
                    endDate = DateUtil.endOfDay((Date)DateUtil.date());
                    sqlDateFormat = "%Y%m%d";
                    break;
                }
                case "yesterday": {
                    beginDate = DateUtil.beginOfDay((Date)new Date(System.currentTimeMillis() - 86400000L));
                    endDate = DateUtil.endOfDay((Date)new Date(System.currentTimeMillis() - 86400000L));
                    sqlDateFormat = "%Y%m%d";
                    dateFormat = "yyyyMMdd";
                    break;
                }
                case "nextYear": {
                    beginDate = DateUtil.beginOfYear((Date)DateUtil.date());
                    beginDate = DateUtil.offset((Date)beginDate, (DateField)DateField.YEAR, (int)1);
                    endDate = DateUtil.endOfYear((Date)DateUtil.date());
                    endDate = DateUtil.offset((Date)endDate, (DateField)DateField.YEAR, (int)1);
                    break;
                }
                case "firstHalfYear": {
                    beginDate = DateUtil.beginOfYear((Date)DateUtil.date());
                    endDate = DateUtil.offsetMonth((Date)DateUtil.endOfYear((Date)DateUtil.date()), (int)-6);
                    break;
                }
                case "nextHalfYear": {
                    beginDate = DateUtil.offsetMonth((Date)DateUtil.beginOfYear((Date)DateUtil.date()), (int)6);
                    endDate = DateUtil.endOfYear((Date)DateUtil.date());
                    break;
                }
                case "nextQuarter": {
                    beginDate = DateUtil.beginOfQuarter((Date)DateUtil.date().offset(DateField.MONTH, 3));
                    endDate = DateUtil.endOfQuarter((Date)DateUtil.date().offset(DateField.MONTH, 3));
                    break;
                }
                case "nextMonth": {
                    beginDate = DateUtil.beginOfMonth((Date)DateUtil.date().offset(DateField.MONTH, 1));
                    endDate = DateUtil.beginOfMonth((Date)DateUtil.date().offset(DateField.MONTH, 1));
                    sqlDateFormat = "%Y%m%d";
                    dateFormat = "yyyyMMdd";
                    break;
                }
                case "nextWeek": {
                    beginDate = DateUtil.beginOfWeek((Date)DateUtil.date().offset(DateField.DAY_OF_YEAR, 7));
                    endDate = DateUtil.endOfWeek((Date)DateUtil.date().offset(DateField.DAY_OF_YEAR, 7));
                    sqlDateFormat = "%Y%m%d";
                    dateFormat = "yyyyMMdd";
                    break;
                }
                case "tomorrow": {
                    beginDate = DateUtil.beginOfDay((Date)DateUtil.date().offset(DateField.DAY_OF_YEAR, 1));
                    endDate = DateUtil.endOfDay((Date)DateUtil.date().offset(DateField.DAY_OF_YEAR, 1));
                    sqlDateFormat = "%Y%m%d";
                    dateFormat = "yyyyMMdd";
                    break;
                }
                case "previous7day": {
                    beginDate = DateUtil.beginOfDay((Date)DateUtil.date().offset(DateField.DAY_OF_YEAR, -7));
                    endDate = DateUtil.endOfDay((Date)DateUtil.date().offset(DateField.DAY_OF_YEAR, -1));
                    break;
                }
                case "previous30day": {
                    beginDate = DateUtil.beginOfDay((Date)DateUtil.date().offset(DateField.DAY_OF_YEAR, -30));
                    endDate = DateUtil.endOfDay((Date)DateUtil.date().offset(DateField.DAY_OF_YEAR, -1));
                    break;
                }
                case "future7day": {
                    beginDate = DateUtil.beginOfDay((Date)DateUtil.date().offset(DateField.DAY_OF_YEAR, 1));
                    endDate = DateUtil.endOfDay((Date)DateUtil.date().offset(DateField.DAY_OF_YEAR, 7));
                    break;
                }
                case "future30day": {
                    beginDate = DateUtil.beginOfDay((Date)DateUtil.date().offset(DateField.DAY_OF_YEAR, 1));
                    endDate = DateUtil.endOfDay((Date)DateUtil.date().offset(DateField.DAY_OF_YEAR, 30));
                    break;
                }
            }
        } else if (StrUtil.isNotEmpty((CharSequence)startTime) && StrUtil.isNotEmpty((CharSequence)endTime)) {
            DateTime end;
            DateTime start;
            int timeLength = 6;
            if (startTime.length() == timeLength) {
                start = DateUtil.parse((CharSequence)startTime, (String)"yyyyMM");
                end = DateUtil.endOfMonth((Date)DateUtil.parse((CharSequence)endTime, (String)"yyyyMM"));
            } else {
                start = DateUtil.parse((CharSequence)startTime);
                end = DateUtil.parse((CharSequence)endTime);
            }
            Integer startMonth = Integer.valueOf(DateUtil.format((Date)start, (String)"yyyyMM"));
            int endMonth = Integer.parseInt(DateUtil.format((Date)end, (String)"yyyyMM"));
            if (startMonth.equals(endMonth)) {
                sqlDateFormat = "%Y%m%d";
                dateFormat = "yyyyMMdd";
            } else {
                sqlDateFormat = "%Y%m";
                dateFormat = "yyyyMM";
            }
            beginDate = start;
            endDate = end;
        }
        return new BiTimeEntity(sqlDateFormat, dateFormat, (Date)beginDate, (Date)endDate, null);
    }

    public static class BiParams
    extends PageEntity {
        @ApiModelProperty(value="\u7c7b\u578b")
        private String type;
        @ApiModelProperty(value="\u5f00\u59cb\u65f6\u95f4")
        private String startTime;
        @ApiModelProperty(value="\u7ed3\u675f\u65f6\u95f4")
        private String endTime;

        public String getType() {
            return this.type;
        }

        public String getStartTime() {
            return this.startTime;
        }

        public String getEndTime() {
            return this.endTime;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }

    public static class BiTimeEntity {
        private String sqlDateFormat;
        private String dateFormat;
        private Date beginDate;
        private Date endDate;
        private List<Long> userIds = new ArrayList<Long>();
        private Integer page;
        private Integer limit;

        public BiTimeEntity(String sqlDateFormat, String dateFormat, Date beginDate, Date endDate, List<Long> userIds) {
            this.sqlDateFormat = sqlDateFormat;
            this.dateFormat = dateFormat;
            this.beginDate = beginDate;
            this.endDate = endDate;
            this.userIds = userIds;
        }

        public BiTimeEntity() {
        }

        public Map<String, Object> toMap() {
            return BeanUtil.beanToMap((Object)this, (String[])new String[0]);
        }

        public String getSqlDateFormat() {
            return this.sqlDateFormat;
        }

        public String getDateFormat() {
            return this.dateFormat;
        }

        public Date getBeginDate() {
            return this.beginDate;
        }

        public Date getEndDate() {
            return this.endDate;
        }

        public List<Long> getUserIds() {
            return this.userIds;
        }

        public Integer getPage() {
            return this.page;
        }

        public Integer getLimit() {
            return this.limit;
        }

        public BiTimeEntity setSqlDateFormat(String sqlDateFormat) {
            this.sqlDateFormat = sqlDateFormat;
            return this;
        }

        public BiTimeEntity setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
            return this;
        }

        public BiTimeEntity setBeginDate(Date beginDate) {
            this.beginDate = beginDate;
            return this;
        }

        public BiTimeEntity setEndDate(Date endDate) {
            this.endDate = endDate;
            return this;
        }

        public BiTimeEntity setUserIds(List<Long> userIds) {
            this.userIds = userIds;
            return this;
        }

        public BiTimeEntity setPage(Integer page) {
            this.page = page;
            return this;
        }

        public BiTimeEntity setLimit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof BiTimeEntity)) {
                return false;
            }
            BiTimeEntity other = (BiTimeEntity)o;
            if (!other.canEqual(this)) {
                return false;
            }
            Integer this$page = this.getPage();
            Integer other$page = other.getPage();
            if (this$page == null ? other$page != null : !((Object)this$page).equals(other$page)) {
                return false;
            }
            Integer this$limit = this.getLimit();
            Integer other$limit = other.getLimit();
            if (this$limit == null ? other$limit != null : !((Object)this$limit).equals(other$limit)) {
                return false;
            }
            String this$sqlDateFormat = this.getSqlDateFormat();
            String other$sqlDateFormat = other.getSqlDateFormat();
            if (this$sqlDateFormat == null ? other$sqlDateFormat != null : !this$sqlDateFormat.equals(other$sqlDateFormat)) {
                return false;
            }
            String this$dateFormat = this.getDateFormat();
            String other$dateFormat = other.getDateFormat();
            if (this$dateFormat == null ? other$dateFormat != null : !this$dateFormat.equals(other$dateFormat)) {
                return false;
            }
            Date this$beginDate = this.getBeginDate();
            Date other$beginDate = other.getBeginDate();
            if (this$beginDate == null ? other$beginDate != null : !((Object)this$beginDate).equals(other$beginDate)) {
                return false;
            }
            Date this$endDate = this.getEndDate();
            Date other$endDate = other.getEndDate();
            if (this$endDate == null ? other$endDate != null : !((Object)this$endDate).equals(other$endDate)) {
                return false;
            }
            List<Long> this$userIds = this.getUserIds();
            List<Long> other$userIds = other.getUserIds();
            return !(this$userIds == null ? other$userIds != null : !((Object)this$userIds).equals(other$userIds));
        }

        protected boolean canEqual(Object other) {
            return other instanceof BiTimeEntity;
        }

        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            Integer $page = this.getPage();
            result = result * 59 + ($page == null ? 43 : ((Object)$page).hashCode());
            Integer $limit = this.getLimit();
            result = result * 59 + ($limit == null ? 43 : ((Object)$limit).hashCode());
            String $sqlDateFormat = this.getSqlDateFormat();
            result = result * 59 + ($sqlDateFormat == null ? 43 : $sqlDateFormat.hashCode());
            String $dateFormat = this.getDateFormat();
            result = result * 59 + ($dateFormat == null ? 43 : $dateFormat.hashCode());
            Date $beginDate = this.getBeginDate();
            result = result * 59 + ($beginDate == null ? 43 : ((Object)$beginDate).hashCode());
            Date $endDate = this.getEndDate();
            result = result * 59 + ($endDate == null ? 43 : ((Object)$endDate).hashCode());
            List<Long> $userIds = this.getUserIds();
            result = result * 59 + ($userIds == null ? 43 : ((Object)$userIds).hashCode());
            return result;
        }

        public String toString() {
            return "BiTimeUtil.BiTimeEntity(sqlDateFormat=" + this.getSqlDateFormat() + ", dateFormat=" + this.getDateFormat() + ", beginDate=" + this.getBeginDate() + ", endDate=" + this.getEndDate() + ", userIds=" + this.getUserIds() + ", page=" + this.getPage() + ", limit=" + this.getLimit() + ")";
        }
    }
}

