package com.moomit.booklist.API;

import com.google.gson.Gson;
import com.moomit.booklist.model.Book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NaverBookSearchAPI {

    public static void main(String searchWord, final int start, NaverBookSearchAPIListener listener) {

        String clientId = "5yAIHRTlJYSUxYNtkudb";
        String clientSecret = "_1pu_0bWhq";

        try {
            String text = null;

            text = URLEncoder.encode(searchWord, "UTF-8");

            String apiURL;

            apiURL = "https://openapi.naver.com/v1/search/book?query=" + text+"&start="+start;



            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("X-Naver-Client-Id", clientId);
            requestHeaders.put("X-Naver-Client-Secret", clientSecret);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String responseBody = get(apiURL, requestHeaders);
                    Gson gson = new Gson();

                    Book book = gson.fromJson(responseBody,Book.class);
                    listener.onSuccess(book);
                }
            }).start();




        } catch (Exception e) {

            listener.onFailed();

        }



    }


    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

    /*
     *       API 통신 성공 여부 Listener 생성
     */
    public interface NaverBookSearchAPIListener{
        void onSuccess(Book book);
        void onFailed();
    }
}