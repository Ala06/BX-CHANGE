package atlantafx.sampler.services.serviceImpl;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class EmailUtil {

    private static final String MJ_APIKEY_PUBLIC = "ddb067428c5ea61e17c65fe5c793ea4a";
    private static final String MJ_APIKEY_PRIVATE = "d9907645444051b5649c879c5e5fd4db";

    public static void sendEmail(String toAddress, String subject, String message) throws IOException {
        MailjetClient client = new MailjetClient(MJ_APIKEY_PUBLIC, MJ_APIKEY_PRIVATE);
        MailjetRequest request;
        MailjetResponse response;

        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", "alaa.benabada@gmail.com")
                                        .put("Name", "BX-CHANGE"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", toAddress)
                                                .put("Name", "Recipient Name")))
                                .put(Emailv31.Message.SUBJECT, subject)
                                .put(Emailv31.Message.TEXTPART, message)
                                .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));

        try {
            response = client.post(request);
            System.out.println(response.getStatus());
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to send email", e);
        }
    }
}
