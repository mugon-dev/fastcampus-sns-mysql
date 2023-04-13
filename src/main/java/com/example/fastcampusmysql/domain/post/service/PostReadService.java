package com.example.fastcampusmysql.domain.post.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.application.util.CursorRequest;
import com.example.fastcampusmysql.application.util.PageCursor;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostReadService {
	final private PostRepository postRepository;

	private static long getNextKey(List<Post> posts) {
		return posts.stream().mapToLong(Post::getId).min().orElse(CursorRequest.NONE_KEY);
	}

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

	public Page<Post> getPosts(Long memberId, Pageable pageable) {
		return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, pageable);
	}

	public PageCursor<Post> getPosts(Long memberId, CursorRequest cursorRequest) {
		var posts = findAllBy(memberId, cursorRequest);
		var nextKey = getNextKey(posts);
		return new PageCursor<>(cursorRequest.next(nextKey), posts);
	}

	public PageCursor<Post> getPosts(List<Long> memberIds, CursorRequest cursorRequest) {
		var posts = findAllBy(memberIds, cursorRequest);
		var nextKey = getNextKey(posts);
		return new PageCursor<>(cursorRequest.next(nextKey), posts);
	}

	public List<Post> getPosts(List<Long> ids) {
		return postRepository.findAllByIds(ids);
	}

	private List<Post> findAllBy(Long memberId, CursorRequest cursorRequest) {
		if (cursorRequest.hasKey()) {
			return postRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId,
				cursorRequest.size());
		} else {
			return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
		}
	}

	private List<Post> findAllBy(List<Long> memberIds, CursorRequest cursorRequest) {
		if (cursorRequest.hasKey()) {
			return postRepository.findAllByLessThanIdAndInMemberIdsAndOrderByIdDesc(cursorRequest.key(), memberIds,
				cursorRequest.size());
		} else {
			return postRepository.findAllByInMemberIdsAndOrderByIdDesc(memberIds, cursorRequest.size());
		}
	}
}
