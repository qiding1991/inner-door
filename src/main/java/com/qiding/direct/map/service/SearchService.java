package com.qiding.direct.map.service;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.qiding.direct.map.common.Geometry;
import com.qiding.direct.map.param.Geo;
import com.qiding.direct.map.param.GeoPolygon;
import com.qiding.direct.map.param.GeoPolygonMap;
import com.qiding.direct.map.param.InnerMapInfo;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkAction;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log4j2
@Service
public class SearchService {


    private TransportClient client = null;
    private final String indexName;
    private Gson gson = new Gson();

    @Autowired
    MongoTemplate mongoTemplate;

    public SearchService(@Value("${app.elasticsearch.host}") String esHost,
                         @Value("${app.elasticsearch.port}") String esPort,
                         @Value("${app.elasticsearch.cluster}") String clusterName,
                         @Value("${app.elasticsearch.index}") String indexName
    ) throws UnknownHostException {

        Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                .put("client.transport.sniff", true)
                .build();
        log.info("host={},port={},index={},clustername={}", esHost, esPort, indexName, clusterName);
        client = new PreBuiltTransportClient(settings).addTransportAddresses(
                new TransportAddress(new InetSocketAddress(InetAddress.getByName(esHost), Integer.valueOf(esPort))));
        log.info("connectnodes={}", client.connectedNodes());
        this.indexName = indexName;
        refreshIndex();
    }

    // 索引下的所有文档
    public void refreshIndex() {
//        DeleteByQueryRequestBuilder requestBuilder=new DeleteByQueryRequestBuilder(client, DeleteByQueryAction.INSTANCE);
//        requestBuilder.filter(QueryBuilders.matchAllQuery())
//                .source(this.indexName).get();
//        requestBuilder.get();
//        try {
//            if(indexExists()){
//                DeleteIndexRequestBuilder bu = client.admin().indices().prepareDelete(indexName);
//                bu.get();
//            }
//        } catch (Exception e){
//            log.info("删除索引失败={}",indexName);
//        }

        if (indexExists()) {
            DeleteIndexRequestBuilder bu = client.admin().indices().prepareDelete(indexName);
            bu.get();

            //设置默认值


        }
    }

    //搜索文档
    public List<GeoPolygon> findIndex(String name) {
        //新建索引
        if (!indexExists()) {
            client.admin().indices().prepareCreate(indexName).get();
            client.admin().indices().prepareUpdateSettings(indexName).setSettings(ImmutableMap.of("max_result_window",Integer.MAX_VALUE-1)).get();
            initES(Geometry.Polygon, GeoPolygonMap.class);
        }
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("properties.name", "*"+name.toLowerCase()+"*");
        SearchResponse response = client.prepareSearch(this.indexName).setQuery(queryBuilder).setFrom(0).setSize(Integer.MAX_VALUE-1).get();
        SearchHits hits = response.getHits();

        if(hits.getHits().length==0){
            queryBuilder = QueryBuilders.matchQuery("properties.name", name);
            response = client.prepareSearch(this.indexName).setQuery(queryBuilder).setFrom(0).setSize(Integer.MAX_VALUE-1).get();
            hits = response.getHits();
        }

        List<GeoPolygon> infoList = new ArrayList<>(Long.valueOf(hits.getHits().length).intValue());
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            GeoPolygon geoPolygon = gson.fromJson(iterator.next().getSourceAsString(), GeoPolygon.class);
            geoPolygon.changePrecision();
            infoList.add(geoPolygon);
        }
        log.info("search result={}", infoList);
        return infoList;
    }

    public void initES(Geometry geometry, Class cls) {
        Query query = Query.query(Criteria.where("geometry").is(geometry.getString()));
        List<InnerMapInfo> infoList = new ArrayList<>();
        infoList.addAll(mongoTemplate.find(query, cls, "innerMapInfo"));
        log.info("init search source={}", infoList);
        BulkRequestBuilder bulkActionBuilder = new BulkRequestBuilder(client, BulkAction.INSTANCE);
      // IndexRequestBuilder indexRequestBuilder=new IndexRequestBuilder(client, IndexAction.INSTANCE);
        infoList.stream().forEach(item -> {
            item.getFeatures().stream().forEach(feature -> {
                log.info("index source={}", feature);
                ((Geo) feature).getProperties().put("floor", item.getFloor());
                IndexRequest request = new IndexRequest(this.indexName);
                request.source(gson.toJson(feature), XContentType.JSON);
//                indexRequestBuilder.setIndex(this.indexName);
//                indexRequestBuilder.setSource(gson.toJson(feature), XContentType.JSON);
//                IndexResponse response= indexRequestBuilder.get();
//                log.info("response={}",response);
                bulkActionBuilder.add(request);
            });
        });
        //发送请求
        bulkActionBuilder.get();
    }


    public Boolean indexExists() {
        try {
            ActionFuture<IndicesExistsResponse> future = client.admin().indices().exists(new IndicesExistsRequest(indexName));
            return future.get().isExists();
        } catch (Exception e) {
            log.error("删除索引失败={}", indexName, e);
            return false;
        }

    }
}
