package com.dd.o2o.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dd.o2o.dao.LocalAuthDao;
import com.dd.o2o.dto.LocalAuthExecution;
import com.dd.o2o.entity.LocalAuth;
import com.dd.o2o.enums.LocalAuthStateEnum;
import com.dd.o2o.exceptions.LocalAuthOperationException;
import com.dd.o2o.service.LocalAuthService;
import com.dd.o2o.util.MD5;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {
	@Autowired
	private LocalAuthDao localAuthDao;

	@Override
	public LocalAuth getLocalAuthByUserNameAndPwd(String username, String password) {
		// TODO Auto-generated method stub
		return localAuthDao.queryLocalByUserNameAndPwd(username, MD5.getMd5(password));
	}

	@Override
	public LocalAuth getLocalAuthByUserId(long userId) {
		// TODO Auto-generated method stub
		return localAuthDao.queryLocalByUserId(userId);
	}

	@Override
	@Transactional
	public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws LocalAuthOperationException {
		if (localAuth == null || localAuth.getPassword() == null || localAuth.getUsername() == null
				|| localAuth.getPersonInfo() == null || localAuth.getPersonInfo().getUserId() == null) {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
		// 查询此用户是否已绑定过平台账号
		LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth.getPersonInfo().getUserId());
		if (tempAuth != null) {
			// 如果绑定过则直接退出
			return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
		}
		try {
			// 如果之前没有绑定过平台，测创建一个平台账号与该用户绑定
			localAuth.setCreateTime(new Date());
			localAuth.setLastEditTime(new Date());
			localAuth.setPassword(MD5.getMd5(localAuth.getPassword()));
			int effectedNum = localAuthDao.insertLocalAuth(localAuth);
			if (effectedNum <= 0) {
				throw new LocalAuthOperationException("账号绑定失败");
			} else {
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS, localAuth);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new LocalAuthOperationException("insertLocalAuth error:" + e.getMessage());
		}
	}

	@Override
	@Transactional
	public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword)
			throws LocalAuthOperationException {
		if (userId != null && username != null && password != null && newPassword != null
				&& !password.equals(newPassword)) {
			try {
				int effectedNum = localAuthDao.updateLocalAuth(userId, username, MD5.getMd5(password),
						MD5.getMd5(newPassword), new Date());
				if (effectedNum <= 0) {
					throw new LocalAuthOperationException("更新密码失败");
				}
				return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
			} catch (Exception e) {
				throw new LocalAuthOperationException("更新密码失败:" + e.getMessage());
			}
		} else {
			return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
		}
	}

}
