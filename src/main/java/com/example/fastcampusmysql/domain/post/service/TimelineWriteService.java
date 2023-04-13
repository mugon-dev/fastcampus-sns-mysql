package com.example.fastcampusmysql.domain.post.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.domain.post.repository.TimelineRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TimelineWriteService {
	final private TimelineRepository timelineRepository;

	private static Timeline toTimeline(Long postId, Long memberId) {
		return Timeline.builder().memberId(memberId).postId(postId).build();
	}

	public void deliveryToTimeline(Long postId, List<Long> toMemberIds) {
		var timelines = toMemberIds.stream()
			.map(memberId -> toTimeline(postId, memberId)).toList();
		timelineRepository.bulkInsert(timelines);
	}
}
