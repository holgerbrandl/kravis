package com.github.holgerbrandl.kravis.scratch;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


/**
 * @author Holger Brandl
 */
public class JavaExample {

    public static void main(String[] args) {
        List<User> users = Arrays.asList(
                new User("Max", LocalDate.parse("2007-12-03"), "M", 1.89),
                new User("Jane", LocalDate.parse("1980-07-03"), "F", 1.67),
                new User("Anna", LocalDate.parse("1992-07-03"), "F", 1.32)
        );

//        plotOf(users, specBuilder ->{}
//                specBuilder.encoding(x,  ){ birthDay.year }
//        )
    }
}
