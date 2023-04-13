package com.example.fastcampusmysql.domain.post.repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PostRepository {
	static final String TABLE = "Post";
	private static final RowMapper<Post> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Post.builder()
		.id(resultSet.getLong("id"))
		.memberId(resultSet.getLong("memberId"))
		.contents(resultSet.getString("contents"))
		.createdDate(resultSet.getObject("createdDate", LocalDate.class))
		.createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
		.build();
	final static private RowMapper<DailyPostCount> DAILY_POST_COUNT_ROW_MAPPER = (ResultSet resultSet, int rowNUm)
		-> new DailyPostCount(
		resultSet.getLong("memberId"),
		resultSet.getObject("createdDate", LocalDate.class),
		resultSet.getLong("cnt")
	);
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request) {
		String sql = String.format("""
			SELECT memberId, createdDate, count(id) as cnt
			FROM %s
			WHERE memberId = :memberId and createdDate between :firstDate and :lastDate
			GROUP BY memberId, createdDate
			""", TABLE);
		var params = new BeanPropertySqlParameterSource(request);
		return namedParameterJdbcTemplate.query(sql, params, DAILY_POST_COUNT_ROW_MAPPER);
	}

	public Post save(Post post) {
		if (post.getId() == null) {
			return insert(post);
		}
		throw new UnsupportedOperationException("Post는 갱신을 지원하지 않습니다.");
	}

	private Post insert(Post post) {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(
			namedParameterJdbcTemplate.getJdbcTemplate()).withTableName(TABLE).usingGeneratedKeyColumns("id");
		BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(post);
		long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
		return Post.builder()
			.id(id)
			.memberId(post.getMemberId())
			.contents(post.getContents())
			.createdDate(post.getCreatedDate())
			.createdAt(post.getCreatedAt())
			.build();
	}
}
