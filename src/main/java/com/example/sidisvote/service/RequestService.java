package com.example.sidisvote.service;

import com.example.sidisvote.model.UserDetailsDTO;
import com.example.sidisvote.model.VoteAPOD;
import com.example.sidisvote.model.VoteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RequestService {
    String baseURL="http://localhost:8087/";
    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }
    public List<VoteDTO> retrieveVoteFromApi(int reviewId) throws IOException {
        String baseUrl = baseURL+"votes/search/"+reviewId;
        List<VoteDTO> vote= new ArrayList<>();
        try {
            InputStream responseStream = openConn(baseUrl).getInputStream();

            ObjectMapper mapper = new ObjectMapper();

            List<VoteAPOD> apods = Arrays.asList(mapper.readValue(responseStream, VoteAPOD.class));
            for (VoteAPOD apod: apods){
                vote.add(new VoteDTO(apod));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
        return vote;
    }

    public UserDetailsDTO makeRequestToAutentication(String jwt) {
        String urlRequest = "http://localhost:8084/auth/search/" + jwt;
        UserDetailsDTO user = null;
        try {
            InputStream responseStream = openConn(urlRequest).getInputStream();

            ObjectMapper mapper = new ObjectMapper();

            user = mapper.readValue(responseStream, UserDetailsDTO.class);
        } catch (IOException e) {
            System.out.println(e);
        }

        return user;
    }

    private HttpURLConnection openConn(String baseUrl) throws IOException {

        URL url = new URL(baseUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");

        return connection;
    }

    public int getStatusCodeOfReview(int reviewId) {
        int statusCode;
        try {
            String urlRequest = "http://localhost:8082/reviews/search/" + reviewId;
            URL url = new URL(urlRequest);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            statusCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return statusCode;
    }

    public int updateReview(int rid, boolean vote){ //str true false
        int statusCode;
        String str="";
        if(vote)str="true";
        else str="false";
        try {
            String urlRequest = "http://localhost:8082/reviews/vote/"+rid+"/"+str;
            URL url = new URL(urlRequest);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            statusCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (statusCode!=200){
            try {
                String urlRequest = "http://localhost:8086/reviews/vote/"+rid+"/"+str;
                URL url = new URL(urlRequest);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                statusCode = connection.getResponseCode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return statusCode;
    }
}
