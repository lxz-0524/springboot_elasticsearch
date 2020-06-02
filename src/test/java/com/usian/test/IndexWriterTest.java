package com.usian.test;

import com.usian.ESApp;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ESApp.class})
public class IndexWriterTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient ;

    @Test
    public void testCrateIndex(){

    }


    @Test
    public void testDeleteIndex(){

    }
}
