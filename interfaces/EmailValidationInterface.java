/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author HBadr
 */
public interface EmailValidationInterface {

    public boolean isEmailInDatabase(String _email);
    public String getAssociatedUsername(String _email);
    public String getTest();
}
