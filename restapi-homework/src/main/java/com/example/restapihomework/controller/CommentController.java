package com.example.restapihomework.controller;

import com.example.restapihomework.common.ResponseMessage;
import com.example.restapihomework.domain.dto.CommentDTO;
import com.example.restapihomework.domain.entity.Comment;
import com.example.restapihomework.global.CommentNotFoundException;
import com.example.restapihomework.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Tag(name = "Spring Boot Swagger 연동 API (Comments)")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "새로운 댓글 작성")
    @PostMapping("/comments")
    public ResponseEntity<ResponseMessage> createNewComment(@RequestBody CommentDTO newComment) {

        commentService.registComment(newComment);

        String successMessage = "댓글 등록에 성공하였습니다.";

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", successMessage);

        return ResponseEntity
                .ok()
                .body(new ResponseMessage(201, "댓글 추가 성공", responseMap));
    }

    @Operation(summary = "댓글 전체 조회", description = "사이트의 댓글 전체 조회")
    @GetMapping("/comments")
    public ResponseEntity<ResponseMessage> findAllComments() {

        List<Comment> comments = commentService.findAllComments();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("comments", comments);

        return ResponseEntity
                .ok()
                .body(new ResponseMessage(200, "조회성공", responseMap));
    }

    @Operation(summary = "댓글 번호로 특정 댓글 조회",
               description = "댓글 번호를 통해 특정 댓글을 조회한다.",
               parameters = {
                    @Parameter(name = "commentId", description = "사용자 화면에서 넘어오는 comment의 pk")
               })
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<ResponseMessage> findCommentByCommentId(@PathVariable long commentId) throws CommentNotFoundException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                new MediaType(
                        "application",
                        "json",
                        Charset.forName("UTF-8")
                )
        );


        CommentDTO foundComment = commentService.getCommentByCommentId(commentId);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("comment", foundComment);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new ResponseMessage(200, "단일조회 성공", responseMap));
    }

    @Operation(summary = "댓글 수정", description = "특정 댓글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "203", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못 입력된 파라미터")
    })
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> editComment(@PathVariable long commentId, @RequestBody CommentDTO modifiedComment) throws CommentNotFoundException {
        commentService.updateComment(commentId, modifiedComment.getComment());

        Map<String, Object> responseMap = new HashMap<>();
        String msg = "댓글 수정에 성공하였습니다.";
        responseMap.put("result", msg);

        return ResponseEntity
                .ok()
                .body(new ResponseMessage(203, "댓글 수정 성공", responseMap));
    }



    @Operation(summary = "댓글 삭제", description = "특정 댓글 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못 입력된 파라미터")
    })
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deleteComment(@PathVariable long commentId) throws CommentNotFoundException {

        Map<String, Object> responseMap = new HashMap<>();

        boolean isDeleted = commentService.deleteComment(commentId);
        if (isDeleted) {
            String msg = "댓글 삭제에 성공하였습니다.";
            responseMap.put("result", msg);
        } else {
            throw new CommentNotFoundException("댓글 삭제에 실패하였습니다.");
        }

        return ResponseEntity
                .ok()
                .body(new ResponseMessage(204, "댓글 삭제 성공", responseMap));
    }

}
