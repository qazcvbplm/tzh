package com.service;

import ops.school.api.entity.School;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SchoolService {

	void add(@Valid School school) throws Exception;

	List<School> find(School school);

	int update(School school);

	School findById(Integer schoolId);

	School login(String loginName, String enCode);


	String tx(int schoolId, BigDecimal amount, String openId);

	void chargeUse(Map<String, Object> map);

    int sendertx(Map<String, Object> map);

    void charge(Map<String, Object> map2);
}
