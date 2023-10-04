package date_naeun.naeunserver.api;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.History;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.HistoryCosmeticDto;
import date_naeun.naeunserver.dto.HistoryDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.service.CosmeticService;
import date_naeun.naeunserver.service.HistoryService;
import date_naeun.naeunserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
    public ResultDto<List<HistoryDto>> getComparisonCosmetic(@RequestHeader HttpHeaders headers) {

        // 토큰으로 사용자 찾기
        User user = getUser(headers);

        // 비교기록 가져오기
        List<History> findHistories = historyService.getHistoryById(user);

        List<HistoryDto> historyDtos = new ArrayList<>();

        // 화장품 리스트 가져오기
        for (History history : findHistories) {
            // 비교 화장품 가져오기
            List<Cosmetic>  cosmetics = cosmeticService.findByIdList(history.getCosmeticList());

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
     * To Do
     * - JWT 토큰으로 사용자 id 가져오는 로직 구현
     */
    /* access Token 으로 사용자를 받아오는 메서드 */
    private User getUser(HttpHeaders headers) {
        // Request Header에서 accessToken 값을 추출
        String accessToken = headers.getFirst("accessToken");
        System.out.println(accessToken);
        // accessToken 유효성 검사: accessToken을 검증하고 사용자 ID를 추출하는 로직 구현

        Long userId = 1L;
        return userService.findUserById(userId);        // 액세스 토큰을 사용하여 사용자 id 조회
    }
}
