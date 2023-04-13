package com.example.fastcampusmysql.application.usecase;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.application.util.CursorRequest;
import com.example.fastcampusmysql.application.util.PageCursor;
import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.service.PostReadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetTImelinePostsUsecase {
	final private FollowReadService followReadService;
	final private PostReadService postReadService;

	public PageCursor<Post> execute(Long memberId, CursorRequest cursorRequest) {
		/*
			1. memberId -> follow 조회
			2. 1번 결과로 게시물 조회
		 */
		var followings = followReadService.getFollowings(memberId);
		var followingMemberIds = followings.stream().map(Follow::getToMemberId).toList();
		return postReadService.getPosts(followingMemberIds, cursorRequest);
	}
}
