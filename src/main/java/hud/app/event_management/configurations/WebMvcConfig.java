package hud.app.event_management.configurations;

import hud.app.event_management.annotations.loggedUser.LoggedUserImpl;
import hud.app.event_management.annotations.loggedUser.LoggedUserResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private final LoggedUserImpl loggedUser;

    public WebMvcConfig(LoggedUserImpl loggedUser) {
        this.loggedUser = loggedUser;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoggedUserResolver(loggedUser));
    }
}
