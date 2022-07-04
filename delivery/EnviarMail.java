package app.elingeniero.atupuerta.delivery;

import android.os.AsyncTask;
import android.util.Log;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EnviarMail extends AsyncTask<EnviarMail.Mail,Void,Void> {

    // Propiedades del cliente de correo
    private static Session session;         // Sesion de correo
    private static Properties properties;   // Propiedades de la sesion
    private static Transport transport;     // Envio del correo
    private static MimeMessage mensaje;     // Mensaje que enviaremos

    // Credenciales de usuario
    private static String direccionCorreo = "1homedelivery1@gmail.com";   // Dirección de correo
    private static String contrasenyaCorreo = "alpadel8";

    // Correo al que enviaremos el mensaje
    private static String destintatarioCorreo = "victorh.alonzo@gmail.com";

    public static void init()  {

        // Ajustamos primero las properties
        properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        //Configuramos la sesión
        session = Session.getDefaultInstance(properties, null);

        //enviarMensaje("Hola Dionis","Prueba cliente correo, buen fin de semana. Sergi Barola");
    }

    protected Void doInBackground(Mail... mails) {
        init();
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getDefaultInstance(properties, null);

        for (Mail mail:mails) {

            try {
                /*
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mail.from));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(mail.to));
                message.setSubject(mail.subject);
                message.setText(mail.content);

                Transport.send(message);
                */
                mensaje = new MimeMessage(session);
                //mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destintatarioCorreo));
                mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.to));
                mensaje.setSubject(mail.subject);
                mensaje.setContent(mail.content, "text/html");

                // Configuramos como sera el envio del correo
                transport = session.getTransport("smtp");
                //transport.connect("smtp.gmail.com", direccionCorreo, contrasenyaCorreo);
                transport.connect("smtp.gmail.com", mail.dest, mail.pwd);
                transport.sendMessage(mensaje, mensaje.getAllRecipients());
                transport.close();

            } catch (MessagingException e) {
                Log.d("MailJob", e.getMessage());
            }
        }
        return null;
    }

    public static class Mail{
        private final String subject;
        private final String content;
        private final String dest;
        private final String pwd;
        private final String to;

        public Mail(String dest,String pwd, String to, String subject, String content){
            this.subject=subject;
            this.content=content;
            this.dest=dest;
            this.pwd=pwd;
            this.to=to;
        }
    }

    public static void enviarMensaje(String subject, String content)  {

        init();
        // Configuramos los valores de nuestro mensaje
        try {
            mensaje = new MimeMessage(session);
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destintatarioCorreo));
            mensaje.setSubject(subject);
            mensaje.setContent(content, "text/html");

            // Configuramos como sera el envio del correo
            transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", direccionCorreo, contrasenyaCorreo);
            transport.sendMessage(mensaje, mensaje.getAllRecipients());
            transport.close();

            // Mostramos que el mensaje se ha enviado correctamente
            System.out.println("--------------------------");
            System.out.println("Mensaje enviado");
            System.out.println("---------------------------");
        }catch (MessagingException me){

        }
    }
}
