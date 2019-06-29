package com.dbzhw.dao;

import com.dbzhw.entity.PaymentInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PaymentInfoDao {

    /**
     * 通过支付id查找支付信息
     */
    @Select("select * from payment_info where  id=#{id}")
    PaymentInfo getPaymentInfo(@Param("id") Long id);

    /**
     * 插入支付信息 : @Options(useGeneratedKeys = true, keyProperty = "id") 将id注入paymentInfo的id字段
     */
    @Insert("insert into payment_info (userid,typeid,orderid,platformorderid,price,source,state,created,updated) values(#{userId},#{typeId},#{orderId},#{platformorderId},#{price},#{source},#{state},#{created},#{updated})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    // 添加该行，product中的id将被自动添加
    Integer savePaymentType(PaymentInfo paymentInfo);

    /**
     * 通过orderId查找支付信息
     */
    @Select("select * from payment_info where  orderId=#{orderId}")
    PaymentInfo getByOrderIdPayInfo(@Param("orderId") String orderId);

    /**
     * 更新支付信息
     */
    @Update("update payment_info set state =#{state},payMessage=#{payMessage},platformorderId=#{platformorderId},updated=#{updated} where orderId=#{orderId} ")
    void updatePayInfo(PaymentInfo paymentInfo);
}
