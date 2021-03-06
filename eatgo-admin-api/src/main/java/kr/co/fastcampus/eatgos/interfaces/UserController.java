package kr.co.fastcampus.eatgos.interfaces;


import kr.co.fastcampus.eatgos.application.UserService;
import kr.co.fastcampus.eatgos.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    //1. user list
    //2. user create -> 회원가입
    //3. user update
    //4. user delete -> level:0i
    @GetMapping("/users")
    public List<User> list(){
        List<User> users = userService.getUsers();
        return users ;
    }


    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody User resource) throws URISyntaxException {
        String email = resource.getEmail();
        String name = resource.getName();
        User user= userService.addUser(email,name);
        String url = "/users/"+user.getId();

        return ResponseEntity.created(new URI(url)).body("{}");

    }
    @PatchMapping("/users/{id}")
    public String update(@PathVariable("id") Long id, @RequestBody User resource){

        String email = resource.getEmail();
        String name = resource.getName();
        Long level = resource.getLevel();

        userService.updateUser(id,email,name,level);

        return "{}";
    }

    @DeleteMapping("/users/{id}")
    public String delete(@PathVariable("id") Long id){
        userService.deactiveUser(id);
        return "{}";
    }
}

