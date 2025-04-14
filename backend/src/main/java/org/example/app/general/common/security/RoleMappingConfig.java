package org.example.app.general.common.security;

import io.smallrye.config.ConfigMapping;

import java.util.List;
import java.util.Map;

@ConfigMapping(prefix = "authorization.mapping")
public interface RoleMappingConfig {

    Map<String, RoleConfig> roles();

    interface RoleConfig {
        String roleName();
        List<String> attachedGroups();
    }
}