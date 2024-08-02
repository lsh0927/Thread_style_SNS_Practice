package com.exam.board.repository;

import com.exam.board.model.entity.LikeEntity;
import com.exam.board.model.entity.PostEntity;
import com.exam.board.model.entity.ReplyEntity;
import com.exam.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity,Long> {


    List<LikeEntity> findByUser(UserEntity user);


    List<LikeEntity> findByPost(PostEntity post);

    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);
}
