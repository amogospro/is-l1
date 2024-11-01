package com.lab1.lab1.config;

import com.lab1.lab1.model.dto.ErrorResponseDTO;
import com.lab1.lab1.model.entities.User;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import com.lab1.lab1.service.AuthService;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private AuthService authService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Пропускаем аутентификацию для эндпоинтов входа и регистрации
        String path = requestContext.getUriInfo().getPath();
        System.out.println(path);
        if (path.equals("/auth/login") || path.equals("/auth/register")) {
            return;
        }

        // Получаем токен из заголовка Authorization
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer ".length());
            User user = authService.validateToken(token);
            if (user != null) {
                // Устанавливаем SecurityContext с пользователем
                requestContext.setSecurityContext(new SecurityContextImpl(user, requestContext.getSecurityContext().isSecure()));
                return;
            }
        }

        // Прерываем запрос, если аутентификация не удалась
        requestContext.abortWith(
                jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponseDTO("Недействительный или отсутствующий токен аутентификации"))
                        .build()
        );
    }
}
