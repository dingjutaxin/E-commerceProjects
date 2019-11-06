package com.itmayiedu.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.itmayiedu.entity.OrderEntity;

@Mapper
public interface OrderDao {

	@Update("update order_info set isPay=#{isPay} ,payId=#{payId},updated=#{updated} where orderNumber=#{orderNumber};")
	public int updateOrder(OrderEntity orderEntity);

}
