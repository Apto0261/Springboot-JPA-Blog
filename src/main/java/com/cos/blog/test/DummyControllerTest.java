package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

//html 파일이 아니라 data를 리턴해줌
@RestController
public class DummyControllerTest {
	
	@Autowired //의존성 주입
	private UserRepository userRepository;
	
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		
		try {
			userRepository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			return "삭제 실패";
		}
		
		return "삭제되었습니다 : " + id ;
	}
	
	//save 함수는 id를 전달하지 않으면 -> insert를 해주고
	//save 함수는 id를 전달하면 해당 id 에 대한 데이터가있으면 update를 해주고
	//save 함수는 id를 전달하면 해당 id에 대한 데이터가 없으면 insert를 해요
	//email,passowrd 수정
	
	@Transactional //함수 종료시에 자동 commit이 됨.
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) { //json 데이터를 요청 => Java Object 로 변환해서 받아줌.
		System.out.println("id : " + id);
		System.out.println("passowrd : " + requestUser.getPassword());
		System.out.println("email : " +  requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패 하였습니다.");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
//		userRepository.save(user); 
		
		//더티체킹 :  트랜잭션널 어노테이션을 걸면 save하지않아도 업데이트가 된다.
		
		return user;
	}
	
	//http://localhost:8000/blog/dummy/user
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	//한페이지당 2건 데이터를 리턴받아 볼 예정
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=1,sort="id", direction = Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingUser = userRepository.findAll(pageable);
		
		/* 분기처리
		 * if(pagingUser.isLast()) {
		 * 
		 * }
		
		 */
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	
	//{id}주소로 파라메터 전달 
	//http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		
		//user/4 찾으면 내가 데이터베이스에서 못찾아오게되면 user가 null 이 되서 return에 문제가생김 
		// 그래서 Optional로 너의 User 객체를 감싸서 가져올테니 null인지 아닌지 판단해서 return 해 //.get 은 null상관없이 다가져오게 //  .orElseGet 은 null이면 비어있는 새 객체를 만들어줘
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 없습니다. id : " +id);
			}
		});
		//람다식 표현 타입같은걸 몰라도 됨 편함. ( Supplier 어쩌고)
		//User user = userRepository.findById(id).orElseThrow(()->{
		//	return new IllegalArgumentException("해당 사용자는 없습니다.");
		//});
		
		// 요청 : 웹브라우저
		//user 객체 = 자바 오브젝트  따라서 웹브라우저는 이해못함
		// 변환 오브젝트 -> json
		// 스프링부트 = MessageConverter 라는 애가 응답시에 자동 작동
		// 만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
		// user 오브젝트를 json으로 변환해서 브라우저에 던짐
		return user;
	}
	
	//http://localhost:8000/blog/dummy/join
	//http의 body 에 username, password, email 데이터를 가지고 요청
	@PostMapping("/dummy/join")
	public String join(User user) {
		System.out.println("id : " + user.getId());
		System.out.println("username : " + user.getUsername());
		System.out.println("role : " + user.getRole());
		System.out.println("password : " + user.getPassword());
		System.out.println("email : " + user.getEmail());
		System.out.println("createDate : " + user.getCreateDate());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}

}
