package com.ohigraffers.practice.post.controller;

import com.ohigraffers.practice.post.dto.request.PostCreateRequest;
import com.ohigraffers.practice.post.dto.request.PostUpdateRequest;
import com.ohigraffers.practice.post.dto.response.PostResponse;
import com.ohigraffers.practice.post.dto.response.ResponseMessage;
import com.ohigraffers.practice.post.model.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/* Swagger 문서화 시 Grouping 작성 */
@Tag(name = "post 관련 api")
@RestController
@RequestMapping("/posts")
public class PostController {

    private List<Post> posts;

    public PostController(){
        posts = new ArrayList<>();
        posts.add(new Post(1L, "제목1", "내용1", "홍길동"));
        posts.add(new Post(2L, "제목2", "내용2", "유관순"));
        posts.add(new Post(3L, "제목3", "내용3", "신사임당"));
        posts.add(new Post(4L, "제목4", "내용4", "이순신"));
        posts.add(new Post(5L, "제목5", "내용5", "장보고"));
    }

    /* 1. 전체 포스트 조회 */
    /* Swagger 문서화 시 설명 어노테이션 작성 */
    /* RequestMapping 어노테이션 작성 */
    @Operation(summary = "전체 게시글 조회", description = "전체 게시글 목록을 조회한다.")
    @GetMapping
    public ResponseEntity<ResponseMessage> findAllPosts() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", StandardCharsets.UTF_8));

        /* Post 타입은 PostResponse 타입으로 변환해서 반환 */
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::from)
                .toList();

        /* hateoas 적용 */
        List<EntityModel<PostResponse>> postsWithRel = postResponses.stream().map(
                postResponse -> EntityModel.of(
                        postResponse,
                        linkTo(methodOn(PostController.class).findPostByCode(postResponse.getCode())).withSelfRel(),
                        linkTo(methodOn(PostController.class).findAllPosts()).withRel("posts")
                )
        ).toList();

        /* 응답 데이터 설정 */
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("posts", postsWithRel);
        ResponseMessage responseMessage = new ResponseMessage(200, "조회성공", responseMap);

        /* ResponseEntity 반환 */
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    /* 2. 특정 코드로 포스트 조회 */
    /* Swagger 문서화 시 설명 어노테이션 작성 */
    /* RequestMapping 어노테이션 작성 */
    @Operation(summary = "게시글 번호로 게시글 조회", description = "게시글 번호를 통해 해당하는 게시글 정보를 조회한다.")
    @GetMapping("/{postCode}")
    public ResponseEntity<ResponseMessage> findPostByCode(@PathVariable Long code) {

        /* 응답 데이터 설정 */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        /* Post 타입은 PostResponse 타입으로 변환해서 반환 */
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::from)
                .toList();

        /* hateoas 적용 */
        PostResponse foundPost = postResponses.stream().filter(
                postResponse -> postResponse.getCode() == code).toList().get(0);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("posts", foundPost);
        ResponseMessage responseMessage = new ResponseMessage(200, "조회성공", responseMap);

        /* ResponseEntity 반환 */

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(responseMessage);
    }

    /* 3. 신규 포스트 등록 */
    /* Swagger 문서화 시 설명 어노테이션 작성 */
    /* RequestMapping 어노테이션 작성 */

    /* @RequestBody 사용자가 어떤 내용을 입력하려고 본문의 내용을 보냄
    *  body 부분만 요청을 함
    * @RequestParam은 그냥 사용자가 입력한 값 받아 먹겠다는 뜻
    * 어떤 값을 던지는데  */
    @Operation(summary = "게시글 정보 등록")
    @PostMapping("/regist")
   public ResponseEntity<Void> registPost(@RequestBody PostCreateRequest post) {

       /* 리스트에 추가 */
        Long lastCode = posts.get(posts.size() - 1).getCode();

        /* 엔티티로 받아서 DTO로 변환을 해줘야 함 */
        posts.add(new Post(lastCode + 1, post.getTitle(), post.getContent(), post.getWriter()));

       /* ResponseEntity 반환 */
        return ResponseEntity
                .created(URI.create("/posts/regist" + posts.get(posts.size() - 1).getCode()))
                .build();

   }

   /* 4. 포스트 제목과 내용 수정 */
   /* Swagger 문서화 시 설명 어노테이션 작성 */
   /* RequestMapping 어노테이션 작성 */
   @Operation(summary = "게시글 정보 수정")
   @PutMapping("/{postCode}")
    public ResponseEntity<Void> modifyPost(@PathVariable Long postCode,
                                           @RequestBody PostUpdateRequest postInfo) {

        /* 리스트에서 찾아서 수정 */

        Post foundPost = posts.stream()
                .filter(post -> post.getCode() == postCode).toList().get(0);

        /* 수정 메소드 활용 */
        foundPost.modifyTitleAndContent(postInfo.getTitle(),
                                        postInfo.getContent());

        /* ResponseEntity 반환 */
        return ResponseEntity.created(URI.create("posts/regist" + foundPost)).build();
    }

    /* 5. 포스트 삭제 */
    /* Swagger 문서화 시 설명 어노테이션 작성 */
    /* RequestMapping 어노테이션 작성 */
    @Operation(summary = "게시글 정보 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "게시글 정보 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못 입력된 파라미터")
    })
    @DeleteMapping("/{postCode}")
    public ResponseEntity<Void> removeUser(@PathVariable Long postCode) {

        /* 리스트에서 찾아서 삭제 */

        Post foundPost = posts.stream().filter(post -> post.getCode() == postCode).toList().get(0);

        posts.remove(foundPost);

        /* ResponseEntity 반환 */
        return ResponseEntity
                /* 값이 없어서 반환할게 없다. */
                .noContent()
                .build();
    }

}
