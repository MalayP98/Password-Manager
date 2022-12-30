package com.key.password_manager.key;

import java.util.Objects;
import com.key.password_manager.key.types.AESKeyType;

public class AESKeyBuilder {

	private AESKey aesKey;

	public AESKeyBuilder() {
		this.aesKey = new AESKey();
	}

	public AESKeyBuilder setKey(String key) {
		this.aesKey.setKey(key);
		return this;
	}

	public AESKeyBuilder setSalt(String salt) {
		this.aesKey.setSalt(salt);
		return this;
	}

	public AESKeyBuilder setIV(String iv) {
		this.aesKey.setIv(iv);
		return this;
	}

	public AESKeyBuilder setAESKeyType(AESKeyType aesKeyType) {
		this.aesKey.setSubKeyType(aesKeyType);
		return this;
	}

	public AESKey build() throws Exception {
		if (Objects.isNull(aesKey.getSalt()) || Objects.isNull(aesKey.getIv())
				|| Objects.isNull(aesKey.getKey())) {
			throw new Exception("Cannot build the AESKey as some parameters are missing.");
		} else if (Objects.isNull(aesKey.getSubKeyType()))
			setAESKeyType(AESKeyType.NONE);
		return aesKey;
	}
}
