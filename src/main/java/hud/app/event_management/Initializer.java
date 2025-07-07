//package hud.app.event_management;
//
//import hud.app.event_management.model.*;
//import hud.app.event_management.repository.*;
//import hud.app.event_management.service.CategoryService;
//import hud.app.event_management.utils.FileUtil;
//import org.apache.catalina.User;
//import org.springframework.data.domain.Page;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//public class Initializer implements ApplicationRunner {
//    private final UserAccountRepository userRepository;
//    private final UserEventRepository userEventRepository;
//    private final EventRepository eventRepository;
//    private final CategoryRepository categoryRepository;
//    private final ClubRepository clubRepository;
//    private final ClubSubscribedMembersRepository clubSubscribedMembersRepository;
//    private final EventTypeRepository eventTypeRepository;
//    private final FileUtil fileUtil;
//    @Autowired
//    public Initializer(ClubRepository clubRepository, ClubSubscribedMembersRepository clubSubscribedMembersRepository, CategoryRepository categoryRepository, UserAccountRepository userAccountRepository, UserEventRepository userEventRepository, EventRepository eventRepository, EventTypeRepository eventTypeRepository, FileUtil fileUtil){
//        this.userRepository = userAccountRepository;
//        this.userEventRepository = userEventRepository;
//        this.eventRepository = eventRepository;
//        this.categoryRepository = categoryRepository;
//        this.clubRepository = clubRepository;
//        this.clubSubscribedMembersRepository = clubSubscribedMembersRepository;
//        this.eventTypeRepository = eventTypeRepository;
//        this.fileUtil = fileUtil;
//    }
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        System.out.println(fileUtil.basePath);
//    }
//}
