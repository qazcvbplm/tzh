package com.service;

import ops.school.api.entity.SecondHand;

import javax.validation.Valid;
import java.util.List;

public interface SecondHandService {

	void add(@Valid SecondHand secondHand);

	List<SecondHand> find(SecondHand secondHand);

	int update(SecondHand secondHand);

	int count(SecondHand secondHand);

}
