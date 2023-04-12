package com.example.fastcampusmysql.domain.member.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.fastcampusmysql.util.MemberFixtureFactory;

class MemberTest {

	@DisplayName("회원은 닉네임을 변경할 수 있다.")
	@Test
	public void testChangeName() {
		// LongStream.range(0, 10).mapToObj(MemberFixtureFactory::create).forEach(member -> {
		// 	System.out.println(member.getNickname());
		// });
		var member = MemberFixtureFactory.create();
		var expected = "pnu";
		member.changeNickname(expected);

		assertThat(member.getNickname()).isEqualTo(expected);
	}

	@DisplayName("회원의 닉네임은 10자를 초과할 수 없다.")
	@Test
	public void testNicknameMaxLength() {
		var member = MemberFixtureFactory.create();
		var overMaxLength = "pnupnupnupnu";
		assertThrows(IllegalArgumentException.class, () -> member.changeNickname(overMaxLength));
	}
}
