package org.qqbot.mapper;

import org.qqbot.entity.PermissionItem;

public interface PermissionMapper {

  PermissionItem getPermission(PermissionItem item);

  void updatePermission(PermissionItem item);

  void removePermission(PermissionItem item);
}
