package date_naeun.naeunserver.api;

import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.History;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.CosmeticDto;
import date_naeun.naeunserver.dto.HistoryCosmeticDto;
import date_naeun.naeunserver.dto.HistoryDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.service.CosmeticService;
import date_naeun.naeunserver.service.HistoryService;
import date_naeun.naeunserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class HistoryApiController {

    private final UserService userService;
    private final HistoryService historyService;
    private final CosmeticService cosmeticService;

    /**
     * 이전 화장품 비교기록 가져오기
     */
    @GetMapping("/api/comparison/history")
    public ResultDto<List<HistoryDto>> getComparisonCosmetic(@AuthenticationPrincipal CustomUserDetail userDetail) {

        // 토큰으로 사용자 찾기
        User user = getUser(userDetail);

        // 비교기록 저장 (sample)
        historyService.saveHistory(user);

        // 비교기록 가져오기
        List<History> findHistories = historyService.getHistoryList(user);

        List<HistoryDto> historyDtos = new ArrayList<>();

        // 화장품 리스트 가져오기
        for (History history : findHistories) {
            // 비교 화장품 가져오기
            List<Cosmetic> cosmetics = cosmeticService.getCosmeticsByHistory(history);

            // 화장품 리스트가 비어 있는 경우
            if (cosmetics == null) {
                return ResultDto.of(HttpStatus.OK, "비교했던 화장품이 없습니다.", null);
            }

            // 비교했던 화장품을 dto에 담는다.
            List<HistoryCosmeticDto> cosmeticDtos = cosmetics.stream()
                    .map(HistoryCosmeticDto::new)
                    .collect(Collectors.toList());

            HistoryDto historyDto = new HistoryDto(history, cosmeticDtos);
            historyDtos.add(historyDto);
        }

        return ResultDto.of(HttpStatus.OK, "이전 비교 기록 가져오기 성공", historyDtos);
    }

    /**
     * 사용자 피부타입에서 가장 인기 있는 화장품 3개 가져오기
     */
    @GetMapping("/api/cosmetic/ranking")
    public ResultDto<Object> getCosmeticRankingBySkinType(@AuthenticationPrincipal CustomUserDetail userDetail) {
        User user = getUser(userDetail);
        List<Cosmetic> top3 = cosmeticService.getTop3(user.getSkinType());
        List<CosmeticDto> cosmeticDtos = top3.stream().map(CosmeticDto::new).collect(Collectors.toList());
        return ResultDto.of(HttpStatus.OK, "피부타입별 많이 비교한 화장품 가져오기 성공", cosmeticDtos);
    }

    /**
     * JWT 토큰으로 사용자를 받아오는 메서드
     */
    private User getUser(CustomUserDetail userDetail) {
        if (userDetail == null) {
            throw new EntityNotFoundException("해당 토큰으로 사용자를 조회할 수 없습니다.");
        }
        // accessToken 검증 후 생성된 userDetail의 id로 user 찾아서 생성
        return userService.findUserById(userDetail.getId());
    }

}
