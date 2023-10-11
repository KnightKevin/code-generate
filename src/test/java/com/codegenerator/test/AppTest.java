package com.codegenerator.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.codegenerator.app.controller.Remote;
import com.codegenerator.app.controller.Types;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
public class AppTest {

    private static final Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");


    @Test
    public void test1() {

    }

    @Test
    public void test2() throws IOException {




        Remote.WriteRequest.Builder writeRequestBuilder = Remote.WriteRequest.newBuilder();
        Types.MetricMetadata.Builder builder = Types.MetricMetadata.newBuilder();
        builder.setType(Types.MetricMetadata.MetricType.GAUGE);
        builder.setMetricFamilyName("cloud_api_metric");
        builder.setHelp("sync cloud api metric");
        Types.MetricMetadata metricMetadata = builder.build();
        writeRequestBuilder.addMetadata(metricMetadata);




        String metricNamePrefix = "my_metricName_";
        String moIdPrefix = "moId-";

        int metricNum = 10;

        // 单位耗秒
        int period = 10000;

        // 云主机台数
        int vmNum = 5000;



        for (int j=0;j<vmNum;j++) {
            String moId = moIdPrefix+j;
            //一个云主机须要构造n个指标
            for (int i=0;i<metricNum;i++) {
                String metricName = metricNamePrefix+i;
                // 构造某个个指标的time series
                Types.TimeSeries timeSeries = buildMoTimeSeries(moId, metricName, period);
                writeRequestBuilder.addTimeseries(timeSeries);
            }


            // 每1000台推送一次
            if ((j!=0 && j%100 == 0) || j == vmNum-1 ) {
                push(writeRequestBuilder.build());
                writeRequestBuilder.clearTimeseries();
            }
        }

        Remote.WriteRequest message = writeRequestBuilder.build();

        final byte[] compressedMessage;
        try {
            compressedMessage = Snappy.compress(message.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        RestTemplate restTemplate = new RestTemplate();

        int timeout = 3000; // 5 seconds
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        restTemplate.setRequestFactory(factory);

        //参考的 go http send 版本
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Encoding", "snappy");
//        headers.add("User-Agent", ;"opcai")
        headers.add("X-Prometheus-Remote-Write-Version", "0.1.0");
        headers.add("Content-Type", "application/x-protobuf");

        HttpEntity requestEntity = new HttpEntity(compressedMessage, headers);

        ResponseEntity<Object> exchange = restTemplate.exchange("http://host231:9090/api/v1/write", HttpMethod.POST, requestEntity, Object.class);

        log.info("s {}", exchange);
    }

    @Test
    public void test3() throws IOException {


        RestTemplate restTemplate = new RestTemplate();

        int timeout = 3000; // 5 seconds
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        restTemplate.setRequestFactory(factory);

        //参考的 go http send 版本
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Encoding", "snappy");
//        headers.add("User-Agent", ;"opcai")
        headers.add("X-Prometheus-Remote-Write-Version", "0.1.0");
        headers.add("Content-Type", "application/x-protobuf");

        HttpEntity requestEntity = new HttpEntity(headers);


        String baseUrl = "http://127.0.0.1:9090/api/v1/query_range";
        // 使用UriComponentsBuilder构建带有查询参数的URL
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("query", "{query}")
                .queryParam("start", 1693894544)
                .queryParam("end", 1693898144)
                .queryParam("step", 360)
                ;

        URI uri = builder.encode().build().expand("{__name__=~\"cpu_util|mem_util|disk_util\"}").toUri();
        ResponseEntity<Object> exchange = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                Object.class
        );

    log.info("sadf");

    }


    @Test
    public void test4() throws ParseException {




        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = sdf.parse("2023-09-12 13:39:41");

        long a = d1.getTime();

        long step = 60000*5;

        long b = (a/step)*step;

        String dateString = sdf.format(new Date(b));

        System.out.println(sdf.format(new Date(a)));
        System.out.println(dateString);
    }

    /**
     *
     * 获        String urlWithParams = builder.toUriString();取某个云主机的某个指标的time series
     * */
    private Types.TimeSeries buildMoTimeSeries(String moId, String metricName, int period) {
        // 构造某个个指标的time series
        Types.TimeSeries.Builder timeSeriesBuilder = getTimeSeriesBuilder(metricName, moId);

        long currentTime = System.currentTimeMillis();
        long from = currentTime - 3500000;
        long to = currentTime;
        for (long i=from;i<to;i+=period) {
            Types.Sample sample = Types.Sample.newBuilder()
                    .setTimestamp(i)
                    .setValue(randomDouble())
                    .build();
            timeSeriesBuilder.addSamples(sample);
        }

        return timeSeriesBuilder.build();
    }










    private void push(Remote.WriteRequest message) {

        final byte[] compressedMessage;
        try {
            compressedMessage = Snappy.compress(message.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        RestTemplate restTemplate = new RestTemplate();

        int timeout = 3000; // 5 seconds
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        restTemplate.setRequestFactory(factory);

        //参考的 go http send 版本
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Encoding", "snappy");
//        headers.add("User-Agent", ;"opcai")
        headers.add("X-Prometheus-Remote-Write-Version", "0.1.0");
        headers.add("Content-Type", "application/x-protobuf");

        HttpEntity requestEntity = new HttpEntity(compressedMessage, headers);

        ResponseEntity<Object> exchange = restTemplate.exchange("http://host231:9090/api/v1/write", HttpMethod.POST, requestEntity, Object.class);
    }


    private double randomDouble() {
        // 创建 Random 对象
        Random random = new Random();

        // 生成 0 到 1 之间的 4 位小数
        double randomDecimal = random.nextDouble();

        return randomDecimal;
    }

    private Types.TimeSeries.Builder getTimeSeriesBuilder(String metricName, String moId) {
        Types.TimeSeries.Builder timeSeriesBuilder = Types.TimeSeries.newBuilder();

        //必须要有 metric_name
        Types.Label metricNameLabel = Types.Label.newBuilder().setName("__name__").setValue(metricName).build();
        Types.Label instanceIdLabel = Types.Label.newBuilder().setName("moUuid").setValue(moId).build();
        Types.Label otherLabel = Types.Label.newBuilder().setName("dataFrom").setValue("cloudApi").build();
        timeSeriesBuilder.addAllLabels(Arrays.asList(metricNameLabel, instanceIdLabel, otherLabel));

        return timeSeriesBuilder;
    }


}
