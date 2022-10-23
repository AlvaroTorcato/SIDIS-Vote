package com.example.sidisvote.repository;

import com.example.sidisvote.model.Vote;
import com.example.sidisvote.model.VoteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote,Integer> {

    @Query("select new com.example.sidisvote.model.VoteDTO(f) from Vote f where f.reviewId = :idReview")
    List<VoteDTO> findVotesInReview(int idReview);
}