package com.sparta.memo.repository;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

// Repository : 데이터베이스와 소통
@Repository //‘Bean’ 클래스의 역할을 명시
public class MemoRepository {

    private final JdbcTemplate jdbcTemplate;

    // JdbcTemplate을 생성자 파라미터로 받아와서 만들어지는 MemoRepository를
    // 어떤 외부에서 만들어 가지고 Service에 집어넣고
    // 또 그걸 받은 MemoService 완성된 MemoService 를
    // Controller 에 파라미터에 의해서 전달 받아서 주입 받으면 된다.
    public MemoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 파라미터로 메모 받아 옴
    public Memo save(Memo memo) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";
        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, memo.getUsername());
                    preparedStatement.setString(2, memo.getContents());
                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        // memo에 id 넣어주고
        memo.setId(id);

        // 그대로 다시 반환
        return memo;
    }

    public List<MemoResponseDto> findAll() {
        // DB 조회
        String sql = "SELECT * FROM memo";

        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                return new MemoResponseDto(id, username, contents);
            }
        });
    }

    public void update(Long id, MemoRequestDto requestDto) {
        String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM memo WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Memo findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM memo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Memo memo = new Memo();
                memo.setUsername(resultSet.getString("username"));
                memo.setContents(resultSet.getString("contents"));
                return memo;
            } else {
                return null;
            }
        }, id);
    }

    // 부모메서드에 트랜잭션이 존재한다면 자식메서드의 트랜잭션은 부모메서드의 트랜잭션에 합류하게 된다. (트랜잭션이 쭉 이어지게 된다. 부모메서드까지 끝나고 commit)
    @Transactional(propagation = Propagation.REQUIRED) // 변경감지 -> update 쿼리 실행 / REQUIRED : 기본
    public Memo createMemo(EntityManager em) { // 부모 메서드(test3()) 에서 보내준 EntityManager 받아서 사용
        Memo memo = em.find(Memo.class, 1); //1번 @Transactional 테스트 중! Robbert 가지고 옴
        memo.setUsername("Robbert"); // 데이터 바꿔줌
        memo.setContents("@Transactional 전파 테스트 중! 2");// 데이터 바꿔줌

        System.out.println("createMemo 메서드 종료");
        return memo;
    }
}