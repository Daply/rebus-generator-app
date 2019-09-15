package com.rebusgenerator.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.rebusgenerator.entity.Syllable;
import com.rebusgenerator.repository.SyllableRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SyllableServiceIntegrationTest {
	
	@Autowired
    private SyllableService syllableService;
	
	@MockBean
	private SyllableRepository syllableRepository;
	
	@Before
    public void setUp() {
		Syllable syllable1 = new Syllable("fa");
		Syllable syllable2 = new Syllable("on");
		Syllable syllable3 = new Syllable("to");
		Syllable syllable4 = new Syllable("be");

        //
        Mockito.when(syllableRepository.findSyllable("fa")).thenReturn(syllable1);
        Mockito.when(syllableRepository.findSyllable("on")).thenReturn(syllable2);
        Mockito.when(syllableRepository.findSyllable("to")).thenReturn(syllable3);
        Mockito.when(syllableRepository.findSyllable("be")).thenReturn(syllable4);
    }
	
	@Test
    public void whenFindRebusByRebusWord_thenRebusMustBeReturned() {
		Syllable found = syllableService.findSyllable("to");
        String expected = "to";
		
        assertThat(found.getSyllable(), is(expected));
    }
}
