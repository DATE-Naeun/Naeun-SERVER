package date_naeun.naeunserver.service;

import date_naeun.naeunserver.exception.ApiErrorException;
import date_naeun.naeunserver.exception.ApiErrorStatus;
import date_naeun.naeunserver.exception.AuthErrorException;
import date_naeun.naeunserver.exception.AuthErrorStatus;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.UpdateUserNicknameRequestDto;
import date_naeun.naeunserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /* 회원 조회 */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long userId) {
        try {
            return userRepository.findOne(userId);
        } catch (Exception e) {
            throw new AuthErrorException(AuthErrorStatus.GET_USER_FAILED);
        }
    }

    @Transactional
    /* 회원 닉네임 변경 */
    public void update(Long userId, UpdateUserNicknameRequestDto updateUserNicknameRequestDto) {
        String newName = updateUserNicknameRequestDto.getUserNickname();
        if (userRepository.findByName(newName).isEmpty()) {
            User user = userRepository.findOne(userId);
            user.setName(newName);
        } else {
            throw new ApiErrorException(ApiErrorStatus.DUPLICATED_USER_NAME);
        }
    }

    @Transactional
    /* 회원 탈퇴 */
    public void delete(Long userId) {
        User user = userRepository.findOne(userId);
        userRepository.delete(user);
    }
}
