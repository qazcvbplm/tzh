package com.serviceimple;

import com.service.SecondHandService;
import com.service.WxUserService;
import ops.school.api.dao.SecondHandMapper;
import ops.school.api.entity.SecondHand;
import ops.school.api.entity.WxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class SecondHandServiceImple implements SecondHandService{

	@Autowired
	private SecondHandMapper secondHandMapper;
	@Autowired
    private WxUserService wxUserService;

	@Override
	public void add(@Valid SecondHand secondHand) {
        WxUser wxUser = wxUserService.findById(secondHand.getOpenId());
		secondHand.setAvatarUrl(wxUser.getAvatarUrl());
		secondHand.setNickName(wxUser.getNickName());
		secondHandMapper.insert(secondHand);
	}

	@Override
	public List<SecondHand> find(SecondHand secondHand) {
		return secondHandMapper.find(secondHand);
	}

	@Override
	public int update(SecondHand secondHand) {
		return secondHandMapper.updateByPrimaryKeySelective(secondHand);
	}

	@Override
	public int count(SecondHand secondHand) {
		return secondHandMapper.count(secondHand);
	}
}
