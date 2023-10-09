package date_naeun.naeunserver.config.jwt;

import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetail loadUserByUsername(String email) throws UsernameNotFoundException {
        User findUser = userRepository.findByEmail(email);
        if (findUser == null) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        } else {
            return new CustomUserDetail(findUser);
        }
    }
}
