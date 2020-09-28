package com.springjpaexercise.jpashop.api;


import com.springjpaexercise.jpashop.domain.Member;
import com.springjpaexercise.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // Entity를 바로 파라미터로 받으면 누군가가 Entity 컬럼명을 수정하면 API Spec 자체가 바뀌어 문제가 생긴다.
    // Entity는 사용 되는 곳이 많아서 수정 가능성이 높다.
    @PostMapping("api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    //DTO 사용하자
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.name);

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);

    }

    @Data
    private class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }

    @Data
    private class CreateMemberRequest {
        @NotEmpty
        private String name;
    }
}
