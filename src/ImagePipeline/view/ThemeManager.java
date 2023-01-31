package ImagePipeline.view;

import java.util.ArrayList;
import java.util.logging.Logger;

import java.awt.Window;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme;
import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGradiantoDarkFuchsiaIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGradiantoDeepOceanIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGradiantoMidnightBlueIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGradiantoNatureGreenIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkHardIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkMediumIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkSoftIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatHiberbeeDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatHighContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatMaterialDesignDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatMonocaiIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatMonokaiProIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatSpacegrayIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatVuesionIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatXcodeDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatDraculaContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatLightOwlIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatLightOwlContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialOceanicIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialOceanicContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialPalenightIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialPalenightContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMonokaiProContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatNightOwlIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatNightOwlContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedLightContrastIJTheme;

import ImagePipeline.util.Common;

import com.formdev.flatlaf.IntelliJTheme.ThemeLaf;

public class ThemeManager {
    protected Logger _logger;
    private ArrayList<String> _themes;

    public ThemeManager() {
        _logger = Logger.getLogger(getClass().getName());
        _logger.setLevel(Common.GLOBAL_LOG_LEVEL);
        _themes = new ArrayList<String>();

        _themes.add("ArcIJTheme");
        _themes.add("ArcOrangeIJTheme");
        _themes.add("ArcDarkIJTheme");
        _themes.add("ArcDarkOrangeIJTheme");
        _themes.add("CarbonIJTheme");
        _themes.add("Cobalt2IJTheme");
        _themes.add("CyanLightIJTheme");
        _themes.add("DarkFlatIJTheme");
        _themes.add("DarkPurpleIJTheme");
        _themes.add("DraculaIJTheme");
        _themes.add("GradiantoDarkFuchsiaIJTheme");
        _themes.add("GradiantoDeepOceanIJTheme");
        _themes.add("GradiantoMidnightBlueIJTheme");
        _themes.add("GradiantoNatureGreenIJTheme");
        _themes.add("GrayIJTheme");
        _themes.add("GruvboxDarkHardIJTheme");
        _themes.add("GruvboxDarkMediumIJTheme");
        _themes.add("GruvboxDarkSoftIJTheme");
        _themes.add("HiberbeeDarkIJTheme");
        _themes.add("HighContrastIJTheme");
        _themes.add("LightFlatIJTheme");
        _themes.add("MaterialDesignDarkIJTheme");
        _themes.add("MonocaiIJTheme");
        _themes.add("MonokaiProIJTheme");
        _themes.add("NordIJTheme");
        _themes.add("OneDarkIJTheme");
        _themes.add("SolarizedDarkIJTheme");
        _themes.add("SolarizedLightIJTheme");
        _themes.add("SpacegrayIJTheme");
        _themes.add("VuesionIJTheme");
        _themes.add("XcodeDarkIJTheme");
        _themes.add("ArcDarkContrastIJTheme");
        _themes.add("AtomOneDarkIJTheme");
        _themes.add("AtomOneDarkContrastIJTheme");
        _themes.add("AtomOneLightIJTheme");
        _themes.add("AtomOneLightContrastIJTheme");
        _themes.add("DraculaContrastIJTheme");
        _themes.add("GitHubIJTheme");
        _themes.add("GitHubContrastIJTheme");
        _themes.add("GitHubDarkIJTheme");
        _themes.add("GitHubDarkContrastIJTheme");
        _themes.add("LightOwlIJTheme");
        _themes.add("LightOwlContrastIJTheme");
        _themes.add("MaterialDarkerIJTheme");
        _themes.add("MaterialDarkerContrastIJTheme");
        _themes.add("MaterialDeepOceanIJTheme");
        _themes.add("MaterialDeepOceanContrastIJTheme");
        _themes.add("MaterialLighterIJTheme");
        _themes.add("MaterialLighterContrastIJTheme");
        _themes.add("MaterialOceanicIJTheme");
        _themes.add("MaterialOceanicContrastIJTheme");
        _themes.add("MaterialPalenightIJTheme");
        _themes.add("MaterialPalenightContrastIJTheme");
        _themes.add("MonokaiProContrastIJTheme");
        _themes.add("MoonlightIJTheme");
        _themes.add("MoonlightContrastIJTheme");
        _themes.add("NightOwlIJTheme");
        _themes.add("NightOwlContrastIJTheme");
        _themes.add("SolarizedDarkContrastIJTheme");
        _themes.add("SolarizedLightContrastIJTheme");
    }

