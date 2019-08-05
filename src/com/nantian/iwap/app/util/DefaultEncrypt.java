package com.nantian.iwap.app.util;

import com.nantian.iwap.security.cipher.impl.IwapMd5Encrypt;

public class DefaultEncrypt implements PasswordEncrypt {

	@Override
	public String encryptPassword(String userId, String pwd) {
		return IwapMd5Encrypt.instance().getMD5ofStr(userId, pwd);
	}

}
