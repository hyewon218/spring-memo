package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//@Component // Bean 객체 등록
@Service // ‘Bean’ 클래스의 역할을 명시
// [Lombok 주입]
// @RequiredArgsConstructor // final로 선언된 멤버 변수를 파라미터로 사용하여 생성자를 자동으로 생성한다.
public class MemoService {

    // MemoService Bean 객체 받아서 사용하고 있음.
    private final MemoRepository memoRepository;
    // 만들어진 MemoRepository 를 파라미터로 전달받아서 집어 넣는다.
    // MemoRepository 를 주입받으려면 그 주입하는 생성자나에 @Autowired 를 달아야 한다. (생략가능 -생성자 선언 1개일 때만)
    @Autowired
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