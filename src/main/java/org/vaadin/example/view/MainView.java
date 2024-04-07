package org.vaadin.example.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@PageTitle("Accueil | Vote Vidéo")
public class MainView extends VerticalLayout {

    @Autowired
    public MainView() {
        // Champ pour le nom du participant
        TextField nameField = new TextField("Votre nom");
        nameField.setPlaceholder("Entrez votre nom...");

        // Bouton pour soumettre et aller à la page de feedback/vote
        Button button = new Button("Aller au vote", event -> {
            // Ici, vous pouvez ajouter du code pour enregistrer le nom du participant si nécessaire
            String participantName = nameField.getValue();
            Notification.show("Bienvenue " + participantName);

            // Redirection vers la page de feedback/vote
            getUI().ifPresent(ui -> ui.navigate("feedback"));
        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Ajout des composants au layout
        add(new Paragraph("Bienvenue à la session de vote vidéo !"), nameField, button);

        // Ajustez la mise en page ou l'apparence selon vos besoins
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }
}
