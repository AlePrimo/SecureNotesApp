package com.app.config;


import com.app.entities.PermissionEntity;
import com.app.entities.RoleEntity;
import com.app.entities.RoleEnum;
import com.app.services.entitiyservices.implementations.PermissionEntityService;
import com.app.services.entitiyservices.implementations.RoleEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PermissionEntityService permissionService;
    private final RoleEntityService roleService;


    @Override
    public void run(String... args) throws Exception {

        PermissionEntity read = createPermission("READ");
        PermissionEntity write = createPermission("WRITE");
        PermissionEntity delete = createPermission("DELETE");
        PermissionEntity createAdmin = createPermission("CREATE_ADMIN");

        // roles
        createRole(RoleEnum.ADMIN, Set.of(read, write, delete));
        createRole(RoleEnum.USER, Set.of(read,write));
        createRole(RoleEnum.DEVELOPER, Set.of(read, write,delete, createAdmin));
    }

    private PermissionEntity createPermission(String name) {
        return this.permissionService.findByName(name)
                .orElseGet(() -> this.permissionService.save(PermissionEntity.builder().name(name).build()));
    }


    private void createRole(RoleEnum roleEnum, Set<PermissionEntity> permissions) {
        this.roleService.findByRoleEnum(roleEnum)
                .orElseGet(() -> this.roleService.save(
                        RoleEntity.builder()
                                .roleEnum(roleEnum)
                                .permissions(permissions)
                                .build()
                ));
    }







}
