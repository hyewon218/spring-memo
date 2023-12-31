package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.service.MemoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MemoController {
// MemoController > MemoService > MemoRepository

    // MemoService Bean 객체 받아서 사용하고 있음.
    private final MemoService memoService;

    // Controller의 역할 : 클라이언트의 요청을 받고 클라이언트의 요청을 Service에 전달한다.
    // 받아 온 데이터가 있다면 그 데이터도 함께 전달한다.
    // 만들어진 MemoService 객체를 외부에서 파라미터로 전달받아서 집어 넣는다.
    // -> DI(의존성 주입)를 하기 위해서는 객체의 생성이 우선이 되어야 한다.
    public MemoController(MemoService memoService) {
        // MemoController가 MemoController 직접 만들고 있다.(제어의 흐름 : MemoController > memoService)
        // this.memoService = new MemoService(jdbcTemplate);

        // 제어의 흐름 : memoService > MemoController
        // memoService를 받아서 필드에 넣어주고 있다.
        this.memoService = memoService;
    }

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // Controller 에서는 데이터만 전달하게끔 변경
        // 클라이언트에서 전달 받은 requestDto도 같이 사용하라고 service에게 보내줌
        // memoService class 에 있는 createMemo 메서드에서 비지니스 로직이 수행되고
        // return이 되면 그 값을 바로 return 해서 클라이언트로 보낸다.
        return memoService.createMemo(requestDto);
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        return memoService.getMemos();
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        return memoService.updateMemo(id, requestDto);
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        return memoService.deleteMemo(id);
    }
}