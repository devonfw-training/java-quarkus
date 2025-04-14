package org.example.app.general.common.security.permissions;


import java.util.ArrayList;
import java.util.List;

import static org.example.app.general.common.security.permissions.ApplicationAccessControlConfig.*;

public class PermissionCollections {
    public static final List<String> USER_GROUP = List.of(
            PERMISSION_FIND_TASK_LIST,
            PERMISSION_FIND_TASK_ITEM,
            PERMISSION_SAVE_TASK_ITEM
    );

    public static final List<String> ADMIN_GROUP = List.of(
            PERMISSION_DELETE_TASK_ITEM,
            PERMISSION_DELETE_TASK_LIST,
            PERMISSION_SAVE_TASK_LIST
    );
}
