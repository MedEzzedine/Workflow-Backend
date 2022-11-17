package com.workflow.serviceTask;

import com.workflow.entity.Demande;
import com.workflow.entity.EmailDetails;
import com.workflow.repository.DemandeRepo;
import com.workflow.service.EmailService;
import lombok.AllArgsConstructor;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailTask2 {

    @Autowired
    private DemandeRepo demandeRepo;

    @Autowired
    private EmailService emailService;


    public void sendMail(DelegateExecution execution) {

        boolean isAccepted = (boolean) execution.getVariable("accept");
        Demande d = demandeRepo.findById((Integer) execution.getVariable("iddemande")).orElse(null);

        EmailDetails mail = new EmailDetails();
        mail.setRecipient(d.getOwner().getEmail());
        if (isAccepted) {
            mail.setSubject("Acceptation de votre demande de congé");
            mail.setMsgBody(
                    "Madame, Monsieur\n" +
                            "Nous avons pris connaissance de votre demande de congé pour " +
                            "création ou pour reprise d'entreprise en date du "+ d.getDate() + " et nous avons le plaisir de vous donner notre accord " +
                            "pour ce congé qui durera du "+ d.getDateDebut() +" au "+ d.getDateFin() + "\n" +
                            "Cordialement.");

        } else {
            mail.setSubject("Refus suite à votre demande de congé");
            mail.setMsgBody(
                    "Madame, Monsieur\n" +
                            "Vous avez sollicité le bénéfice d'un congé d'une durée de " + d.getDuree() + " jours." + "\n" + //Todo: calculate the duration
                            "Nous vous informons, par la présente, que nous ne pouvons accéder à votre demande.\n" +
                            //"En effet, (explications)\n" +
                            "Nous vous prions d'agréer, Madame/Monsieur, l'expression de notre considération distinguée.\n" +
                            "Cordialement.");
        }

        emailService.sendSimpleMail(mail);
    }
}