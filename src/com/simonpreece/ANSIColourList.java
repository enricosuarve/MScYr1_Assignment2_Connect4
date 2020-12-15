package com.simonpreece;

public class ANSIColourList {

    private static String OS = System.getProperty("os.name").toLowerCase();
    public String Blue = "";
    public String Red = "";
    public String Yellow = "";
    public String White = "";
    public String BoldWhite = "";
    public Boolean osSupportsAnsiColours = false;

    public ANSIColourList() {
        String operatingSystem = "";
        if (OS.contains("win")) {
            operatingSystem = "Windows";
        }
        if (OS.contains("mac")) {
            operatingSystem = "Mac";
        }
        if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
            operatingSystem = "UNIX";
        }
        if (OS.contains("sunos")) {
            operatingSystem = "Solaris";
        }
        switch (operatingSystem) {
            case "UNIX":
            case "Mac":
            case "Windows": //todo - move this to the bottom before publishing as colors only work in the IDE
                Blue = "\033[0;34m";
                Red = "\033[0;31m";
                Yellow = "\033[0;33m";
                White = "\033[0;57m";
                BoldWhite = "\033[0;97m";
                osSupportsAnsiColours = true;
                break;
            //case "Windows":
            case "Solaris":
            default:
                //do nothing - leave colours blank as OS either does not reliably support ANSI colors or is unknown
        }

    }
}
