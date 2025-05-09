package com.app.config;


import com.app.entities.PermissionEntity;
import com.app.entities.RoleEntity;
import com.app.entities.RoleEnum;
import com.app.entities.UserEntity;
import com.app.services.entitiyservices.implementations.PermissionEntityService;
import com.app.services.entitiyservices.implementations.RoleEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {


    private final PermissionEntityService permissionService;
    private final RoleEntityService roleService;


    @Override
    public void run(String... args) throws Exception {


        List<String> perms = List.of("READ", "WRITE", "DELETE", "CREATE_ADMIN","CREATE_DEVELOPER");
        perms.forEach(this::createPermission);


        PermissionEntity read = permissionService.findByName("READ").get();
        PermissionEntity write = permissionService.findByName("WRITE").get();
        PermissionEntity delete = permissionService.findByName("DELETE").get();
        PermissionEntity createAdmin = permissionService.findByName("CREATE_ADMIN").get();
        PermissionEntity createDeveloper = permissionService.findByName("CREATE_DEVELOPER").get();

        createRole(RoleEnum.ADMIN, Set.of(read, write, delete));
        createRole(RoleEnum.USER, Set.of(read, write));
        createRole(RoleEnum.DEVELOPER, Set.of(read, write, delete, createAdmin,createDeveloper));

    
    }


    public PermissionEntity createPermission(String name) {

        return this.permissionService.findByName(name)
                .orElseGet(() -> {
                    PermissionEntity permission = PermissionEntity.builder().name(name).build();
                    this.permissionService.save(permission);

                    return this.permissionService.findByName(name)
                            .orElseThrow(() -> new RuntimeException("Permission could not be found after creation: " + name));
                });
    }

    public RoleEntity createRole(RoleEnum roleEnum, Set<PermissionEntity> permissions) {
        return this.roleService.findByRoleEnum(roleEnum)
                .orElseGet(() -> {

                    Set<PermissionEntity> managedPermissions = permissions.stream()
                            .map(p -> permissionService.findByName(p.getName())
                                    .orElseThrow(() -> new RuntimeException("Permission not found: " + p.getName())))
                            .collect(Collectors.toSet());

                    RoleEntity newRole = RoleEntity.builder()
                            .roleEnum(roleEnum)
                            .permissions(managedPermissions)
                            .build();

                    return this.roleService.save(newRole);
                });
    }

}
