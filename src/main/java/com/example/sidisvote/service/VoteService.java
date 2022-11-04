package com.example.sidisvote.service;

import com.example.sidisvote.model.UserDetailsDTO;
import com.example.sidisvote.model.Vote;
import com.example.sidisvote.model.VoteDTO;
import com.example.sidisvote.model.VoteDetailsDTO;
import com.example.sidisvote.repository.VoteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class VoteService {
    @Autowired
    private VoteRepository repository;
    @Autowired
    private RequestService service;
    public VoteDTO createVote(final VoteDetailsDTO resource, int reviewId, HttpServletRequest request) throws IOException {
        int statusCode = getStatusCodeOfReview(reviewId);
        if (statusCode == 404){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Review Not Found");
        }
        String jwt = parseJwt(request);
        UserDetailsDTO user = makeRequestToAutentication(jwt);
        if (!user.getRoles().equals("[MODERATOR]") && !user.getRoles().equals("[COSTUMER]")){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t be accessed by this user");
        }
        VoteDTO voteDTO = repository.findIfUserMadeAVoteInAReview(reviewId,user.getId());
        if (voteDTO != null){
            throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "Can´t make another vote on this review");
        }
        Vote vote = new Vote(resource.getVote(),reviewId,user.getId());
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

    public List<VoteDTO> searchVotes(int idReview) {
        List<VoteDTO> votes = repository.findVotesInReview(idReview);
        if (votes == null){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote Not Found");
        }
        return votes;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }

        return null;
    }

    public UserDetailsDTO makeRequestToAutentication(String jwt){
        String urlRequest = "http://localhost:8084/auth/search" + jwt;
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
}
