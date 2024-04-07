package org.vaadin.example.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.vaadin.example.entity.Vote;
import org.vaadin.example.event.VoteAddedEvent;
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
    private ApplicationEventPublisher eventPublisher;

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
        // Publish the event
        eventPublisher.publishEvent(new VoteAddedEvent(vote));
    }


//    public FeedbackView() {
//        // Titre de la page
//        H1 heading = new H1("Feedback sur la vidéo de la semaine");
//
//        // Intégration vidéo - Utiliser un composant approprié ou un iframe
//        // Remplacer `urlVideo` par l'URL de votre vidéo
//        String urlVideo = "https://example.com/video";
//        Anchor videoLink = new Anchor(urlVideo, "Voir la vidéo de la semaine");
//        videoLink.setTarget("_blank");
//
//        // Avis
//        TextArea feedbackField = new TextArea("Votre avis");
//        feedbackField.setPlaceholder("Écrivez ici...");
//
//        // Suggestions d'amélioration
//        TextArea improvementField = new TextArea("Suggestions d'amélioration");
//        improvementField.setPlaceholder("Écrivez ici...");
//
//        // Notation
//        RadioButtonGroup<String> rating = new RadioButtonGroup<>();
//        rating.setLabel("Notez la vidéo");
//        rating.setItems("1", "2", "3", "4", "5");
//
//        // Bouton de soumission
//        Button submitButton = new Button("Soumettre", event -> {
//            // Traitement à l'envoi
//            // Sauvegarder les feedbacks et la note, afficher une notification, etc.
//        });
//
//        // Ajouter tous les composants au layout
//        add(heading, videoLink, feedbackField, improvementField, rating, submitButton);
//    }

//    private void saveVote() {
//        Vote vote = new Vote();
//        vote.setVoterName(voterNameField.getValue());
//        vote.setRating(ratingField.getValue().intValue());
//        vote.setComment(commentField.getValue());
//        vote.setSuggestion(suggestionField.getValue());
//
//        voteService.saveVote(vote);
//        Notification.show("Vote saved successfully!");
//        // Optionally, clear the form fields or take other actions
//    }
}
