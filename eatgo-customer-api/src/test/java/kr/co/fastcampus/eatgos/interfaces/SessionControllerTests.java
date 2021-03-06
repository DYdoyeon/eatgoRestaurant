package kr.co.fastcampus.eatgos.interfaces;



import kr.co.fastcampus.eatgos.application.EmailNotExistedException;
import kr.co.fastcampus.eatgos.application.PasswordWrongException;
import kr.co.fastcampus.eatgos.application.UserService;


import kr.co.fastcampus.eatgos.domain.User;
import kr.co.fastcampus.eatgos.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
public class SessionControllerTests {
    @Autowired
    MockMvc mvc;

    @MockBean
    private JwtUtil jwtUtil;


    @MockBean
    private UserService userService;

    @Test
    public void createWithValidAttributes() throws Exception {
        String email = "tester@example.com";
        String password = "test";
        Long id = 1004L;
        String name = "tester";

        User mockUser = User.builder().name(name).id(id).build()  ;
        given(userService.authenticate(email,password)).willReturn(mockUser);

        given(jwtUtil.createToken(id,name)).willReturn("header.payload.signature");

        mvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"password\":\"test\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("location","/session"))
                 .andExpect(content().string(containsString("{\"accessToken\":\"header.payload.signature\"}")));


              verify(userService).authenticate(eq(email),eq(password));



    }
    @Test
    public void createWithNotExistedEmail() throws Exception {

        given(userService.authenticate("x@example.com","test"))
                .willThrow(EmailNotExistedException.class);

        mvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"x@example.com\",\"password\":\"test\"}"))
                .andExpect(status().isBadRequest());

        verify(userService).authenticate(eq("x@example.com"),eq("test"));

    }

    @Test
    public void createWithWongPassword() throws Exception {

        given(userService.authenticate("tester@example.com","x"))
                .willThrow(PasswordWrongException.class);

        mvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"password\":\"x\"}"))
                .andExpect(status().isBadRequest());


        verify(userService).authenticate(eq("tester@example.com"),eq("x"));

    }

}