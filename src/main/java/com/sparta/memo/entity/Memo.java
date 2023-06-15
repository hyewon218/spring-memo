package com.sparta.memo.entity;

import com.sparta.memo.dto.MemoRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "memo") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Memo extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "contents", nullable = false, length = 500)
    private String contents;

    public Memo(MemoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }

    // 변경할 데이터(requestDto) 받아와서 변경
    // set 메서드로 변경하는 게 아니라 update 메서드를 만들어서 수정
    // 변경 감지 -> 영속성 컨테스트가 존재해야 함
    public void update(MemoRequestDto requestDto) {
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }
}