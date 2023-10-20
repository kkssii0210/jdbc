package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId); //돈 보낼사람을 멤버리포지토리에서 꺼내온다.
        Member toMember = memberRepository.findById(toId);     //돈 받을사람을 멤버리포지토리에서 꺼내온다.
        validation(toMember);
        memberRepository.update(fromId, fromMember.getMoney()-money); // 보낸사람의 돈을 보낸 돈 만큼 깍는다.
        memberRepository.update(toId, toMember.getMoney()+money); //받은 사람의 돈을 받은 돈 만큼 더한다.
    }
    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("아채중 예외 발생");
        }
    }
}
