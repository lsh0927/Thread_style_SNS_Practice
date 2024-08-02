package com.exam.board.repository;

import com.exam.board.model.entity.PostEntity;
import com.exam.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity,Long> {


    //유저 정보를 가지고 검색 findByUsername으로 못하는 이유: PostEntity에는 해당 필드가 없음(대신 user라는 필드로 정의)
    List<PostEntity> findByUser(UserEntity userEntity);
}
