package hud.app.event_management.annotations.loggedUser;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoggedUserResolver implements HandlerMethodArgumentResolver {

    private final LoggedUserImpl loggedUser;

    public LoggedUserResolver(LoggedUserImpl loggedUser) {
        this.loggedUser = loggedUser;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoggedUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {


        return loggedUser.getUser();
    }
}
