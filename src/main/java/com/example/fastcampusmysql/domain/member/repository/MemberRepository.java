package com.example.fastcampusmysql.domain.member.repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

	private static final String TABLE = "Member";
	private static final RowMapper<Member> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Member.builder()
		.id(resultSet.getLong("id"))
		.nickname(resultSet.getString("nickname"))
		.email(resultSet.getString("email"))
		.birthday(resultSet.getObject("birthday", LocalDate.class))
		.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
		.build();
	final private NamedParameterJdbcTemplate parameterJdbcTemplate;

	public Optional<Member> findById(Long id) {
		var sql = String.format("SELECT * FROM %s WHERE id = :id ", TABLE);
		var params = new MapSqlParameterSource()
			.addValue("id", id);
		List<Member> members = parameterJdbcTemplate.query(sql, params, ROW_MAPPER);

		// jdbcTemplate.query의 결과 사이즈가 0이면 null, 2 이상이면 예외
		Member nullableMember = DataAccessUtils.singleResult(members);
		return Optional.ofNullable(nullableMember);
	}

	public List<Member> findAllByIdIn(List<Long> ids) {
		// 빈 값처리를 안해주면 sql 문법 오류 발생
		if (ids.isEmpty()) {
			return List.of();
		}
		var sql = String.format("SELECT * FROM %s WHERE id in (:ids)", TABLE);
		var params = new MapSqlParameterSource().addValue("ids", ids);
		return parameterJdbcTemplate.query(sql, params, ROW_MAPPER);
	}

	public Member save(Member member) {
		/*
		member id를 보고 갱신 또는 삽입을 결정
		반환값은 id를 담아서 반환한다.
		 */
		if (member.getId() == null) {
			return insert(member);
		}
		return update(member);
	}

	private Member insert(Member member) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(parameterJdbcTemplate.getJdbcTemplate()).withTableName(
			TABLE).usingGeneratedKeyColumns("id");
		SqlParameterSource params = new BeanPropertySqlParameterSource(member);
		var id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
		return Member.builder()
			.id(id)
			.email(member.getEmail())
			.nickname(member.getNickname())
			.email(member.getEmail())
			.birthday(member.getBirthday())
			.createdAt(member.getCreatedAt())
			.build();
	}

	private Member update(Member member) {
		var sql = String.format(
			"UPDATE %s set email = :email, nickname = :nickname, birthday = :birthday WHERE id = :id", TABLE);
		SqlParameterSource params = new BeanPropertySqlParameterSource(member);
		parameterJdbcTemplate.update(sql, params);
		return member;
	}

}
