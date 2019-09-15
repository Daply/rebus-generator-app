package com.rebusgenerator.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rebusgenerator.entity.Rebus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RebusRepositoryIntegrationTest {
	
	@Autowired
    private RebusRepository rebusRepository;
	
	@Test
    public void whenFindRebus_thenReturnRebus() {
		Rebus expected = new Rebus();
		expected.setRebusWord("pen");
		expected.setRebusSequence(Arrays.asList("[front change] h=p", "hen"));
		
		rebusRepository.save(expected);

        Rebus actual = rebusRepository.findRebus(expected.getRebusWord());
        
        rebusRepository.delete(expected);
 
        assertThat(actual.getRebusSequence().get(0), is(expected.getRebusSequence().get(0)));
    }
}
