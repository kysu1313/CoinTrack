
package controllers.assistantControllers;

/**
 * Changeable theme class.
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

    /**
     * Return current theme
     * @return
     */
    public String getTheme() {
        return this.theme;
    }

}
