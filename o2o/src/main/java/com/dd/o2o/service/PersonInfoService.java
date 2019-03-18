package com.dd.o2o.service;

import com.dd.o2o.entity.PersonInfo;

public interface PersonInfoService {

	/**
	 * 
	 * @param userId
	 * @return
	 */
	PersonInfo getPersonInfoById(Long userId);

}
