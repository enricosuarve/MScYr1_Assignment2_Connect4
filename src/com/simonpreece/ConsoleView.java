package com.simonpreece;

public class ConsoleView implements View {
    @Override
    public void Display(String displayString) {
        System.out.println(displayString);
    }

}
