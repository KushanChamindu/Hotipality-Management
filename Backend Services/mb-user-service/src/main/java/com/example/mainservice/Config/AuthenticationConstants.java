package com.example.mainservice.Config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.example.mainservice.Entity.RoleType;

public final class AuthenticationConstants {
    public static final Set<RoleType> ADMINTATION_ROLES = new HashSet<>(
            Arrays.asList(RoleType.ADMIN, RoleType.MAINTENANCE));
    public static final long EXPIRE_TOKEN_AFTER_MINUTES = 15;
}
