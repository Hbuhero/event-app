package hud.app.event_management.serviceImpl;

import hud.app.event_management.dto.response.ClubResponseDto;
import hud.app.event_management.dto.response.UserAccountResponseDto;
import hud.app.event_management.mappers.ClubMapper;
import hud.app.event_management.mappers.UserAccountMapper;
import hud.app.event_management.model.Club;
import hud.app.event_management.model.ClubSubscribedMembers;
import hud.app.event_management.model.UserAccount;
import hud.app.event_management.repository.ClubRepository;
import hud.app.event_management.repository.ClubSubscribedMembersRepository;
import hud.app.event_management.service.ClubSubscriptionService;
import hud.app.event_management.utils.Response;
import hud.app.event_management.utils.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClubSubscriptionServiceImpl implements ClubSubscriptionService {
    private final ClubSubscribedMembersRepository subscriptionRepository;
    private final ClubRepository clubRepository;

    private final ClubMapper clubMapper;
    private final UserAccountMapper userAccountMapper;

    @Autowired
    public ClubSubscriptionServiceImpl(ClubSubscribedMembersRepository subscriptionRepository, ClubRepository clubRepository, ClubMapper clubMapper, UserAccountMapper userAccountMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
        this.userAccountMapper = userAccountMapper;
    }

    @Override
    public Response<String> subscribe(UserAccount userAccount, String uuid) {
        try {

            if (userAccount == null){
                return new Response<>(true, "Anonymous user, Full authentication is required", ResponseCode.UNAUTHORIZED);
            }

            Optional<Club> optionalClub = clubRepository.findFirstByUuid(uuid);
            if (optionalClub.isEmpty()){
                return new Response<>(true, "Club not found", ResponseCode.NO_RECORD_FOUND);
            }

            Club club = optionalClub.get();
            ClubSubscribedMembers clubSubscribedMembers = ClubSubscribedMembers.builder()
                    .club(club)
                    .userAccount(userAccount)
                    .build();
            subscriptionRepository.save(clubSubscribedMembers);
            return new Response<>(false, ResponseCode.SUCCESS, "Subscribed successfully");
        } catch (Exception e) {
            return new Response<>(true, "Failed to subscribe to club with root cause:\n"+ e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<String> unsubscribe(UserAccount userAccount,String uuid) {
        try {

            if (userAccount == null){
                return new Response<>(true, "Anonymous user, Full authentication is required", ResponseCode.UNAUTHORIZED);
            }

            Optional<Club> optionalClub = clubRepository.findFirstByUuid(uuid);
            if (optionalClub.isEmpty()){
                return new Response<>(true, "Club not found", ResponseCode.NO_RECORD_FOUND);
            }

            Club club = optionalClub.get();
            Optional<ClubSubscribedMembers> optionalSubscribedMembers = subscriptionRepository.findByClubAndUserAccount(club, userAccount);
            if (optionalSubscribedMembers.isEmpty()){
                return new Response<>(true, "Subscription not found, please subscribe first", ResponseCode.INVALID_REQUEST);
            }

            subscriptionRepository.delete(optionalSubscribedMembers.get());
            return new Response<>(false, ResponseCode.SUCCESS, "Unsubscribed successfully");
        } catch (Exception e) {
            return new Response<>(true, "Failed to unsubscribe to club with root cause:\n" + e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<UserAccountResponseDto> getAllClubSubscribers(String uuid, Pageable pageable) {
        try {
            Optional<Club> optionalClub = clubRepository.findFirstByUuid(uuid);
            if (optionalClub.isEmpty()){
                return new Response<>(true, "Club not found", ResponseCode.NO_RECORD_FOUND);
            }

            Club club = optionalClub.get();
            Page<ClubSubscribedMembers> clubSubscribedMembers = subscriptionRepository.findAllByClub(club, pageable);
            List<UserAccountResponseDto> userAccounts = clubSubscribedMembers
                    .stream()
                    .map(value -> userAccountMapper.toDto(value.getUserAccount()))
                    .toList();

            return new Response<>(false, ResponseCode.SUCCESS, userAccounts);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get club's all subscribers with root cause: \n" +e.getMessage(), ResponseCode.FAIL);
        }
    }

    @Override
    public Response<ClubResponseDto> getUserSubscribedClubs(UserAccount userAccount,Pageable pageable) {
        try {

            if (userAccount == null){
                return new Response<>(true, "Anonymous user, full authentication is required", ResponseCode.UNAUTHORIZED);
            }

            Page<ClubSubscribedMembers> clubSubscribedMembers = subscriptionRepository.findAllByUserAccount(userAccount, pageable);
            List<ClubResponseDto> clubResponses = clubSubscribedMembers.stream()
                    .map(clubSubscribedMember -> clubMapper.toDto(clubSubscribedMember.getClub()))
                    .toList();

            return new Response<>(false, ResponseCode.SUCCESS, clubResponses);
        } catch (Exception e) {
            return new Response<>(true, "Failed to get user's subscribed clubs with root cause: \n"+e.getMessage(), ResponseCode.FAIL);
        }
    }
}
