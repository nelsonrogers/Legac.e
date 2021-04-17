package testament.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import java.io.IOException;

@Controller
public class emailController {

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/email")
    public String email() {
        return "espaceUtilisateur";
    }

    @PostMapping("/envoiEmailInscription")
    public String envoiMail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("lucasabitbol6@gmail.com", "lucasabitbol6@gmail.com");

        msg.setSubject("Bienvenue sur Legac.e");
        msg.setText("Merci de vous identifier sur notre site : \nhttps://legace.com");

        javaMailSender.send(msg);
        return "espaceUtilisateur";
    }




    @PostMapping("/envoiEmailTestament")
    public String envoiTestament() throws MessagingException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo("lucasabitbol6@gmail.com");

        helper.setSubject("J'envoie ce mail depuis Java");

        // default = text/plain
        //helper.setText("Check attachment for image!");

        // true = text/html
        helper.setText("<h1>Ca fonctionne ?</h1>", true);

        //FileSystemResource file = new FileSystemResource(new File("classpath:android.png"));

        //Resource resource = new ClassPathResource("android.png");
        //InputStream input = resource.getInputStream();

        //ResourceUtils.getFile("classpath:android.png");

        helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

        javaMailSender.send(msg);
        return "espaceUtilisateur";

    }


}
