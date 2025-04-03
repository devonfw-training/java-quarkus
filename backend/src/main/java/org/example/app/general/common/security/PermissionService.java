package org.example.app.general.common.security;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.ArrayList;
import java.util.List;

import static org.example.app.general.common.security.ApplicationAccessControlConfig.*;



@ApplicationScoped
public class PermissionService {

    @Inject
    SecurityIdentity securityIdentity;

    private List<String> getPermissions(){
        List<String> permissions = new ArrayList<>();
        List<String> roles = securityIdentity.getRoles().stream().map(String::toUpperCase).toList();
        roles.forEach(role->{
                Roles userRole = Roles.valueOf(role.toUpperCase());
                switch (userRole) {
                    case ADMIN:
                        permissions.add(PERMISSION_FIND_TASK_LIST);
                        permissions.add(PERMISSION_SAVE_TASK_LIST);
                        permissions.add(PERMISSION_DELETE_TASK_LIST);
                        permissions.add(PERMISSION_FIND_TASK_ITEM);
                        permissions.add(PERMISSION_SAVE_TASK_ITEM);
                        permissions.add(PERMISSION_DELETE_TASK_ITEM);
                        break;
                    case USER:
                        permissions.add(PERMISSION_FIND_TASK_LIST);
                        permissions.add(PERMISSION_FIND_TASK_ITEM);
                        permissions.add(PERMISSION_SAVE_TASK_ITEM);
                        break;
                    default:
                        break;
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
