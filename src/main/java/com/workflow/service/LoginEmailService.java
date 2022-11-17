package com.workflow.service;


import com.workflow.entity.EmailDetails;
import com.workflow.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginEmailService {

    @Autowired
    private EmailService emailService;

    public void sendLoginMail(User user, String rawPassword) {
        EmailDetails mail = new EmailDetails();
        mail.setRecipient(user.getEmail());
        mail.setSubject("Création de votre compte");
        mail.setMsgBody(
                "Monsieur, Madame\n" +
                "Afin de créer votre compte professionel au sein de notre entreprise, veuillez suivre ce lien:\n    http://localhost:4200/login\n" +
                "    Votre mot de passe: " + rawPassword + "\n\nCordialement."
        );
        //System.out.println("Should send now!!");
        emailService.sendSimpleMail(mail);
    }
}
