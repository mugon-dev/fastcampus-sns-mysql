package com.example.fastcampusmysql.domain.follow.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FollowWriteService {
	final private FollowRepository followRepository;

	public void create(MemberDto fromMemberId, MemberDto toMemberId) {
		Assert.isTrue(!fromMemberId.id().equals(toMemberId.id()), "From, To 회원이 동일합니다.");
		var follow = Follow.builder().fromMemberId(fromMemberId.id()).toMemberId(toMemberId.id()).build();
		followRepository.save(follow);

	}
}
