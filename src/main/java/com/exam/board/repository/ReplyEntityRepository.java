package com.exam.board.repository;

import com.exam.board.model.entity.PostEntity;
import com.exam.board.model.entity.ReplyEntity;
import com.exam.board.model.entity.UserEntity;
import com.exam.board.model.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyEntityRepository extends JpaRepository<ReplyEntity,Long> {


    List<ReplyEntity> findByUser(UserEntity user);


    List<ReplyEntity> findByPost(PostEntity post);
}