    public ArrayList<String> getThemes() {
        return _themes;
    }

    public void setTheme(String strTheme) {
        ThemeLaf theme = null;

        if (strTheme.equalsIgnoreCase("ArcIJTheme")) {
            theme = new FlatArcIJTheme();
        } else if (strTheme.equalsIgnoreCase("ArcOrangeIJTheme")) {
            theme = new FlatArcOrangeIJTheme();
        } else if (strTheme.equalsIgnoreCase("ArcDarkIJTheme")) {
            theme = new FlatArcDarkIJTheme();
        } else if (strTheme.equalsIgnoreCase("ArcDarkOrangeIJTheme")) {
            theme = new FlatArcDarkOrangeIJTheme();
        } else if (strTheme.equalsIgnoreCase("CarbonIJTheme")) {
            theme = new FlatCarbonIJTheme();
        } else if (strTheme.equalsIgnoreCase("Cobalt2IJTheme")) {
            theme = new FlatCobalt2IJTheme();
        } else if (strTheme.equalsIgnoreCase("CyanLightIJTheme")) {
            theme = new FlatCyanLightIJTheme();
        } else if (strTheme.equalsIgnoreCase("DarkFlatIJTheme")) {
            theme = new FlatDarkFlatIJTheme();
        } else if (strTheme.equalsIgnoreCase("DarkPurpleIJTheme")) {
            theme = new FlatDarkPurpleIJTheme();
        } else if (strTheme.equalsIgnoreCase("DraculaIJTheme")) {
            theme = new FlatDraculaIJTheme();
        } else if (strTheme.equalsIgnoreCase("GradiantoDarkFuchsiaIJTheme")) {
            theme = new FlatGradiantoDarkFuchsiaIJTheme();
        } else if (strTheme.equalsIgnoreCase("GradiantoDeepOceanIJTheme")) {
            theme = new FlatGradiantoDeepOceanIJTheme();
        } else if (strTheme.equalsIgnoreCase("GradiantoMidnightBlueIJTheme")) {
            theme = new FlatGradiantoMidnightBlueIJTheme();
        } else if (strTheme.equalsIgnoreCase("GradiantoNatureGreenIJTheme")) {
            theme = new FlatGradiantoNatureGreenIJTheme();
        } else if (strTheme.equalsIgnoreCase("GrayIJTheme")) {
            theme = new FlatGrayIJTheme();
        } else if (strTheme.equalsIgnoreCase("GruvboxDarkHardIJTheme")) {
            theme = new FlatGruvboxDarkHardIJTheme();
        } else if (strTheme.equalsIgnoreCase("GruvboxDarkMediumIJTheme")) {
            theme = new FlatGruvboxDarkMediumIJTheme();
        } else if (strTheme.equalsIgnoreCase("GruvboxDarkSoftIJTheme")) {
            theme = new FlatGruvboxDarkSoftIJTheme();
        } else if (strTheme.equalsIgnoreCase("HiberbeeDarkIJTheme")) {
            theme = new FlatHiberbeeDarkIJTheme();
        } else if (strTheme.equalsIgnoreCase("HighContrastIJTheme")) {
            theme = new FlatHighContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("LightFlatIJTheme")) {
            theme = new FlatLightFlatIJTheme();
        } else if (strTheme.equalsIgnoreCase("MaterialDesignDarkIJTheme")) {
            theme = new FlatMaterialDesignDarkIJTheme();
        } else if (strTheme.equalsIgnoreCase("MonocaiIJTheme")) {
            theme = new FlatMonocaiIJTheme();
        } else if (strTheme.equalsIgnoreCase("MonokaiProIJTheme")) {
            theme = new FlatMonokaiProIJTheme();
        } else if (strTheme.equalsIgnoreCase("NordIJTheme")) {
            theme = new FlatNordIJTheme();
        } else if (strTheme.equalsIgnoreCase("OneDarkIJTheme")) {
            theme = new FlatOneDarkIJTheme();
        } else if (strTheme.equalsIgnoreCase("SolarizedDarkIJTheme")) {
            theme = new FlatSolarizedDarkIJTheme();
        } else if (strTheme.equalsIgnoreCase("SolarizedLightIJTheme")) {
            theme = new FlatSolarizedLightIJTheme();
        } else if (strTheme.equalsIgnoreCase("SpacegrayIJTheme")) {
            theme = new FlatSpacegrayIJTheme();
        } else if (strTheme.equalsIgnoreCase("VuesionIJTheme")) {
            theme = new FlatVuesionIJTheme();
        } else if (strTheme.equalsIgnoreCase("XcodeDarkIJTheme")) {
            theme = new FlatXcodeDarkIJTheme();
        } else if (strTheme.equalsIgnoreCase("ArcDarkContrastIJTheme")) {
            theme = new FlatArcDarkContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("AtomOneDarkIJTheme")) {
            theme = new FlatAtomOneDarkIJTheme();
        } else if (strTheme.equalsIgnoreCase("AtomOneDarkContrastIJTheme")) {
            theme = new FlatAtomOneDarkContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("AtomOneLightIJTheme")) {
            theme = new FlatAtomOneLightIJTheme();
        } else if (strTheme.equalsIgnoreCase("AtomOneLightContrastIJTheme")) {
            theme = new FlatAtomOneLightContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("DraculaContrastIJTheme")) {
            theme = new FlatDraculaContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("GitHubIJTheme")) {
            theme = new FlatGitHubIJTheme();
        } else if (strTheme.equalsIgnoreCase("GitHubContrastIJTheme")) {
            theme = new FlatGitHubContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("GitHubDarkIJTheme")) {
            theme = new FlatGitHubDarkIJTheme();
        } else if (strTheme.equalsIgnoreCase("GitHubDarkContrastIJTheme")) {
            theme = new FlatGitHubDarkContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("LightOwlIJTheme")) {
            theme = new FlatLightOwlIJTheme();
        } else if (strTheme.equalsIgnoreCase("LightOwlContrastIJTheme")) {
            theme = new FlatLightOwlContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("MaterialDarkerIJTheme")) {
            theme = new FlatMaterialDarkerIJTheme();
        } else if (strTheme.equalsIgnoreCase("MaterialDarkerContrastIJTheme")) {
            theme = new FlatMaterialDarkerContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("MaterialDeepOceanIJTheme")) {
            theme = new FlatMaterialDeepOceanIJTheme();
        } else if (strTheme.equalsIgnoreCase("MaterialDeepOceanContrastIJTheme")) {
            theme = new FlatMaterialDeepOceanContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("MaterialLighterIJTheme")) {
            theme = new FlatMaterialLighterIJTheme();
        } else if (strTheme.equalsIgnoreCase("MaterialLighterContrastIJTheme")) {
            theme = new FlatMaterialLighterContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("MaterialOceanicIJTheme")) {
            theme = new FlatMaterialOceanicIJTheme();
        } else if (strTheme.equalsIgnoreCase("MaterialOceanicContrastIJTheme")) {
            theme = new FlatMaterialOceanicContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("MaterialPalenightIJTheme")) {
            theme = new FlatMaterialPalenightIJTheme();
        } else if (strTheme.equalsIgnoreCase("MaterialPalenightContrastIJTheme")) {
            theme = new FlatMaterialPalenightContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("MonokaiProContrastIJTheme")) {
            theme = new FlatMonokaiProContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("MoonlightIJTheme")) {
            theme = new FlatMoonlightIJTheme();
        } else if (strTheme.equalsIgnoreCase("MoonlightContrastIJTheme")) {
            theme = new FlatMoonlightContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("NightOwlIJTheme")) {
            theme = new FlatNightOwlIJTheme();
        } else if (strTheme.equalsIgnoreCase("NightOwlContrastIJTheme")) {
            theme = new FlatNightOwlContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("SolarizedDarkContrastIJTheme")) {
            theme = new FlatSolarizedDarkContrastIJTheme();
        } else if (strTheme.equalsIgnoreCase("SolarizedLightContrastIJTheme")) {
            theme = new FlatSolarizedLightContrastIJTheme();
        }

        if (theme != null) {
            try {
                _logger.info("Set theme: " + strTheme);
                UIManager.setLookAndFeel(theme);

                Window[] windows = Window.getWindows();
                if (windows.length > 0) {
                    for (Window window : windows) {
                        SwingUtilities.updateComponentTreeUI(window);
                        window.pack();
                    }
                }
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        }
    }

}
