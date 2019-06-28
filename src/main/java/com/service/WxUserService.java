package com.service;

import com.entity.WxUser;

import java.util.List;

public interface WxUserService {


	WxUser update(WxUser wxUser);

	List<WxUser> find(WxUser wxUser);

	Object charge(String string, int chargeId);

	void chargeSuccess(String orderId, String openId, String attach);

	Object findcharge(String openId);

    WxUser findById(String openId);

	int addSource(String openId, Integer source);

	int countBySchoolId(int schoolId);

	WxUser login(String openid, Integer schoolId, Integer appId, String client);

	List<WxUser> findByPhoneGZH(String phone);

    WxUser findGzh(String phone);

    WxUser findByschoolAndPhone(WxUser query);
}
