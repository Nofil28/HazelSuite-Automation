package com.hazelsuite.utilities;
import org.testng.Assert;

import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromTerm;

import static com.hazelsuite.utilities.CommonUtil.getEmailContentSubString;
import static com.hazelsuite.utilities.CommonUtil.getVerificationLinkSubString;

public class EmailUtil {
    public static Store emailAuthentication(String host, String username, String password) {
        Store store = null;
        try {
            Properties properties = new Properties();
            properties.setProperty("mail.store.protocol", "imaps");

            // Create a session
            Session session = Session.getDefaultInstance(properties, null);

            // Connect to the store
            store = session.getStore("imaps");
            store.connect(host, username, password);

        } catch (MessagingException e) {
            Log.error("Exception occurred: " + e.getMessage());
        }
        return store;
    }

    public static Message[] checkUnreadEmailSubject(Store store, String senderEmail) {
        Message[] messages = null;
        try {
            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_WRITE);

            // Search for unread emails from a specific sender
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false); // 'false' means unread


            // Create an Address object representing the sender
            Address senderAddress;

            senderAddress = new InternetAddress(senderEmail);

            FromTerm fromTerm = new FromTerm(senderAddress);
            // Combine the search criteria
            AndTerm combinedTerm = new AndTerm(unseenFlagTerm, fromTerm);
            messages = inbox.search(combinedTerm);


            // Mark the found messages as seen (read)
            for (Message message : messages) {
                message.setFlag(Flags.Flag.SEEN, true);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static String extractVerificationLinkFromBody(Message[] messages) throws MessagingException, IOException {
        Object content = messages[0].getContent();
        String verificationLink = null;

        if (content instanceof String) {
            // Handle plain text content
            String textContent = (String) content;
            verificationLink = getVerificationLinkSubString(textContent);
        }
        return verificationLink;
    }

    public static String extractEmailContentFromBody(Message[] messages) throws MessagingException, IOException {
        Object content = messages[0].getContent();
        String emailContent = null;

        if (content instanceof String) {
            // Handle plain text content
            String textContent = (String) content;
            emailContent = getEmailContentSubString(textContent);
        }
        return emailContent;
    }

    public static void archiveEmail(Message[] messages, Store store) {
        try {
            Folder archiveFolder = store.getFolder("Archives"); // Specify the name of your archive folder
            archiveFolder.open(Folder.READ_WRITE);

            for (Message message : messages) {
                // Copy the message to the archive folder
                message.getFolder().copyMessages(new Message[]{message}, archiveFolder);

                // Mark the original message for deletion
                message.setFlag(Flags.Flag.DELETED, true);
            }
        } catch (MessagingException e) {
            Log.error("Exception occurred while archiving emails: " + e.getMessage());
        }
    }

}