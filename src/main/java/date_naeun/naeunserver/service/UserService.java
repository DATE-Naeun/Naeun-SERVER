package date_naeun.naeunserver.service;

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
        return userRepository.findOne(userId);
    }

    @Transactional
    /* 회원 닉네임 변경 */
    public void update(Long userId, UpdateUserNicknameRequestDto updateUserNicknameRequestDto) {
        User user = userRepository.findOne(userId);
        user.setName(updateUserNicknameRequestDto.getUserNickname());
    }

    @Transactional
    /* 회원 탈퇴 */
    public void delete(Long userId) {
        User user = userRepository.findOne(userId);
        userRepository.delete(user);
    }
}
