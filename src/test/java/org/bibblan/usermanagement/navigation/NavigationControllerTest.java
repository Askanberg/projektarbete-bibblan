package org.bibblan.usermanagement.navigation;

import org.bibblan.Main;
import org.bibblan.core.controller.NavigationController;
import org.bibblan.usermanagement.config.DisableSecurityConfig;
import org.bibblan.usermanagement.service.UserService;
import org.bibblan.usermanagement.testinitializer.TestContextInitializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@WebMvcTest(NavigationController.class)
@ContextConfiguration(classes = {Main.class, DisableSecurityConfig.class, TestContextInitializer.TestConfig.class})
@TestPropertySource(locations = "classpath:usermanagement/application-test.properties")
@DisplayName("NavigationController tests")
public class NavigationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Correctly redirects to the profile page.")
    public void testProfileRedirect() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(forwardedUrl("/profile.html"));
    }

    @Test
    @DisplayName("Correctly redirects to the home page.")
    public void testHomeRedirect() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(forwardedUrl("/home.html"));
    }

    @Test
    @DisplayName("Correctly redirects to the login page.")
    public void redirectsToLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(forwardedUrl("/login.html"));

    }

    @Test
    @DisplayName("Redirects to register form.")
    public void redirectsToRegisterPage()throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(forwardedUrl("/register.html"));
    }

    @Test
    @DisplayName("Redirects to terms and conditions.")
    public void redirectsToTermsAndConditions()throws Exception {
        mockMvc.perform(get("/terms-and-conditions"))
                .andExpect(forwardedUrl("/terms-and-conditions.html"));
    }
}
