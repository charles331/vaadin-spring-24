package org.vaadin.example.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
