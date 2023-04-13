package com.example.fastcampusmysql.domain.post.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.PostFixtureFactory;

@SpringBootTest
public class PostBulkInsertTest {
	@Autowired
	private PostRepository postRepository;

	@Test
	public void bulkInsert() {
		var easyRandom = PostFixtureFactory.get(
			3L,
			LocalDate.of(1970, 1, 1),
			LocalDate.of(2022, 2, 1));
		var stopWatch = new StopWatch();
		stopWatch.start();
		List<Post> posts = IntStream.range(0, 10000 * 100)
			.parallel()
			.mapToObj(i -> easyRandom.nextObject(Post.class))
			.toList();
		stopWatch.stop();
		System.out.println("객체 생성 시간" + stopWatch.getTotalTimeSeconds());

		var queryStopWatch = new StopWatch();
		queryStopWatch.start();
		postRepository.builkInsert(posts);
		queryStopWatch.stop();

		System.out.println("DB Insert 시간" + queryStopWatch.getTotalTimeSeconds());
	}
}
