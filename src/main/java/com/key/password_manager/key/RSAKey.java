package com.key.password_manager.key;

import java.util.Objects;
import com.key.password_manager.key.types.KeyType;
import com.key.password_manager.key.types.RSAKeyType;

public class RSAKey extends AbstractKey {

	private RSAKeyType subKeyType;

	public RSAKey() {
		super(KeyType.RSA);
	}

	public RSAKey(String key, RSAKeyType type) {
		super(key, KeyType.RSA);
		setSubKeyType(type);
	}

	public RSAKey(RSAKey copyObject) {
		this(copyObject.getKey(), copyObject.getSubKeyType());
	}

	public boolean isPrivateKey() {
		return RSAKeyType.PRIVATE.equals(subKeyType);
	}

	public boolean isPublicKey() {
		return RSAKeyType.PUBLIC.equals(subKeyType);
	}

	public RSAKeyType getSubKeyType() {
		return subKeyType;
	}

	public void setSubKeyType(RSAKeyType type) {
		if (Objects.isNull(type)) {
			throw new NullPointerException(
					"Public/Private key type not provided while creation of RSA Key.");
		}
		this.subKeyType = type;
	}
}
