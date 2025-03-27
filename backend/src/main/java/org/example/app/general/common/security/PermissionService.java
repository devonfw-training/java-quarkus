package org.example.app.general.common.security;

import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static org.example.app.general.common.security.ApplicationAccessControlConfig.*;



@ApplicationScoped
public class PermissionService {

    @Inject
    JwtService jwtService;

    @Inject
    SessionService sessionService;

    @Inject
    ContainerRequestContext requestContext;

    private List<String> getPermissions() throws ParseException {
        List<String> permissions = new ArrayList<>();
        String sessionId = requestContext.getCookies().get("SESSION_ID").getValue();
        List<String> roles = jwtService.getRoles(sessionService.getSession(sessionId).get().getJwt());
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
        try {
            if (!getPermissions().contains(requiredPermission)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Access Denied: No valid permissions for the current user")
                        .build();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;  // Return null if the user has permission
    }
}
