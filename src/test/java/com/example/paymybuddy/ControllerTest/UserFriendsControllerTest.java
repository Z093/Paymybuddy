/*
package com.example.paymybuddy.ControllerTest;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.paymybuddy.controller.UserFriendsController;
import com.example.paymybuddy.service.UserFriendsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

@WebMvcTest(UserFriendsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserFriendsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserFriendsService userFriendsService; // Utilisez @MockBean au lieu de @Mock

    @Test
    public void testAddFriend() throws Exception {
        String userMail = "user@example.com";
        String friendMail = "friend@example.com";

        doNothing().when(userFriendsService).addFriend(userMail, friendMail);

        mockMvc.perform(post("/api/v1/" + userMail + "/friends")

                        .param("friendMail", friendMail)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userFriendsService, times(1)).addFriend(userMail, friendMail);
    }
}

*/
