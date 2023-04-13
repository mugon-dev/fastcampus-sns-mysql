package com.example.fastcampusmysql.domain.post.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostReadService {
	final private PostRepository postRepository;

	/**
	 * select *
	 * from Post
	 * where memberId = :memberId and createdDate between firstDate and lastDate
	 * group by createdDate memberId
	 * @param request [작성회원, 시작일, 마지막일]
	 * @return 리스트 [작성일자, 작성회원, 작성 게시물 갯수]
	 */
	public List<DailyPostCount> getDailyPostCount(DailyPostCountRequest request) {
		return postRepository.groupByCreatedDate(request);
	}
}
