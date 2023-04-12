package com.example.fastcampusmysql.domain.member.service;

import org.springframework.stereotype.Service;

import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberWriteService {

	final private MemberRepository memberRepository;

	public Member register(RegisterMemberCommand command) {
		/*
			묙표 - 회원정보(이메일, 닉네임, 생년월일)을 등록한다.
				- 닉네임은 10자를 넘길 수 없다.
			파라미터 - memberRegisterCommand
			val member = Member.of(memberRegisterCommand)
			memberRepository.save(member)
		 */
		Member member = Member.builder()
			.nickname(command.nickname())
			.birthday(command.birthday())
			.email(command.email())
			.build();
		return memberRepository.save(member);
	}
}
