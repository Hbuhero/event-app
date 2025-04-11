//package hud.app.event_management;
//
//import hud.app.event_management.model.*;
//import hud.app.event_management.repository.*;
//import hud.app.event_management.service.CategoryService;
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
//
//    @Autowired
//    public Initializer(ClubRepository clubRepository, ClubSubscribedMembersRepository clubSubscribedMembersRepository, CategoryRepository categoryRepository, UserAccountRepository userAccountRepository, UserEventRepository userEventRepository, EventRepository eventRepository, EventTypeRepository eventTypeRepository){
//        this.userRepository = userAccountRepository;
//        this.userEventRepository = userEventRepository;
//        this.eventRepository = eventRepository;
//        this.categoryRepository = categoryRepository;
//        this.clubRepository = clubRepository;
//        this.clubSubscribedMembersRepository = clubSubscribedMembersRepository;
//        this.eventTypeRepository = eventTypeRepository;
//    }
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        try{
//            if (userRepository.count() > 1) return;
//            EventType workshop = EventType.builder()
//                    .type("workshop")
//                    .build();
//            EventType training = EventType.builder()
//                    .type("training")
//                    .build();
//            UserAccount userAccount = UserAccount.builder()
//                    .phone("123243452")
//                    .firstName("hud")
//                    .lastName("said")
//                    .username("hbuhero")
//                    .build();
//
//            UserAccount userAccount1 = UserAccount.builder()
//                    .phone("1232434252")
//                    .firstName("hud")
//                    .lastName("said")
//                    .username("nbuhero")
//                    .build();
//
//            Category category = new Category("testing");
//
//            Club club = Club.builder()
//                    .name("testing")
//                    .email("testing@email")
//                    .address("testing")
//                    .phone("22435153434")
//                    .picUrl("testing")
//                    .clubAdmin(userAccount)
//                    .build();
//
//            Event event = Event.builder()
//                    .title("Testing")
//                    .hostedBy("hud")
//                    .startingDate(LocalDateTime.now())
//                    .endingDate(LocalDateTime.now().plusDays(3))
//                    .location("dar")
//                    .about("testing")
//                    .type(workshop)
//                    .category(category)
//                    .eventStatus(EventStatus.UPCOMING)
//                    .url("testing")
//                    .club(club)
//                    .build();
//
//            Event event1 = Event.builder()
//                    .title("Testing1")
//                    .type(training)
//                    .hostedBy("hud1")
//                    .startingDate(LocalDateTime.now())
//                    .endingDate(LocalDateTime.now().plusDays(4))
//                    .location("dar")
//                    .about("testing")
//                    .category(category)
//                    .eventStatus(EventStatus.UPCOMING)
//                    .url("testing")
//                    .club(club)
//                    .build();
//
//            UserEvent userEvent = UserEvent.builder()
//                    .event(event)
//                    .userAccount(userAccount)
//                    .build();
//
//            UserEvent userEvent1 = UserEvent.builder()
//                    .event(event)
//                    .userAccount(userAccount1)
//                    .build();
//
//            ClubSubscribedMembers clubMembers = ClubSubscribedMembers.builder()
//                    .club(club)
//                    .userAccount(userAccount)
//                    .build();
//
//            ClubSubscribedMembers clubMembers1 = ClubSubscribedMembers.builder()
//                    .club(club)
//                    .userAccount(userAccount1)
//                    .build();
//
//            System.out.println("data made");
//            userRepository.saveAll(List.of(userAccount, userAccount1));
//            eventTypeRepository.saveAll(List.of(workshop, training));
//            categoryRepository.save(category);
//            clubRepository.save(club);
//            eventRepository.saveAll(List.of(event, event1));
//            userEventRepository.saveAll(List.of(userEvent, userEvent1));
//            clubSubscribedMembersRepository.saveAll(List.of(clubMembers1, clubMembers));
//            System.out.println("data saved");
//           // Page<Event> events = categoryService.getEventsByCategoryUuid(category.getUuid(), null);
//            System.out.println("fetching");
//           // System.out.println(events.getTotalElements());
//            System.out.println("trying");
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
//}
