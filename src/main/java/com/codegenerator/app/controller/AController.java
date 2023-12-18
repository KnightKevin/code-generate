package com.codegenerator.app.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AController {

    final private String[] modules = {
            "base",
            "connector",
            "maintenance",
            "operation",
            "oss",
            "portal",
            "rds",
            "ticket",
            "vm"
    };

    final private String host = "http://host16:8100";

    final private String uri = "/v4/pub/cmp-global/debug/listRoute";


    /**
     *
     * */
    @GetMapping("/a")
    public Map<String, List<RouteInfo>> a() throws IOException {

        Map<String, List<RouteInfo>> r = new HashMap<>();
        for (String module : modules) {
            String url = host+"/"+module+uri;
            JSONObject json = sendGetRequest(url);

            List<RouteInfo> routes = new ArrayList<>();
            for (String key : json.keySet()) {
                RouteInfo info = json.getObject(key, RouteInfo.class);
                info.setModule(module);
                if (
                        info.uri.startsWith("/swagger") ||
                        info.uri.startsWith("/web/index") ||
                        info.uri.startsWith("/error")
                ) {
                    continue;
                }

                routes.add(info);
            }

            r.put(module, routes);


        }


        return r;
    }

    private final OkHttpClient httpClient = new OkHttpClient();

    public JSONObject sendGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            return JSONObject.parseObject(response.body().string());
        }
    }


    @Data
    private static class RouteInfo {
        private String uri;
        private String module;
        private String actionKey = "";
        private String method = "";
    }
}
