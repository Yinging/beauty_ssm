package com.yingjun.ssm.dao;

import com.yingjun.ssm.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

import static org.apache.ibatis.javassist.util.proxy.ProxyFactory.useCache;

public interface GoodsMapper {
    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    List<Goods> queryAllGoods(@Param("offset") int offset,@Param("limit") int limit);

    int reduceNumber(long goodId);

    void buyWithProcedure(Map<String,Object> paramMap);
}