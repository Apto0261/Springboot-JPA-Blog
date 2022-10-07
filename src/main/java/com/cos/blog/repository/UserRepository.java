package com.cos.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blog.model.User;

// DAO
// 자동으로 bean 등록이 된다. 어노테이션 생략가능. @Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findByUsername(String username);
}
//JPA Naming 쿼리
//SELECT * FR0M user WHERE username = ? AND password = ?; 대박인데?
//User findByUsernameAndPassword(String username, String password);
	
// 위에랑 같은 기능
//	@Query(value="SELECT * FR0M user WHERE username = ? AND password = ?", nativeQuery = true) 
//	User login(String username, String password);