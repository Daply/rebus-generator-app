package com.rebusgenerator.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.rebusgenerator.entity.Syllable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SyllableRepositoryIntegrationTest {
	
	@Autowired
    private SyllableRepository syllableRepository;
	
	@Test
    public void whenFindSyllableByItsWordPart_thenReturnSyllable() {	
		Syllable syllable = new Syllable("xt");

		syllableRepository.save(syllable);

        Syllable actual = syllableRepository.findSyllable("xt");
        
        syllableRepository.delete(syllable);
 
        assertThat(actual.getSyllable(), is(syllable.getSyllable()));
    }
}
