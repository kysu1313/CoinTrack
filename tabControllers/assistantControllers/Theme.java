/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabControllers.assistantControllers;

/**
 *
 * @author Kyle
 */
public class Theme {
    
    private final String DARK = "styles/DarkTheme.css";
    private final String LIGHT = "styles/LightTheme.css";
    private String theme;
    
    public Theme(String _theme) {
        if (_theme.toLowerCase().equalsIgnoreCase("dark")){
            this.theme = DARK;
        } else if (_theme.toLowerCase().equalsIgnoreCase("light")) {
            this.theme = LIGHT;
        }
    }
    
    public String getTheme() {
        return this.theme;
    }
    
}
