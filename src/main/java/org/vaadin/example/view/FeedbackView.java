package org.vaadin.example.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.broadcaster.Broadcaster;
import org.vaadin.example.entity.Vote;
import org.vaadin.example.service.VoteService;

@Route("feedback")
@PageTitle("Feedback")
public class FeedbackView extends VerticalLayout {

    private final VoteService voteService;

    private TextField voterNameField = new TextField("Voter Name");
    //private TextField ratingField = new TextField("Rating (1-5)");
    private TextArea commentField = new TextArea("Comment");
    private TextArea suggestionField = new TextArea("Suggestion");
    private Button saveButton = new Button("Save Vote");

    private Binder<Vote> binder = new Binder<>(Vote.class);

    //private NumberField ratingField = new NumberField("Rating (1-5)");
    private RadioButtonGroup<Integer> ratingField = new RadioButtonGroup<>();

    @Autowired
    public FeedbackView(VoteService voteService) {
        this.voteService = voteService;
        configureForm();
        add(voterNameField, ratingField, commentField, suggestionField, saveButton);
        saveButton.addClickListener(e -> saveVote());
    }

    private void configureForm() {
        // Configure binder to bind form fields to the Vote entity
        binder.forField(voterNameField)
                .asRequired("Voter name is required")
                .bind(Vote::getVoterName, Vote::setVoterName);
//        binder.forField(ratingField)
//                .asRequired("Rating is required")
//                .withConverter(
//                        new StringToIntegerConverter(0, "Must enter a number"))
//                .bind(Vote::getRating, Vote::setRating);

//        binder.forField(ratingField)
//                .withNullRepresentation("")
//                .withConverter(
//                        new StringToIntegerConverter("Must enter a number"))
//                .bind(Vote::getRating, Vote::setRating);
        ratingField.setLabel("Rating (1-5)");
        ratingField.setItems(1, 2, 3, 4, 5); // Set the items (options) for the radio buttons.
        ratingField.setRequiredIndicatorVisible(true);

        // Other configurations for your form...

        // Bind the radio button group to the Vote entity's rating property.
        binder.forField(ratingField)
                .asRequired("Rating is required")
                .bind(Vote::getRating, Vote::setRating);

        binder.bind(commentField, Vote::getComment, Vote::setComment);
        binder.bind(suggestionField, Vote::getSuggestion, Vote::setSuggestion);

        // Bind an empty Vote entity to reset the form
        binder.setBean(new Vote());
    }

    private void saveVote() {
        Vote vote = binder.getBean();
        try {
            voteService.saveVote(vote);
            Notification.show("Vote saved successfully!");
            binder.setBean(new Vote()); // Reset form after saving
            // Navigate to the ResultsView
            UI.getCurrent().navigate(ResultsView.class);
        } catch (Exception e) {
            Notification.show("Failed to save vote: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
        Broadcaster.broadcast("Nouveau vote");
    }

}
