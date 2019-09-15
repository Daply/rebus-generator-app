package com.rebusgenerator.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.rebusgenerator.entity.Rebus;
import com.rebusgenerator.repository.RebusRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RebusServiceIntegrationTest {
	
	@Autowired
    private RebusService rebusService;
	
	@MockBean
	private RebusRepository rebusRepository;
	
	@Before
    public void setUp() {
		Rebus rebus1 = new Rebus();
		rebus1.setRebusWord("pen");
		rebus1.setRebusSequence(Arrays.asList("[front change] h=p", "hen"));
		Rebus rebus2 = new Rebus();
		rebus2.setRebusWord("hen");
		rebus2.setRebusSequence(Arrays.asList("[front change] p=h", "pen"));

        //
        Mockito.when(rebusRepository.findRebus(rebus1.getRebusWord())).thenReturn(rebus1);
        Mockito.when(rebusRepository.findRebus(rebus2.getRebusWord())).thenReturn(rebus2);
    }
	
	@Test
    public void whenFindRebusByRebusWord_thenRebusMustBeReturned() {
		String word = "hen";
        Rebus found = rebusService.getRebus(word);
        Rebus expected = new Rebus();
        expected.setRebusWord("hen");
        expected.setRebusSequence(Arrays.asList("[front change] p=h", "pen"));
        assertThat(found.getRebusSequence(), is(expected.getRebusSequence()));
    }
}
