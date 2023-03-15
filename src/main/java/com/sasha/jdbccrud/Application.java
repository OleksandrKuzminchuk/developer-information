package com.sasha.jdbccrud;

import com.sasha.jdbccrud.util.DeveloperApplicationGenerator;

public class Application {
    public static void main(String[] args) {
        Runnable developerApllication = new DeveloperApplicationGenerator();
        developerApllication.run();
    }
}
