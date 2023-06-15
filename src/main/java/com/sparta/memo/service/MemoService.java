package com.sparta.memo.service;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import com.sparta.memo.repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//@Component // Bean 객체 등록
@Service // ‘Bean’ 클래스의 역할을 명시
// [Lombok 주입]
// @RequiredArgsConstructor // final 로 선언된 멤버 변수를 파라미터로 사용하여 생성자를 자동으로 생성한다.
public class MemoService {

    // MemoService Bean 객체 받아서 사용하고 있음.
    private final MemoRepository memoRepository;

    // 만들어진 MemoRepository 를 파라미터로 전달받아서 집어 넣는다.
    // MemoRepository 를 주입받으려면 그 주입하는 생성자나에 @Autowired 를 달아야 한다. (생략가능 -생성자 선언 1개일 때만)
    @Autowired
    public MemoService(MemoRepository memoRepository) {
        // 여기서 MemoRepository 를 딱 하나 만들어 준다.
        // MemoService 가 MemoRepository 를 직접 만들고 있다.(제어의 흐름 : MemoService >MemoRepository)
        // this.memoRepository = new MemoRepository(jdbcTemplate);

        // 제어의 흐름 : MemoRepository > MemoService
        // 자동으로 구현이 되는 SimpleJpaRepository 가 memoRepository 에 Memo 타입으로 들어옴
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
        // 반환타입이 List<MemoResponseDto>니까 Memo -> MemoResponseDto 타입으로 변환
        // stream 에서 memo 가 하나씩 빠져갈 거고 map 에 의해서 변환이 될 건데
        // MemoResponseDto 의 생성자 중에서 Memo 를 파라미터로 가지고 있는 생성자가 호출이 되고
        // 그게 하나씩 변환이 되면서 그 뭉덩이를 List 타입으로 바꿔준다.
        // findAllByOrderByModifiedAtDesc : 직접 만든 메서드
        return memoRepository.findAllByOrderByModifiedAtDesc().stream().map(MemoResponseDto::new).toList();
    }

    // 부모메서드에 transaction 없어서 추가
    // 트랜잭션 환경을 만들어 줘야 영속성 컨테스트가 유지가 되면서 변경 감지를 할 수 있다.
    @Transactional
    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인

        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        // memo 내용 수정
        memo.update(requestDto);

        return id;
    }

    public Long deleteMemo(Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        // memo 삭제
        // delete(지울 객체)
        memoRepository.delete(memo);

        return id;

    }

    // 중복되는 것 빼주기 (해당 메모가 DB에 존재하는지 확인)
    // id 받아와서 findById 실행해서 확인
    // 반환형이 Optional null 체크 필요! -> orElseThrow 로 null 체크
    // null 값이 아니라면 Memo가 반환이 되고, 데이터가 없으면 null이 반환된다.
    // 데이터가 있으면 Memo 로 반환 됨
    private Memo findMemo(Long id) {
        return memoRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 메모는 존재하지 않습니다.")
        );
    }
}