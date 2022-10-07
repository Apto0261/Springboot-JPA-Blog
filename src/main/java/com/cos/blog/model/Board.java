package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob
	private String content; //섬머노트 라이브러리
	
	private int count; //조회수
	
	@ManyToOne(fetch = FetchType.EAGER) // Many = Board, User = One ,, EAGER 전략은 연관 데이터(객체)도 다가져옴 영속성 컨텍스트에 ,, LAZYLOADING  은 연관 프록시 객체를 가져옴
	@JoinColumn(name="userId")
	private User user; //DB는 오브젝트를 저장 할 수없다 . FK 로 조인해서 땅겨요지,... 자바는 오브젝트를 저장할 수 있다.
	
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER ,cascade = CascadeType.REMOVE) // mappedBy 연관관계의 주인이 아니다 (FK 아니다) DB에 컬럼을 만들지 마세요.
	@JsonIgnoreProperties({"board"}) // 무한 참조 방지
	@OrderBy("id desc") // 내림차순 
	private List<Reply> replys;
	
	@CreationTimestamp
	private Timestamp createDate;

}
