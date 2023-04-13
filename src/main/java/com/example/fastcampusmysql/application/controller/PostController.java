package com.example.fastcampusmysql.application.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fastcampusmysql.application.usecase.CreatePostUsecase;
import com.example.fastcampusmysql.application.usecase.GetTImelinePostsUsecase;
import com.example.fastcampusmysql.application.util.CursorRequest;
import com.example.fastcampusmysql.application.util.PageCursor;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostController {
	final private PostWriteService postWriteService;
	final private PostReadService postReadService;
	final private GetTImelinePostsUsecase getTImelinePostsUsecase;
	final private CreatePostUsecase createPostUsecase;

	@PostMapping()
	public Long create(PostCommand command) {
		return createPostUsecase.execute(command);
	}

	@PostMapping("/daily-post-counts")
	public List<DailyPostCount> getDailyPostCounts(@RequestBody DailyPostCountRequest request) {
		System.out.println(request);
		return postReadService.getDailyPostCount(request);
	}

	@GetMapping("/members/{memberId}")
	public Page<Post> getPosts(
		@PathVariable Long memberId,
		Pageable pageable) {
		return postReadService.getPosts(memberId, pageable);
	}

	@GetMapping("/members/{memberId}/by-cursor")
	public PageCursor<Post> getPostsByCursor(
		@PathVariable Long memberId,
		CursorRequest cursorRequest) {
		return postReadService.getPosts(memberId, cursorRequest);
	}

	@GetMapping("/members/{memberId}/timeline")
	public PageCursor<Post> getTimeline(@PathVariable Long memberId,
		CursorRequest cursorRequest) {
		return getTImelinePostsUsecase.executeByTimeline(memberId, cursorRequest);
	}

}
