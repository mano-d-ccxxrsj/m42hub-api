package com.m42hub.m42hub_api.config;

import com.m42hub.m42hub_api.user.service.PermissionService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class PermisionInitializer {

    @Autowired
    private PermissionService permissionService;

    @PostConstruct
    @Transactional
    public void extractAndSavePermissions() {
        Reflections reflections = new Reflections(
                "com.m42hub.m42hub_api",
                Scanners.TypesAnnotated,
                Scanners.MethodsAnnotated
        );
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(RestController.class);

        Pattern permissionPattern = Pattern.compile("hasAuthority\\('([^']+)'\\)");

        for (Class<?> controllerClass : controllerClasses) {
            for (var method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(PreAuthorize.class)) {
                    String expression = method.getAnnotation(PreAuthorize.class).value();

                    Matcher matcher = permissionPattern.matcher(expression);
                    while (matcher.find()) {
                        String permission = matcher.group(1);
                        permissionService.registerPermission(permission);
                    }
                }
            }
        }
    }


}
