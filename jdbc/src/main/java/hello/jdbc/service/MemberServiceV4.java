package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;


@Slf4j
public class MemberServiceV4 {
    private final MemberRepository memberRepository;
    public MemberServiceV4(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Transactional
    public void accountTransfer(String fromId, String toId, int money){
        bizLogic(fromId, toId, money);
    }
    private void bizLogic(String fromId, String toId, int money) {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);
        validation(toMember);
        memberRepository.update(fromId, fromMember.getMoney()- money);
        memberRepository.update(toId, toMember.getMoney()+ money);
    }
    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("아채중 예외 발생");
        }
    }
}
