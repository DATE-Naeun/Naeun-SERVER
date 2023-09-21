package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.UserUpdateRequestDto;
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

    /* 회원 가입 */
    @Transactional
    public Long join(User user) {
        validateDuplicateMember(user);    // 중복 회원 검증
        userRepository.save(user);
        return user.getId();
    }

    //== 중복 회원 검증 ==//
    private void validateDuplicateMember(User user) {
        List<User> findMembers = userRepository.findByName(user.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /* 회원 조회 */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserById(Long userId) {
        return userRepository.findOne(userId);
    }

    @Transactional
    /* 회원 닉네임 변경 */
    public void update(Long userId, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findOne(userId);
        user.setName(userUpdateRequestDto.getUserNickname());
    }

    @Transactional
    /* 회원 탈퇴 */
    public void delete(Long userId) {
        User user = userRepository.findOne(userId);
        userRepository.delete(user);
    }
}
