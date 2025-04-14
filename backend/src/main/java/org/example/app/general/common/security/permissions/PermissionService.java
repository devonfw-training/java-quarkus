package org.example.app.general.common.security.permissions;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.example.app.general.common.security.RoleMappingConfig;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;



@ApplicationScoped
public class PermissionService {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    RoleMappingConfig roleMappingConfig;

    private List<String> getPermissions(){
        List<String> permissions = new ArrayList<>();
        List<String> roles = securityIdentity.getRoles().stream().map(String::toLowerCase).toList();
        roles.forEach(role->{
            RoleMappingConfig.RoleConfig roleConfig = roleMappingConfig.roles().get(role);
            if(roleConfig!=null){
                roleConfig.attachedGroups().forEach(group->{
                    try {
                        Field field = PermissionCollections.class.getField(group);
                        List<String> groupPermissions = (List<String>) field.get(null); // static field, hence null
                        permissions.addAll(groupPermissions);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        // Log or handle unknown group
                        System.err.println("Group not found in PermissionCollections: " + group);
                    }
                });
            }

        });
        return permissions;
    }

    public Response checkPermission(String requiredPermission) {
        if (!getPermissions().contains(requiredPermission)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Access Denied: No valid permissions for the current user")
                    .build();
        }
        return null;  // Return null if the user has permission
    }
}
