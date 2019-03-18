package com.dd.o2o.dao;

import com.dd.o2o.entity.PersonInfo;

public interface PersonInfoDao {

	PersonInfo queryPersonInfoById(long userId);

	int insertPersonInfo(PersonInfo personInfo);

}
