package com.sparta.memo.repository;

import com.sparta.memo.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository : 데이터베이스와 소통

public interface MemoRepository extends JpaRepository<Memo, Long> {

}