package com.example.restapihomework.service;

import com.example.restapihomework.domain.dto.PostDTO;
import com.example.restapihomework.domain.entity.Post;
import com.example.restapihomework.global.PostNotFoundException;
import com.example.restapihomework.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;


    @Transactional
    public void registPost(PostDTO newPost) {
        Post post = Post.builder()
                .postId(newPost.getPostId())
                .title(newPost.getTitle())
                .content(newPost.getContent())
                .build();

        postRepository.save(post);
    }

    public List<Post> findAllPosts() { return postRepository.findAll(); }

    public PostDTO getPostByPostId(long postId) throws PostNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("ID 값이 없습니다." + postId));

        return PostDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    @Transactional
    public void updatePost(long postId, String title, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 게시글을 찾을 수 없습니다."));

        post.setTitle(title);
        post.setContent(content);

        postRepository.save(post);
    }


    public boolean deletePost(long postId) {
        try {
            if (postRepository.existsById(postId)) {
                postRepository.deleteById(postId);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
