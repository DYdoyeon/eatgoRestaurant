package kr.co.fastcampus.eatgos.interfaces;


import kr.co.fastcampus.eatgos.application.UserService;
import kr.co.fastcampus.eatgos.domain.User;
import kr.co.fastcampus.eatgos.util.JwtUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;



@RestController
public class SessionController {


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;
  //  private Logger logger;


    @PostMapping("/session")
    public ResponseEntity<SessionResponseDto> create(
            @RequestBody SessionRequestDto resource) throws URISyntaxException {

        String url="/session";

        String email=resource.getEmail();
        String password = resource.getPassword();

        System.out.println("email :email :" +email);
        User user = userService.authenticate(email,password);
        //String accessToken = user.getAccessToken();
        String accessToken = jwtUtil.createToken(1003L,"John");

        System.out.println("accessToken : "  + accessToken);

          return ResponseEntity.created(new URI(url))
                .body(SessionResponseDto.builder()
                        .accessToken(accessToken)
                        .build());
    }
}
