package com.dao;

import java.util.List;
import java.util.Map;

import com.dto.RunOrdersTj;
import org.apache.ibatis.annotations.Mapper;

import com.entity.RunOrders;
import com.entity.Sender;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RunOrdersMapper {
    int insert(RunOrders record);


    RunOrders selectByPrimaryKey(String id);


	List<RunOrders> find(RunOrders orders);


	int count(RunOrders orders);


	int paySuccess(Map<String, Object> map);


	List<RunOrders> findBySenderRun(Sender sender);


	int SenderAccept(RunOrders orders);


	int end(String orderId);


	void remove();


	List<RunOrders> senderStatistics(Map<String, Object> map);


	List<RunOrders> temp(Map<String, Object> map);


	int pl(String orderid);


	int cancel(String id);


	int countBySchoolId(int schoolId);

	@Select("select count(id) as counts,sum(ro.total_price * s.rate) as senderGet,sum(ro.total_price) as total,ro.school_id as schoolId,ro.payment as payType " +
			"from run_orders ro,sender s where ro.school_id=#{schoolId} and ro.sender_id=s.id and ro.status='已完成' and to_days(ro.create_time)=to_days(now()) group by payType")
	List<RunOrdersTj> tj(@Param("schoolId") Integer schoolId);
}