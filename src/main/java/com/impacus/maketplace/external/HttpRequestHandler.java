package com.impacus.maketplace.external;

import com.google.gson.JsonObject;
import groovy.util.logging.Slf4j;
import net.minidev.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import jakarta.validation.ValidationException;
@Slf4j
public class HttpRequestHandler {

    public JSONObject sendHttp(String url, JsonObject map, String accessToken, String type) throws Exception {
        StringBuffer rtnString = new StringBuffer();
        URL urlAsUrl;
        JSONObject resultJson = new JSONObject();
        String resultCd ="";
        String body = "";
        String sRtnMsg = "";
        try {
            if (null == url || url.equals(""))	throw new ValidationException("INVALID HTTP REQUEST URL");
            urlAsUrl = new URL(url);
            URLConnection con = urlAsUrl.openConnection();
            HttpURLConnection hurlc = (HttpURLConnection) con;

            hurlc.setRequestMethod("POST");
            hurlc.setDoOutput(true);
            hurlc.setDoInput(true);
            hurlc.setUseCaches(false);
            hurlc.setDefaultUseCaches(false);
            body = map.toString();
            hurlc.setRequestProperty("Content-Type", "application/json");
            hurlc.setRequestProperty("cache-control", "no-cache");
            if (null != accessToken) hurlc.setRequestProperty("Authorization", "Bearer " + accessToken);

            PrintWriter out = new PrintWriter(hurlc.getOutputStream());
            out.println(body);
            out.close();
            BufferedReader in;
            if (hurlc.getResponseCode() != 200) {
                in = new BufferedReader(new InputStreamReader(hurlc.getErrorStream(), "UTF-8"));
                resultCd = String.valueOf(hurlc.getResponseCode());
            } else {
                in = new BufferedReader(new InputStreamReader(hurlc.getInputStream(), "UTF-8"));
                resultCd = String.valueOf(hurlc.getResponseCode());
            }
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                rtnString.append(inputLine);
            }
            in.close();
            sRtnMsg = rtnString.toString();
//            JSONParser parser = new JSONParser();
//            resultJson = (JSONObject) parser.parse(sRtnMsg);
            resultJson.put("resultCd", resultCd);
        } catch (ValidationException e) {
            resultJson.put("resultCd", "400");
        } catch (IOException e) {
            resultJson.put("resultCd", "500");
        } catch (Exception e) {
            resultJson.put("resultCd", "500");
            e.printStackTrace();
        }

        return resultJson;
    }

}
