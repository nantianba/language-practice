package com.nantianba.study.io;

public class 控制台输出样式 {
    public static void main(String[] args) {
        System.out.println("\033[1m粗体\033[0m");
        System.out.println("\033[2m无\033[0m");
        System.out.println("\033[3m无\033[0m");
        System.out.println("\033[4m下划线\033[0m");
        System.out.println("\033[5m闪烁\033[0m");
        System.out.println("\033[6m无\033[0m");
        System.out.println("\033[7m反显\033[0m");
        System.out.println("\033[8m消隐\033[0m");
        System.out.println("\033[9m无\033[0m");

        System.out.println("\033[30m黑\033[0m");
        System.out.println("\033[31m酱红\033[0m");
        System.out.println("\033[32m浅绿\033[0m");
        System.out.println("\033[33m黄褐\033[0m");
        System.out.println("\033[34m浅蓝\033[0m");
        System.out.println("\033[35m紫\033[0m");
        System.out.println("\033[36m天蓝\033[0m");
        System.out.println("\033[37m灰白\033[0m");

        System.out.println("\033[1;30m浅黑\033[0m");
        System.out.println("\033[1;31m红\033[0m");
        System.out.println("\033[1;32m绿\033[0m");
        System.out.println("\033[1;33m黄\033[0m");
        System.out.println("\033[1;34m蓝\033[0m");
        System.out.println("\033[1;35m粉红/洋红\033[0m");
        System.out.println("\033[1;36m青/蓝绿\033[0m");
        System.out.println("\033[1;37m白\033[0m");

        System.out.println("\033[40;37m 黑底白字\033[0m");
        System.out.println("\033[41;37m 紅底白字\033[0m");
        System.out.println("\033[42;37m 綠底白字\033[0m");
        System.out.println("\033[43;37m 黃底白字\033[0m");
        System.out.println("\033[44;37m 藍底白字\033[0m");
        System.out.println("\033[45;37m 紫底白字\033[0m");
        System.out.println("\033[46;37m 天藍底白字\033[0m");
        System.out.println("\033[47;30m 白底黑字\033[0m");
    }
}
