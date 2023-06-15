package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class MemoService {

    private final MemoRepository memoRepository;

    // 만들어진 MemoRepository를 파라미터로 전달받아서 집어 넣는다.
    public MemoService(MemoRepository memoRepository) {
        // 여기서 MemoRepository 를 딱 하나 만들어 준다.
        // MemoService가 MemoRepository를 직접 만들고 있다.(제어의 흐름 : MemoService >MemoRepository)
        // this.memoRepository = new MemoRepository(jdbcTemplate);

        // 제어의 흐름 : MemoRepository > MemoService
        this.memoRepository = memoRepository;
    }

    // 반환타입이 Controller 메서드와 동일해야 한다.
    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        // RequestDto -> Entity
        // 메모 클래스 객체 하나가 데이터 베이스의 한줄, 한 row 이다.
        Memo memo = new Memo(requestDto);

        // DB 저장
        // 메모 한 줄 저장 후 Momo 반환
        Memo saveMemo = memoRepository.save(memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(saveMemo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        // DB 조회
        return memoRepository.findAll();
    }

    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = memoRepository.findById(id);
        if (memo != null) {
            // memo 내용 수정
            memoRepository.update(id, requestDto);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    public Long deleteMemo(Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = memoRepository.findById(id);
        if (memo != null) {
            // memo 삭제
            memoRepository.delete(id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}