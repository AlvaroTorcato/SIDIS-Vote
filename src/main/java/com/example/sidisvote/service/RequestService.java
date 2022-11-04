package com.example.sidisvote.service;

import com.example.sidisvote.model.VoteAPOD;
import com.example.sidisvote.model.VoteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RequestService {
    String baseURL="http://localhost:8087/";
    private HttpURLConnection openConn(String Url) throws IOException {

        URL url = new URL(Url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("accept", "application/json");

        return connection;
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
}
