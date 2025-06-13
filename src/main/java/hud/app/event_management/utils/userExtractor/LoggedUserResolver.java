package hud.app.event_management.utils.userExtractor;

import hud.app.event_management.annotations.CurrentUser;
import hud.app.event_management.repository.UserAccountRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoggedUserResolver implements HandlerMethodArgumentResolver {

    private final LoggedUser loggedUser;

    public LoggedUserResolver(LoggedUser loggedUser) {
        this.loggedUser = loggedUser;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {


        return loggedUser.getUser();
    }
}
