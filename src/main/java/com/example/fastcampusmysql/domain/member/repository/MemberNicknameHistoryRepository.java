package com.example.fastcampusmysql.domain.member.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MemberNicknameHistoryRepository {

	public static final RowMapper<MemberNicknameHistory> ROW_MAPPER = (resultSet, rowNum) ->
		MemberNicknameHistory.builder()
			.id(resultSet.getLong("id"))
			.memberId(resultSet.getLong("memberId"))
			.nickname(resultSet.getString("nickname"))
			.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
			.build();
	private static final String TABLE = "MemberNicknameHistory";
	final private NamedParameterJdbcTemplate parameterJdbcTemplate;

	public Optional<MemberNicknameHistory> findById(Long id) {
		var sql = String.format("SELECT * FROM %S WHERE id = :id", TABLE);
		var param = new MapSqlParameterSource().addValue("id", id);
		var member = parameterJdbcTemplate.queryForObject(sql, param, ROW_MAPPER);
		return Optional.ofNullable(member);
	}

	public List<MemberNicknameHistory> findAllByMemberId(Long memberId) {
		var sql = String.format("SELECT * from %s WHERE memberId = :memberId", TABLE);
		var params = new MapSqlParameterSource().addValue("memberId", memberId);
		return parameterJdbcTemplate.query(sql, params, ROW_MAPPER);
	}

	public MemberNicknameHistory save(MemberNicknameHistory memberNicknameHistory) {
		/*
		memberNicknameHistory id를 보고 갱신 또는 삽입을 결정
		반환값은 id를 담아서 반환한다.
		 */
		if (memberNicknameHistory.getId() == null) {
			return insert(memberNicknameHistory);
		}
		throw new UnsupportedOperationException("MemberNicknameHistory는 갱신을 지원하지 않습니다.");
	}

	private MemberNicknameHistory insert(MemberNicknameHistory memberNicknameHistory) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(parameterJdbcTemplate.getJdbcTemplate()).withTableName(
			TABLE).usingGeneratedKeyColumns("id");
		SqlParameterSource params = new BeanPropertySqlParameterSource(memberNicknameHistory);
		var id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
		return MemberNicknameHistory.builder()
			.id(id)
			.memberId(memberNicknameHistory.getMemberId())
			.nickname(memberNicknameHistory.getNickname())
			.createdAt(memberNicknameHistory.getCreatedAt())
			.build();
	}

}
