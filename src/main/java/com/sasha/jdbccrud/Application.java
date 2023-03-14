package com.sasha.jdbccrud;

import com.sasha.jdbccrud.util.DeveloperApllicationGenerator;

public class Application {
    public static void main(String[] args) {
        Runnable developerApllication = new DeveloperApllicationGenerator();
        developerApllication.run();
    }
}
