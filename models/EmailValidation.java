package models;

import interfaces.EmailValidationInterface;
import org.json.JSONObject;

/**
 *This class makes a connection to the API and then validates a given email
 * @author HBadr
 */
public class EmailValidation implements EmailValidationInterface {

    private final String key = "5a3ce41824msh1c5a64a78ca076dp14b7e4jsn08ee066a23b5";
    private final String web = "email-checker.p.rapidapi.com";
    private final String test;

    /**
     * Connects to the API and validates the email address.
     *
     * @param _email
     */
    public EmailValidation(String _email) {
        String url = "https://email-checker.p.rapidapi.com/verify/v1?email=" + _email;
        ConnectToApi api = new ConnectToApi(url, this.web, this.key);
        JSONObject results = api.getJsonObject();
        this.test = results.getString("status");
    }

    public EmailValidation() {
        this.test = "";
    }

    /**
     * Check if an email already exists in the database.
     * @param _email
     * @return
     */
    @Override
    public boolean isEmailInDatabase(String _email) {
        ConnectToDatabase conn = new ConnectToDatabase();
        boolean isGood = conn.emailExists(_email);
        conn.close();
        return isGood;
    }

    /**
     * Return username from associated email address.
     * @param _email
     * @return
     */
    @Override
    public String getAssociatedUsername(String _email) {
        ConnectToDatabase conn = new ConnectToDatabase();
        String assocEmail = conn.getUsernameFromEmail(_email);
        conn.close();
        return assocEmail;
    }

    //=================  GETTERS ===============//

    @Override
    public String getTest() {
        return this.test;
    }
}
