package org.bibblan.usermanagement.testinitializer;

import org.bibblan.usermanagement.config.DisableSecurityConfig;
import org.bibblan.usermanagement.repository.RoleRepository;
import org.bibblan.usermanagement.repository.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Validator;

import static org.mockito.Mockito.*;

@ContextConfiguration(initializers = TestContextInitializer.class)
public class TestContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        context.getBeanFactory().registerSingleton("roleRepository", mock(RoleRepository.class));
        context.getBeanFactory().registerSingleton("userRepository", mock(UserRepository.class));
        context.getBeanFactory().registerSingleton("passwordEncoder", new BCryptPasswordEncoder());
        context.getBeanFactory().registerSingleton("validator", mock(Validator.class));
    }

    @TestConfiguration
    @Import(DisableSecurityConfig.class)
    public static class TestConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(10);
        }

    }
}