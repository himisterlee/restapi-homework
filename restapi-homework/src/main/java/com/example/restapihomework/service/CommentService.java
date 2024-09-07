package com.example.restapihomework.service;

import com.example.restapihomework.domain.dto.CommentDTO;
import com.example.restapihomework.domain.entity.Comment;
import com.example.restapihomework.domain.entity.Post;
import com.example.restapihomework.global.CommentNotFoundException;
import com.example.restapihomework.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public void registComment(CommentDTO newComment) {
        Comment comment = Comment.builder()
                .commentId(newComment.getCommentId())
                .comment(newComment.getComment())
                .build();

        commentRepository.save(comment);
    }

    public List<Comment> findAllComments() { return commentRepository.findAll(); }


    public CommentDTO getCommentByCommentId(long commentId) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("ID 값이 없습니다." + commentId));

        return CommentDTO.builder()
                .commentId(comment.getCommentId())
                .comment(comment.getComment())
                .build();
    }

    @Transactional
    public void updateComment(long commentId, String comment) {
        Comment comment1 = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 댓글을 찾을 수 없습니다."));

        comment1.setComment(comment);

        commentRepository.save(comment1);
    }



    public boolean deleteComment(long commentId) {
        try {
            if (commentRepository.existsById(commentId)) {
                commentRepository.deleteById(commentId);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
