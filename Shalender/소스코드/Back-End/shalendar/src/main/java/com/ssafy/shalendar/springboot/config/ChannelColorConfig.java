package com.ssafy.shalendar.springboot.config;

public class ChannelColorConfig {

    private String[][] colorTable={{"#f44336", "#ef9a9a"}, {"#e91e63", "#f48fb1"}, {"#9c27b0", "#ce93d8"}, {"#673ab7", "#b39ddb"}, {"#3f51b5", "#9fa8da"}, {"#2196f3", "#90caf9"}, {"#03a9f4", "#81d4fa"}, {"#00bcd4", "#80deea"}, {"#009688", "#80cbc4"}, {"#4caf50", "#a5d6a7"}, {"#8bc34a", "#c5e1a5"}, {"#cddc39", "#e6ee9c"}, {"#ffeb3b", "#fff59d"}, {"#ffc107", "#ffe082"}, {"#ff9800", "#ffcc80"}, {"#ff5722", "#ffab91"}, {"#795548", "#bcaaa4"}, {"#9e9e9e", "#eeeeee"}, {"#607d8b", "#b0bec5"}};

    public String[] getColor(int ch_no){
        return colorTable[ch_no];
    }
}
