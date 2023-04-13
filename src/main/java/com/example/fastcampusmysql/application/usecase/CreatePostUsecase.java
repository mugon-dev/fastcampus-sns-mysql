package com.example.fastcampusmysql.application.usecase;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import com.example.fastcampusmysql.domain.post.service.TimelineWriteService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CreatePostUsecase {
	final private PostWriteService postWriteService;
	final private FollowReadService followReadService;
	final private TimelineWriteService timelineWriteService;

	public Long execute(PostCommand postCommand) {
		var postId = postWriteService.create(postCommand);
		var followMemberIds = followReadService.getFollowers(postCommand.memberId())
			.stream().map(Follow::getFromMemberId).toList();
		timelineWriteService.deliveryToTimeline(postId, followMemberIds);
		return postId;
	}
}
