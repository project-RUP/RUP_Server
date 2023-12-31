package com.rup.web.controller;

import com.rup.apiPayload.response.ResponseDto;
import com.rup.converter.MemberConverter;
import com.rup.domain.Keyword;
import com.rup.domain.Member;
import com.rup.service.KeywordService;
import com.rup.service.MemberService;
import com.rup.web.dto.request.MemberRequestDto;
import com.rup.web.dto.response.MemberResponseDto;
import com.rup.web.dto.response.MemberResponseDto.MemberDetailResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.rup.utils.ContextUtil.getMemberId;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final MemberConverter memberConverter;
    private final KeywordService keywordService;

    @Operation(summary = "멤버 정보 조회 API", description = "멤버 정보 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK 성공"),
    })
    @GetMapping("/{memberId}")
    public ResponseDto<MemberDetailResponseDto> getMember(@PathVariable("memberId") Long memberId) {
        Member member = memberService.getMember(memberId);
        MemberDetailResponseDto memberInfoResponseDto = memberConverter.memberEntityToMemberInfoDto(member);
        return ResponseDto.of(memberInfoResponseDto);
    }

    @Operation(summary = "존재하는 멤버인지 확인 API", description = "db에 이미 존재하는 멤버인지 확인하는 API입니다. kakaoId를 넣어주시면 됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK 성공"),
    })
    @PostMapping("/exists")
    public ResponseDto<MemberResponseDto.existsInfo> isExistMember(@RequestBody MemberRequestDto.kakaoMember kakaoMember) {
        boolean isExists = memberService.existsMember(kakaoMember.getKakaoId());
        return ResponseDto.of(MemberResponseDto.existsInfo.builder()
                .isExist(isExists)
                .build());
    }

    @Operation(summary = "로그인 API", description = "로그인 API 입니다. kakaoId를 넣어주시면 됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK 성공"),
    })
    @PostMapping("/login")
    public ResponseDto<MemberResponseDto.LoginMember> memberLogin(@RequestBody MemberRequestDto.kakaoMember kakaoMember) {
        return ResponseDto.of(memberService.login(kakaoMember.getKakaoId()));
    }

    @Operation(summary = "회원가입 API", description = "회원가입 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK 성공"),
    })
    @PostMapping("/signUp")
    public ResponseDto<MemberResponseDto.basicResponseDto> signup(@RequestBody MemberRequestDto.signUpDto signUpDto) {
        memberService.signUp(signUpDto);
        return ResponseDto.of(MemberResponseDto.basicResponseDto.builder()
                .basicResponse(true)
                .build());
    }

    @Operation(summary = "키워드 API", description = "키워드 API 입니다. 맴버의 키워드를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK 성공"),
    })
    @GetMapping("/keywords")
    public ResponseDto<MemberResponseDto.keywordDto> keywords() {
        List<Keyword> keywords = keywordService.findAll();

        return ResponseDto.of(MemberResponseDto.keywordDto.builder()
                .keywords(keywords)
                .build());
    }

    @Operation(summary = "멤버 삭제 API", description = "멤버 삭제 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000", description = "OK 성공"),
    })
    @DeleteMapping("")
    public ResponseDto<Void> deleteUser() {
        memberService.deleteMember(getMemberId());
        return ResponseDto.of(null);
    }

}
