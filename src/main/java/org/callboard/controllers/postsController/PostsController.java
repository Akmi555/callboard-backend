package org.callboard.controllers.postsController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.callboard.dto.StandardIntRequest;
import org.callboard.dto.StandardResponse;
import org.callboard.dto.StandardStringRequest;
import org.callboard.dto.postDto.*;
import org.callboard.services.postsServices.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {

    private final CreatePostService createPostService;
    private final UpdatePostService updatePostService;
    private final FindAllPostsService findAllPostsService;
    private final FindPostsBySubjectService findPostsBySubjectService;
    private final FindUsersPostsByEmailService findUsersPostsByEmailService;
    private final DeletePostByIdService deletePostByIdService;
    private final DeletePostsByUserId deletePostsByUserIdService;

    @RolesAllowed({"USER", "ADMIN"})
    @PostMapping(consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<PostCreateSuccessResponse> createNewPost(
           @Valid @ModelAttribute @RequestBody NewPostRequest request) throws IOException {
        return new ResponseEntity<>(createPostService.execute(request), HttpStatus.CREATED);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @PutMapping("/update")
    public ResponseEntity<PostResponse> updatePost(@Valid @RequestBody UpdatePostRequest request) {
        return new ResponseEntity<>(updatePostService.execute(request), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PostListResponse> getAllPosts() {
        return new ResponseEntity<>(findAllPostsService.findAllPosts(), HttpStatus.OK);
    }

    @GetMapping("/{subject}")
    public ResponseEntity<PostListResponse> getPostsBySubject(@PathVariable String subject) throws Exception {
        return new ResponseEntity<>(findPostsBySubjectService.execute(new StandardStringRequest(subject)), HttpStatus.OK);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/user")
    public ResponseEntity<PostListResponse> getPostsByUser(Principal principal) throws Exception {

        StandardStringRequest email = new StandardStringRequest(principal.getName());
        return new ResponseEntity<>(findUsersPostsByEmailService.execute(email), HttpStatus.OK);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deletePostById(@PathVariable Integer id) throws Exception {
        StandardIntRequest request = new StandardIntRequest(id);
        StandardResponse response = deletePostByIdService.execute(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RolesAllowed("ADMIN")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<StandardResponse> deletePostByUserId(@PathVariable Integer id) throws Exception {
        StandardIntRequest request = new StandardIntRequest(id);
        StandardResponse response = deletePostsByUserIdService.execute(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
