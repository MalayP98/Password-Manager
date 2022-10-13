package com.key.password_manager.constants;

import com.key.password_manager.utils.Helpers;

public interface CredentialConstants {

    public final static String DEFAULT_SALT = Helpers.randomString();

    public final static String DEFAULT_IV = Helpers.NByteString(16);
}
