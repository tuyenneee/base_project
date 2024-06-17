package com.ivnd.knowledgebase.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 16/05/2024
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        var context = SecurityContextHolder.getContext().getAuthentication();
        if (context == null || !context.isAuthenticated()) {
            return Optional.of("ADMIN");
        }
        return Optional.of(context.getName());
    }
}
