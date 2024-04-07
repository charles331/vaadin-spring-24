package org.vaadin.example.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.vaadin.example.entity.Vote;
import org.vaadin.example.event.VoteAddedEvent;
import org.vaadin.example.service.VoteService;

@Route(value = "results")
@PageTitle("Results | Vote App")
public class ResultsView extends VerticalLayout {

    private final VoteService voteService;
    private final Grid<Vote> grid = new Grid<>(Vote.class);

    @Autowired
    public ResultsView(VoteService voteService, ApplicationEventPublisher eventPublisher) {
        this.voteService = voteService;
        setSizeFull(); // Ensure the layout takes the full size
        configureGrid();
        add(grid);
        updateList();
    }

    @EventListener
    public void onVoteAdded(VoteAddedEvent event) {
        UI.getCurrent().access(() -> {
            updateList();
        });
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns(); // Remove auto-generated columns

        // Explicitly add columns
        grid.addColumn(Vote::getVoterName).setHeader("Voter Name");
        grid.addColumn(Vote::getRating).setHeader("Rating");
        grid.addColumn(Vote::getComment).setHeader("Comment");
        grid.addColumn(Vote::getSuggestion).setHeader("Suggestion");

        // Alternatively, if you want to use property names directly, uncomment the following line
        // grid.setColumns("voterName", "rating", "comment", "suggestion");
    }

    private void updateList() {
        grid.setItems(voteService.findAllVotes()); // Fetch and display all votes
    }
}
