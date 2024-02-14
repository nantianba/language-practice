package com.nantianba.study.feature.jdk21;

import static com.nantianba.study.feature.jdk21.str.Templates.$;
import static com.nantianba.study.feature.jdk21.str.Templates.JSON;
import static java.lang.StringTemplate.RAW;
import static java.util.FormatProcessor.FMT;

public class String增强 {
    final static String name = "Joan";


    public static void main(String[] args) {
        int x = 1;
        int y = 2;

        String s = String.format("%2$d plus %1$d equals %3$d", x, y, x + y);
        String t = "%2$d plus %1$d equals %3$d".formatted(x, y, x + y);//JDK15

        System.out.println(s);
        System.out.println(t);

        String info = STR."My name is \{name}";//JDK21
        System.out.println(info);

// Embedded expressions can be strings
        String firstName = "Bill";
        String lastName = "Duck";
        System.out.println($."\{firstName} \{lastName}");
        System.out.println($."\{lastName}, \{firstName}");

// Embedded expressions can perform arithmetic
        s = $."\{x} + \{y} = \{x + y}";
        System.out.println(s);

// Embedded expressions can invoke methods and access fields
        s = $."You have a \{getOfferType()} waiting for you!";
        Request req = new Request("2021-09-01", "12:00", "10.0.0.1");
        t = $."Access at \{req.date} \{req.time} from \{req.ipAddress}";
        System.out.println(s);
        System.out.println(t);

        String title = "My Web Page";
        String text = "Hello, world";
        String html = $."""
        <html>
          <head>
            <title>\{title}</title>
          </head>
          <body>
            <p>\{text}</p>
          </body>
        </html>
        """;
        System.out.println(html);

        String name = "Joan Smith";
        String phone = "555-123-4567";
        String address = "1 Maple Drive, Anytown";
        String json = $."""
    {
        "name":    "\{name}",
        "phone":   "\{phone}",
        "address": "\{address}"
    }
    """;

        System.out.println(json);

        System.out.println(RAW."\{x} plus \{y} equals \{x + y}");
        System.out.println(FMT."%.2f\{Math.PI}");

        //自定义字符串模版处理
        System.out.println(JSON."""
        {
            "json_obj":\{req}
        }
        """);

    }

    private static String getOfferType() {
        return "offer";
    }

    private record Request(String date, String time, String ipAddress) {
    }


}
