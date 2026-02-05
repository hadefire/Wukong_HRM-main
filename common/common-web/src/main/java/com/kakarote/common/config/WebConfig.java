/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cn.hutool.core.date.DatePattern
 *  cn.hutool.core.date.DateUtil
 *  cn.hutool.core.util.StrUtil
 *  com.fasterxml.jackson.core.JsonGenerator
 *  com.fasterxml.jackson.core.JsonParser
 *  com.fasterxml.jackson.databind.DeserializationContext
 *  com.fasterxml.jackson.databind.DeserializationFeature
 *  com.fasterxml.jackson.databind.JsonDeserializer
 *  com.fasterxml.jackson.databind.JsonSerializer
 *  com.fasterxml.jackson.databind.Module
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.fasterxml.jackson.databind.SerializerProvider
 *  com.fasterxml.jackson.databind.json.JsonMapper
 *  com.fasterxml.jackson.databind.ser.std.ToStringSerializer
 *  com.fasterxml.jackson.datatype.jdk8.Jdk8Module
 *  com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
 *  com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
 *  com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
 *  com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
 *  com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 */
package com.kakarote.common.config;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Value(value="${spring.jackson.timeZone:GMT+8}")
    private String timeZone;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(Long.TYPE, (JsonSerializer)ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Long.class, (JsonSerializer)ToStringSerializer.instance);
            jacksonObjectMapperBuilder.deserializerByType(Date.class, (JsonDeserializer)new DateDeSerializer());
            jacksonObjectMapperBuilder.serializers(new JsonSerializer[]{new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMATTER), new LocalDateSerializer(DatePattern.NORM_DATE_FORMATTER)});
            jacksonObjectMapperBuilder.deserializers(new JsonDeserializer[]{new LocalDateTimeDeserializer(DatePattern.NORM_DATETIME_FORMATTER), new LocalDateDeserializer(DatePattern.NORM_DATE_FORMATTER)});
        };
    }

    @Bean
    public ObjectMapper buildObjectMapper() {
        JsonMapper objectMapper = new JsonMapper();
        objectMapper.registerModule((Module)new Jdk8Module());
        objectMapper.setDateFormat((DateFormat)new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(TimeZone.getTimeZone(this.timeZone));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, (JsonSerializer)new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMATTER));
        javaTimeModule.addSerializer(LocalDate.class, (JsonSerializer)new LocalDateSerializer(DatePattern.NORM_DATE_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, (JsonDeserializer)new LocalDateTimeDeserializer(DatePattern.NORM_DATETIME_FORMATTER));
        javaTimeModule.addDeserializer(LocalDate.class, (JsonDeserializer)new LocalDateDeserializer(DatePattern.NORM_DATE_FORMATTER));
        javaTimeModule.addDeserializer(Date.class, (JsonDeserializer)new DateDeSerializer());
        javaTimeModule.addSerializer(Long.class, (JsonSerializer)ToStringSerializer.instance);
        javaTimeModule.addSerializer(Long.TYPE, (JsonSerializer)ToStringSerializer.instance);
        objectMapper.registerModule((Module)javaTimeModule);
        return objectMapper;
    }

    public static class DateDeSerializer
    extends JsonDeserializer<Date> {
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String text = p.getText();
            if (StrUtil.isNotEmpty((CharSequence)text)) {
                return DateUtil.parse((CharSequence)text);
            }
            return null;
        }
    }

    public static class NumberSerializer
    extends JsonSerializer<Long> {
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(value.longValue());
        }
    }
}

