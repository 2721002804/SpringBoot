package com.dd.o2o.service;

import com.dd.o2o.dto.LocalAuthExecution;
import com.dd.o2o.entity.LocalAuth;
import com.dd.o2o.exceptions.LocalAuthOperationException;

public interface LocalAuthService {
	/**
	 * 
	 * @param userName
	 * @return
	 */
	LocalAuth getLocalAuthByUserNameAndPwd(String username, String password);

	/**
	 * 
	 * @param userId
	 * @return
	 */
	LocalAuth getLocalAuthByUserId(long userId);

	/**
	 * 
	 * @param localAuth
	 * @return
	 * @throws RuntimeException
	 */
	LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException;

	/**
	 * 
	 * @param localAuthId
	 * @param userName
	 * @param password
	 * @param newPassword
	 * @param lastEditTime
	 * @return
	 */
	LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException;
}
