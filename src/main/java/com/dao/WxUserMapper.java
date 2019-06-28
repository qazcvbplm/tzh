package com.dao;

import com.entity.WxUser;

import java.util.List;

public interface WxUserMapper {
    int insert(WxUser record);

    int insertSelective(WxUser record);

    WxUser selectByPrimaryKey(String openId);

    int updateByPrimaryKeySelective(WxUser record);

    int updateByPrimaryKey(WxUser record);

	List<WxUser> find(WxUser wxUser);

	int findByPhone(String phone);

	WxUser findByschoolAndPhone(WxUser query);

	int countBySchoolId(int schoolId);

	List<WxUser> findByPhoneGZH(String query);

    List<WxUser> findGzh(String phone);

}