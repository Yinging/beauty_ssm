package com.yingjun.ssm.dao;

import com.yingjun.ssm.entity.Order;

public interface OrderMapper {
    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long orderId);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}