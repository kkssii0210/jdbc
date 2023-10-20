package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection(); //여기서만 커넥션을 받는다.
        try {
            con.setAutoCommit(false); //트랜잭션 시작
            //비지니스 로직
            bizLogic(con, fromId, toId, money);
            con.commit(); //커밋을 해준다. 성공시 커밋!
        } catch (Exception e){
            con.rollback(); //예외가 생겨서 실패시 롤백!
            throw new IllegalStateException(e);
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close(); // 여기서 그냥 닫아버리면 커넥션풀로 돌아갈때 설정이 autocommit이 false인채로 돌아간다.
                                 // 근데 기본설정이 auto라서 살짝 문제가 생길수있다.
                                 // 그래서 오토커밋을 true로 해주고 돌려주자.
                } catch (Exception e) {
                    log.info("error",e);
                }
            }
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);
        validation(toMember);
        memberRepository.update(con, fromId, fromMember.getMoney()- money);
        memberRepository.update(con, toId, toMember.getMoney()+ money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("아채중 예외 발생");
        }
    }
}
