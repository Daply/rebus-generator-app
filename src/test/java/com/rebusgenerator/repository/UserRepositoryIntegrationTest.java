package com.rebusgenerator.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rebusgenerator.entity.Language;
import com.rebusgenerator.entity.RebusUser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryIntegrationTest {
	
	@Autowired
    private UserRepository userRepository;
	
	@Test
    public void whenFindLanguage_thenReturnLanguage() {
		RebusUser expected = new RebusUser("me", "123", "USER");
		userRepository.save(expected);
        RebusUser actual = userRepository.findByUsername("me");
        userRepository.delete(expected);
        assertThat(actual.getPassword(), is(expected.getPassword()));
    }
}
