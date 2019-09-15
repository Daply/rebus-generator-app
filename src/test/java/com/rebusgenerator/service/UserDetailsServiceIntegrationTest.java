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

import com.rebusgenerator.entity.User;
import com.rebusgenerator.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDetailsServiceIntegrationTest {
	
	@Autowired
    private UserDetailsServiceImpl userDetailsService;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
    private AuthenticationManager authenticationManager;
	
	@Before
    public void setUp() {
		User user = new User("me", "123me", "USER");
		User admin = new User("admin", "admin", "ADMIN");

        //
		Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		Mockito.when(userRepository.findByUsername(admin.getUsername())).thenReturn(admin);
    }
	
	@Test
    public void whenValidUserFindByUsername_thenMustReturnUserDetails() throws Exception {
		assertTrue(userDetailsService.loadUserByUsername("me").getUsername().equals("me"));
		assertTrue(userDetailsService.loadUserByUsername("admin").getUsername().equals("admin"));
    }
	
	@Test
    public void whenNotValidUserFindByUsername_thenMustReturnNull() throws Exception {
		assertTrue(userDetailsService.loadUserByUsername("who") == null);
    }
}
