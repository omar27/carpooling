package com.carpooling.web.rest;

import com.carpooling.service.DecisionEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserResourceV2 {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private String email;

    private final DecisionEngineService decisionEngineService;

    private final SimpMessageSendingOperations messagingTemplate;

    public UserResourceV2(DecisionEngineService decisionEngineService, SimpMessageSendingOperations messagingTemplate) {

        this.decisionEngineService = decisionEngineService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/data")
    public void saveData(@RequestBody String email) {
        this.email = email;
    }

    @Scheduled(fixedRate = 5000)
    public void sendRidesToUI() {
        if (this.email != null) {
            try {
                this.messagingTemplate.convertAndSend("/topic/passenger",
                        this.decisionEngineService.getRidesWithFavLocations(this.email));
                this.messagingTemplate.convertAndSend("/topic/locations",
                        this.decisionEngineService.getFavLocations(this.email));
                this.messagingTemplate.convertAndSend("/topic/driver",
                        this.decisionEngineService.getOwnRides(this.email));

            } catch (Exception exp) {
                log.debug(exp.getMessage());
            }

        }

    }
}
