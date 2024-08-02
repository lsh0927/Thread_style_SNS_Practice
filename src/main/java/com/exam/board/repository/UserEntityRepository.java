package com.exam.board.repository;

import com.exam.board.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {

    //userId가 아닌, username으로 검색할 필요가 UserDetailsService의 loadUserByUsername 때문에 생김
    Optional<UserEntity> findByUsername(String username);


    //필드+Containing: 부분적 포함 데이터 조회
    List<UserEntity> findByUsernameContaining(String username);
}
