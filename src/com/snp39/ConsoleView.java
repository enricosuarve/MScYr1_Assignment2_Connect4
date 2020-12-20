package com.snp39;

public class ConsoleView implements View {
    /**
     * Output data in String form to the java console.
     *
     * @param displayString - String to be displayed
     */
    @Override
    public void Display(String displayString) {
        System.out.println(displayString);
    }

}
