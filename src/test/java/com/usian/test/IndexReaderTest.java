package com.usian.test;

import com.usian.ESApp;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.hibernate.validator.constraints.br.TituloEleitoral;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ESApp.class})
public class IndexReaderTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient ;

    private SearchRequest searchRequest ;

    private SearchResponse searchResponse ;

    @Before
    public void initSearchRequest(){
        //搜索请求对象
        searchRequest = new SearchRequest("java1906");
        searchRequest.types("course");
    }


    //查询文档
    @Test
    public void testGetDoc() throws IOException {
        GetRequest getRequest = new GetRequest("java1906","course","1");
        GetResponse response = restHighLevelClient.get(getRequest,RequestOptions.DEFAULT);
        boolean exists = response.isExists();
        System.out.println("exists: "+exists);
        String source = response.getSourceAsString();
        System.out.println("source: "+source);
    }

    //搜索type下的全部记录
    @Test
    public void testSearchAll() throws IOException {
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        //为了避免代码冗余，将重复性的代码提取，分别置于@Before、@After的方法中
       /* //搜索匹配结果
        SearchHits responseHits = searchResponse.getHits();
        long totalHits = responseHits.totalHits;
        System.out.println("共搜索到" + totalHits + "条文档");
        //匹配的文档
        SearchHit[] hits = responseHits.getHits();
        //
        for (SearchHit hit : hits){
            String id = hit.getId();
            System.out.println("文档id：" + id);
            //源文档内容
            String sourceAsString = hit.getSourceAsString();
            System.out.println("源文档内容："+sourceAsString);
        }*/
    }

    //分页搜索type下的全部记录
    @Test
    public void testSearchPage() throws IOException {
        //搜索请求对象
       /* SearchRequest searchRequest = new SearchRequest("java1906");
        searchRequest.types("course");*/
        //搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(2);
        searchSourceBuilder.sort("price", SortOrder.DESC);
        //设置搜索源
        searchRequest.source(searchSourceBuilder);

        //执行搜索
        searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

    }

    //match查询
    @Test
    public void testMatchQuery() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("name","spring开发").operator(Operator.AND));
        //设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索
        searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    //multi_match查询
    @Test
    public void testMultiMatchQuery() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("开发","name","description"));
        //设置搜索源
        searchRequest.source(searchSourceBuilder);
        //执行搜索
        searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    //bool查询
    @Test
    public void testBoolQuery() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //bool
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //must
        boolQueryBuilder.must(QueryBuilders.matchQuery("name","开发"));
        boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(50).lte(100));
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
    }

    //filter查询
    @Test
    public void testFilterQuery() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("name","开发"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(50).lte(100));
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
    }

    @After
    public void testDisplayDoc(){
        //搜索匹配结果
        SearchHits responseHits = searchResponse.getHits();
        long totalHits = responseHits.totalHits;
        System.out.println("共搜索到" + totalHits + "条文档");
        //匹配的文档
        SearchHit[] hits = responseHits.getHits();
        //
        for (SearchHit hit : hits){
            String id = hit.getId();
            System.out.println("文档id：" + id);
            //源文档内容
            String sourceAsString = hit.getSourceAsString();
            System.out.println("源文档内容："+sourceAsString);
        }
    }
}
