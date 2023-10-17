package date_naeun.naeunserver.config.jwt;

import date_naeun.naeunserver.exception.AuthErrorException;
import date_naeun.naeunserver.exception.AuthErrorStatus;
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
            throw new AuthErrorException(AuthErrorStatus.GET_USER_FAILED);
        } else {
            return new CustomUserDetail(findUser);
        }
    }
}
