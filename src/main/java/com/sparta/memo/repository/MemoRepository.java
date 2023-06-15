package com.sparta.memo.repository;

import com.sparta.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository : 데이터베이스와 소통

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findAllByOrderByModifiedAtDesc();

    // 이 파라미터 username 을 통해서 SQL을 동적으로 처리할 수 있다.
    // username 필드만 가져오기 (로비가 작성한 모든 메모를 가져온다.)
    // List<Memo> findAllByUsername(String username);
}