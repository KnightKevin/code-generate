package com.codegenerator.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.codegenerator.app.model.Metric;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.xerial.snappy.Snappy;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Slf4j
@RestController
public class TestController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/a")
    public void a(int n) {

        String prefix = "http_";

        StringBuilder metricData = new StringBuilder();


        for (int i = 0; i < n; i++) {
            metricData.append(buildMetric(new Metric(prefix + i, randomDouble())));
        }

        push(metricData.toString());

        log.info("send metricData is: {}", metricData);
    }

    @GetMapping("/b")
    public void b() throws IOException {

        Remote.WriteRequest.Builder writeRequestBuilder = Remote.WriteRequest.newBuilder();
        Types.MetricMetadata.Builder builder = Types.MetricMetadata.newBuilder();
        builder.setType(Types.MetricMetadata.MetricType.UNKNOWN);
        builder.setMetricFamilyName("metricFamilyName");
        builder.setHelp("helper");
        Types.MetricMetadata metricMetadata = builder.build();
        writeRequestBuilder.addMetadata(metricMetadata);

        Types.Sample inSample = Types.Sample.newBuilder().setTimestamp(System.currentTimeMillis()).setValue(12.4).build();
        Types.TimeSeries.Builder timeSeriesBuilder = Types.TimeSeries.newBuilder();
        timeSeriesBuilder.addSamples(inSample);

        writeRequestBuilder.addTimeseries(timeSeriesBuilder);

        try {
            remoteWrite("datasourceId", writeRequestBuilder);
        } catch (IOException e) {
            log.error("远程写入prometheus出错, 异常信息为: {}", e.getMessage(), e);
        }



        CollectorRegistry registry = new CollectorRegistry();

        Gauge customMetric = Gauge.build()
                .name("custom_metric")
                .help("My custom metric")
                .register(registry);

        // Set metric value
        customMetric.set(5.5);

        // Push to Prometheus
        String url = "http://host231:9090/api/v1/write";
        String payload = "custom_metric 5.5";

        // Use a HTTP client to send data
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(payload));
        httpclient.execute(httpPost);

    }


    @PostMapping("/api/v1/write")
    public void c(HttpServletRequest request, @RequestBody byte[] dddd) throws IOException {

        String s = new String(Snappy.uncompress(dddd));
        List<Types.TimeSeries> timeSeriesList = Remote.WriteRequest.parseFrom(Snappy.uncompress(dddd)).getTimeseriesList();

        //log.error("{}", s);

    }

    @GetMapping("/d")
    public void d() throws IOException {
        List<SendData> datas = initData();

        Remote.WriteRequest.Builder writeRequestBuilder = Remote.WriteRequest.newBuilder();
        Types.MetricMetadata.Builder builder = Types.MetricMetadata.newBuilder();
        builder.setType(Types.MetricMetadata.MetricType.GAUGE);
        builder.setMetricFamilyName("bandwitch_guage");
        builder.setHelp("helper");
        Types.MetricMetadata metricMetadata = builder.build();
        writeRequestBuilder.addMetadata(metricMetadata);



        for (SendData data : datas) {
            Types.TimeSeries.Builder timeSeriesBuilder = Types.TimeSeries.newBuilder();

            //必须要有 metric_name
            Types.Label inNameLavble = Types.Label.newBuilder().setName("__name__").setValue("cpu_util").build();//metrics name
            Types.Label instanceIdLabel = Types.Label.newBuilder().setName("instanceId").setValue("123123").build();
            Types.Label otherLabel = Types.Label.newBuilder().setName("other").setValue("123123").build();

            Types.Sample inSample = Types.Sample.newBuilder().setTimestamp(data.timestamp).setValue(data.in.doubleValue()).build();
            timeSeriesBuilder.addSamples(inSample);
            timeSeriesBuilder.addAllLabels(Arrays.asList(inNameLavble, instanceIdLabel, otherLabel));

            writeRequestBuilder.addTimeseries(timeSeriesBuilder.build());
        }




        Remote.WriteRequest message = writeRequestBuilder.build();

        byte[] compressedMessage = Snappy.compress(message.toByteArray());


        RestTemplate restTemplate = new RestTemplate();

        //参考的 go http send 版本
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Encoding", "snappy");
//        headers.add("User-Agent", ;"opcai")
        headers.add("X-Prometheus-Remote-Write-Version", "0.1.0");
        headers.add("Content-Type","application/x-protobuf");

        HttpEntity requestEntity = new HttpEntity(compressedMessage, headers);

        ResponseEntity<Object> exchange = restTemplate.exchange("http://host231:9090/api/v1/write", HttpMethod.POST, requestEntity, Object.class);

        System.out.println(exchange);
    }

    private void remoteWrite(String datasourceId, Remote.WriteRequest.Builder writeRequestBuilder) throws IOException {
        // 将写请求使用Snappy压缩为字节数组
        Remote.WriteRequest writeRequest = writeRequestBuilder.build();
        log.info("ddddddd = {}", new String(writeRequest.toByteArray()));
        byte[] compressed = Snappy.compress(writeRequest.toByteArray());

        // 获取远程写URL
        String remoteWriteUrl = "http://host231:9090/api/v1/write";

        HttpPost httpPost = new HttpPost(remoteWriteUrl);
        // 添加prometheus请求头信息, 参考go版本请求发送头
        httpPost.setHeader("Content-type", "application/x-protobuf");
        httpPost.setHeader("Content-Encoding", "snappy");
        httpPost.setHeader("X-Prometheus-Remote-Write-Version", "0.1.0");

        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(compressed);
        httpPost.getRequestLine();
        httpPost.setEntity(byteArrayEntity);

        // 添加重试机制
        for (int i = 1; i <= 1; i++) {
            try {
                HttpClient httpclient = HttpClients.createDefault();

                CloseableHttpResponse response = (CloseableHttpResponse) httpclient.execute(httpPost);
                log.info("远程写入prometheus数据结果, {}", response);
                break;
            } catch (Exception e) {
                log.error("[POST/HTTP 远程写入Prometheus请求信息]异常, 重试次数:{}, 请求地址:{}, 异常信息:{}", i, remoteWriteUrl, e);
            }
        }
    }

    private String buildMetric(Metric metric) {
        return String.format("my_metric{instanceId=\"%s\"} %f\n", metric.getInstanceId(), metric.getValue());
    }


    private void push(String metricData) {
        String pushgatewayUrl = "http://host231:9091/metrics/job/my_job";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        HttpEntity<String> requestEntity = new HttpEntity<>(metricData, headers);


        restTemplate.postForObject(pushgatewayUrl, requestEntity, Void.class);
    }


    private double randomDouble() {
        // 创建 Random 对象
        Random random = new Random();

        // 生成 0 到 1 之间的 4 位小数
        double randomDecimal = random.nextDouble();

        return randomDecimal;
    }

    @PostMapping("/b")
    public String b(@RequestBody JSONObject object) {
        log.info("alertManager:   {}", object.toJSONString());
        return "b";
    }

    private List<SendData> initData() {

        long time = System.currentTimeMillis() - 60*60*1000;
        List<SendData> datas = new ArrayList<>();
        for (int j = 1; j <= 60; j++) {


            long timestamp = time + 15000*j;
//                long timestamp = System.currentTimeMillis();

            SendData sendData = new SendData();
            sendData.setIn(BigInteger.valueOf(new Random().nextInt(30)));
            sendData.setOut(BigInteger.valueOf(new Random().nextInt(30)));
            sendData.setTimestamp(timestamp);
            sendData.setSnmpId(1L);
            sendData.setFlowType(1);
            sendData.setFlowId(1);
            sendData.setHost("11111");

            datas.add(sendData);
        }

        return datas;
    }


    @Data
    public static class SendData {

        private BigInteger in;
        private BigInteger out;
        private Long index;
        private Long timestamp;
        @JsonProperty("snmp_id")
        private Long snmpId;
        private String ip;

        @JsonProperty("flow_type")
        private Integer flowType;

        @JsonProperty("flow_id")
        private int flowId = 0;

        private Integer sign = 1;

        private String host;
        //getter and setter
    }
}
