/*
 * Copyright (c) 2011-2020, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.core.metadata;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 分页 Page 对象接口
 * </p>
 *
 * @author hubin
 * @since 2018-06-09
 */
public interface IPage<T> extends Serializable {

    /**
     * <p>
     * 降序字段数组
     * </p>
     *
     * @return order by desc 的字段数组
     */
    default String[] descs() {
        return null;
    }

    /**
     * <p>
     * 升序字段数组
     * </p>
     *
     * @return order by asc 的字段数组
     */
    default String[] ascs() {
        return null;
    }

    /**
     * <p>
     * KEY/VALUE 条件
     * </p>
     *
     * @return
     */
    default Map<Object, Object> condition() {
        return null;
    }

    /**
     * <p>
     * 自动优化 COUNT SQL【 默认：true 】
     * </p>
     *
     * @return true 是 / false 否
     */
    default boolean optimizeCountSql() {
        return true;
    }

    /**
     * <p>
     * 进行 count 查询 【 默认: true 】
     * </p>
     *
     * @return true 是 / false 否
     */
    default boolean isSearchCount() {
        return true;
    }

    /**
     * <p>
     * 计算当前分页偏移量
     * </p>
     */
    default long offset() {
        return getCurrent() > 0 ? (getCurrent() - 1) * getSize() : 0;
    }

    /**
     * <p>
     * 当前分页总页数
     * </p>
     */
    default long getPages() {
        if (getSize() == 0) {
            return 0L;
        }
        long pages = getTotal() / getSize();
        if (getTotal() % getSize() != 0) {
            pages++;
        }
        return pages;
    }

    /**
     * <p>
     * 内部什么也不干
     * 只是为了 json 反序列化时不报错
     * </p>
     */
    default IPage<T> setPages(long pages) {
        // to do nothing
        return this;
    }

    /**
     * <p>
     * 分页记录列表
     * </p>
     *
     * @return 分页对象记录列表
     */
    List<T> getRecords();

    /**
     * <p>
     * 设置分页记录列表
     * </p>
     */
    IPage<T> setRecords(List<T> records);

    /**
     * <p>
     * 当前满足条件总行数
     * </p>
     *
     * @return 总条数
     */
    long getTotal();

    /**
     * <p>
     * 设置当前满足条件总行数
     * </p>
     */
    IPage<T> setTotal(long total);

    /**
     * <p>
     * 当前分页总页数
     * </p>
     *
     * @return 总页数
     */
    long getSize();

    /**
     * <p>
     * 设置当前分页总页数
     * </p>
     */
    IPage<T> setSize(long size);

    /**
     * <p>
     * 当前页，默认 1
     * </p>
     *
     * @return 当然页
     */
    long getCurrent();

    /**
     * <p>
     * 设置当前页
     * </p>
     */
    IPage<T> setCurrent(long current);

    /**
     * <p>
     * IPage 的泛型转换
     * </p>
     *
     * @param mapper 转换函数
     * @param <R>    转换后的泛型
     * @return 转换泛型后的 IPage
     */
    @SuppressWarnings("unchecked")
    default <R> IPage<R> convert(Function<? super T, ? extends R> mapper) {
        List<R> collect = this.getRecords().stream().map(mapper).collect(toList());
        return ((IPage<R>) this).setRecords(collect);
    }

    default org.springframework.data.domain.Page<T> toPage(){
        return new PageImpl<>(getRecords(), PageRequest.of((int)getCurrent(), (int)getSize()), getTotal());
    }


}
