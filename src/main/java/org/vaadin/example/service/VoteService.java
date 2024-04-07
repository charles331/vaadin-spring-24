package org.vaadin.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.vaadin.example.entity.Vote;
import org.vaadin.example.event.VoteAddedEvent;
import org.vaadin.example.repository.VoteRepository;

import java.util.List;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public VoteService(VoteRepository voteRepository,ApplicationEventPublisher publisher) {
        this.voteRepository = voteRepository;
        this.publisher=publisher;
    }

    public Vote saveVote(Vote vote) {
        Vote save = voteRepository.save(vote);
        publisher.publishEvent(new VoteAddedEvent(vote));
        return save;
    }

    public List<Vote> findAllVotes() {
        List<Vote> votes = voteRepository.findAll();
        // Log to verify that votes are being fetched
        System.out.println("Fetched votes: " + votes);
        return votes;
    }

}
