package com.example.sidisvote.service;

import com.example.sidisvote.model.Vote;
import com.example.sidisvote.model.VoteDTO;
import com.example.sidisvote.model.VoteDetailsDTO;
import com.example.sidisvote.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class VoteService {
    @Autowired
    private VoteRepository repository;
    public VoteDTO createVote(final VoteDetailsDTO resource, int reviewId, int userId) throws IOException {
        int statusCode = getStatusCodeOfReview(reviewId);
        if (statusCode == 404){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found");
        }
        Vote vote = new Vote(resource.getVote(),reviewId,userId);
        repository.save(vote);
        VoteDTO reviewDTO = new VoteDTO(vote);
        return reviewDTO;
    }
    public int getStatusCodeOfReview(int reviewId){
        int statusCode;
        try{
            String urlRequest = "http://localhost:8082/reviews/search/" + reviewId;
            URL url = new URL(urlRequest);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            statusCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return statusCode;
    }

}
