package org.vaadin.example.event;

import org.springframework.context.ApplicationEvent;

public class VoteAddedEvent extends ApplicationEvent {
    public VoteAddedEvent(Object source) {
        super(source);
    }
}

