package hud.app.event_management.configurations;

import hud.app.event_management.repository.UserAccountRepository;
import hud.app.event_management.utils.userExtractor.LoggedUser;
import hud.app.event_management.utils.userExtractor.LoggedUserResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private final LoggedUser loggedUser;

    public WebMvcConfig(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoggedUserResolver(loggedUser));
    }
}
