package com.exam.board.controller;

import com.exam.board.model.entity.UserEntity;
import com.exam.board.model.post.Post;
import com.exam.board.model.reply.Reply;
import com.exam.board.model.user.*;
import com.exam.board.service.PostService;
import com.exam.board.service.ReplyService;
import com.exam.board.service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    ReplyService replyService;


    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String query
                                               //팔로우 상태를 받기 위해 유저 인증정보 추가
                                               ,
                                               Authentication authentication
    ){
        var users= userService.getUsers(query, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(
            @PathVariable String username,   Authentication authentication
    ){
        var user= userService.getUser(username,(UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<User> updateUser(
            @PathVariable String username, @Valid @RequestBody UserPatchRequestBody userPatchRequestBody, Authentication authentication
    ){
        var user= userService.updateUser(username, userPatchRequestBody, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }


    //url 상으로 /users/{username}/posts에 속하기 때문에 여기 컨트롤러에 작성
    @GetMapping("/{username}/posts")
    public ResponseEntity<List<Post>> getPostsByUsername(
            @PathVariable String username, Authentication authentication
    ){
        var posts= postService.getPostByUsername(username, (UserEntity) authentication.getPrincipal());

        return ResponseEntity.ok(posts);
    }



    @PostMapping
    public ResponseEntity<User> signUp(@Valid  @RequestBody UserSignUpRequestBody userSignUpRequestBody){
        var user = userService.signup(userSignUpRequestBody.username(), userSignUpRequestBody.password());

        return ResponseEntity.ok(user);
    }


    //인증하여 유저에게 토큰을 반환함
    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(
            @Valid  @RequestBody UserLoginRequestBody userLoginRequestBody
    ){
        //유저를 전달하는게 아니라, 유저를 기반으로 생성한 accessToken을 생성해 별도의 응답을 생성해 client에게 내려줘야함
        var response = userService.authenticate(userLoginRequestBody.username(), userLoginRequestBody.password());

        return ResponseEntity.ok(response);
    }


    //Follow는 Post API
    @PostMapping("/{username}/follows")
    public ResponseEntity<User> follow(
            //api를 호출하는 유저인 authentication -> username을 follow하겠다
            @PathVariable String username, Authentication authentication
    ){

        //상태가 변경된 유저를 Client에게 반환
        var user= userService.follow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    //Unfollow delete API, 어차피 호출하는 주체(언팔로우를 하는 주체는 본인이므로 id를 url로 안받아도 됨)
    @DeleteMapping("/{username}/follows")
    public ResponseEntity<User> unfollow(
            //api를 호출하는 유저인 authentication -> username을 unFollow하겠다
            @PathVariable String username, Authentication authentication
    ){

        //상태가 변경된 유저를 Client에게 반환
        var user= userService.unfollow(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(user);
    }

    //팔로잉 목록

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<Follower>> getFollowers(
            @PathVariable String username,
            //인증 정보는 딱히 필요없을듯
            //근데 상태값 전달을 위해 필요해짐
            Authentication authentication
    ){

        var followers= userService.getFollowersByUsername(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(followers);
    }


    @GetMapping("/{username}/followings")
    public ResponseEntity<List<User>> getFollowings(
            @PathVariable String username, Authentication authentication
    ){

        var followings= userService.getFollowingsByUsername(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(followings);
    }

    //댓글만 모아볼거
    @GetMapping("/{username}/replies")
    public ResponseEntity<List<Reply>> getRepliesByUser(
            @PathVariable String username
    ){
        var replies= replyService.getRepliesByUser(username);

        return ResponseEntity.ok(replies);
    }


    @GetMapping("/{username}/liked-users")
    public ResponseEntity<List<LikedUser>> getLikedUsersByUser(
            @PathVariable String username, Authentication authentication
    ){

        var likedUsers= userService.getLikedUsersByUser(username, (UserEntity) authentication.getPrincipal());
        return ResponseEntity.ok(likedUsers);
    }
}
