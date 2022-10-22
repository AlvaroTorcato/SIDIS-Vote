package com.example.sidisvote.repository;

import com.example.sidisvote.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,Integer> {


}