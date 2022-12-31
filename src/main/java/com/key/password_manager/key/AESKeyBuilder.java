package com.key.password_manager.key;

import java.util.Objects;
import com.key.password_manager.key.types.AESKeyType;

/**
 * Builder used to create {@link AESKey}. Provides facility to set salt, IV, key and a sub key type.
 * This class will allow to build a partial key, i.e. all the properties of the this key should be
 * set. Mostly used by {@link KeyFactory} but can also be used to create custom AES keys.
 * 
 * @apiNote AESKeyBuilder is mainly created to provide ease while creating a custom AES keys. If
 *          user is not aware of Salt, IV and type and just wants a working key they can go with
 *          {@link KeyFactory}.
 */
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

	/**
	 * Checks if the build is partial or not using {@link #isPartialBuild()}, if yes an error is
	 * thrown else the flow moves forward. If no key tpye is mentioned, then {@link AESKeyType#NONE}
	 * is assigned. If not an error is thrown, else the object builds successfully.
	 */
	public AESKey build() throws Exception {
		if (isPartialBuild()) {
			throw new Exception("Cannot build the AESKey as some parameters are missing.");
		} else if (Objects.isNull(aesKey.getSubKeyType()))
			setAESKeyType(AESKeyType.NONE);
		return aesKey;
	}

	/** Checks if salt, iv and key are set into the key object */
	private boolean isPartialBuild() {
		return Objects.isNull(aesKey.getSalt()) || Objects.isNull(aesKey.getIv())
				|| Objects.isNull(aesKey.getKey());
	}
}
