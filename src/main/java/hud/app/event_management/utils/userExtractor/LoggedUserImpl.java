package hud.app.event_management.utils.userExtractor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashMap;

import hud.app.event_management.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import lombok.extern.slf4j.Slf4j;
import hud.app.event_management.repository.UserAccountRepository;

@Slf4j
@Service
public class LoggedUserImpl implements LoggedUser, Serializable {

    @Autowired
    private UserAccountRepository repository;

    @Autowired
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(LoggedUserImpl.class);

    public LoggedUserImpl() {
        this.objectMapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    @Override
    public UserInfo getInfo() {
        // get current authentication object from SecurityContextHolder
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            log.info("Null Auth detected");
            return null;
        } else {
            try {
                // Convert the authentication object to JSON
                String jsonAuth = objectMapper.writeValueAsString(auth);
                // Parse it to a HashMap for easy usage
                HashMap<String, Object> result = objectMapper.readValue(jsonAuth, HashMap.class);

                if (result.get("principal") != null) {
                    // Retrieve Principal from the hashmap
                    Object principal = result.get("principal");
                    if (principal.toString().contains("anonymousUser")) {
                        // Handle anonymous user case
                        return null; // You can set appropriate values for an anonymous user
                    }

                    // retrieve the objects of the principal to hashmap
                    HashMap<String, Object> principalMap = (HashMap<String, Object>) principal;
                    log.info("User principal found: {}", principalMap);

//                    Object id = principalMap.get("id");
                    Object username = principalMap.get("username");
                    Object uuid = principalMap.get("uuid"); // Add this line to get the email

                    return new UserInfo(
//                            Long.parseLong(id.toString()), // Pass id as Long
//                            uuid != null ? uuid.toString() : null, // Pass email or null,
                            username != null ? username.toString() : null ,// Pass username or null
                            true, // Add your logic for accountNonExpired
                            true, // Add your logic for accountNonLocked
                            true, // Add your logic for credentialsNonExpired
                            true // Add your logic for enabled
                    );
                }
            } catch (Exception e) {
                log.error("Error occurred on authentication facade: {}", e.getMessage());
                log.error("Exception", e);
            }
        }
        return null;
    }

    @Override
    public UserAccount getUser() {
        UserInfo userInfo = getInfo();
        if (userInfo != null && userInfo.getEmail() != null) {
            return repository.findFirstByUsername(userInfo.getEmail()).orElse(null);
        }
        return null;
    }


}

