package com.tickety.configs;

import com.tickety.filters.UserProvisioningFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final UserProvisioningFilter userProvisioningFilter;

    @Bean
    public FilterRegistrationBean<UserProvisioningFilter> userProvisioningFilterRegistration() {
        FilterRegistrationBean<UserProvisioningFilter> registration =
                new FilterRegistrationBean<>(userProvisioningFilter);
        registration.setOrder(2);
        return registration;
    }
}
