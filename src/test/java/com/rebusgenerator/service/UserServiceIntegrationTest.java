package com.rebusgenerator.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.rebusgenerator.entity.RebusUser;
import com.rebusgenerator.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceIntegrationTest {
	
	@Autowired
    private UserService userService;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
    private AuthenticationManager authenticationManager;
	
	@Before
    public void setUp() {
		RebusUser user = new RebusUser("me", "123me", "USER");
		RebusUser admin = new RebusUser("admin", "admin", "ADMIN");

        //
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.findByUsername(admin.getUsername())).thenReturn(admin);
    }
	
	@Test
    public void whenValidUserLogin_thenMustReturnTrue() throws Exception {
		assertTrue(userService.login("me", "123me"));
		assertTrue(userService.login("admin", "admin"));
    }
	
	@Test
    public void whenNotValidUserLogin_thenMustReturnFalse() throws Exception {
		assertFalse(userService.login("who", "123"));
    }
}
