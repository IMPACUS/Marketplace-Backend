package com.impacus.maketplace.annotation;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithUserPrincipalsSecurityContextFactory
        implements WithSecurityContextFactory<WithUserPrincipals> {

    @Override
    public SecurityContext createSecurityContext(WithUserPrincipals annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                annotation.email(), annotation.password());

        context.setAuthentication(authenticationToken);
        return context;
    }
}
