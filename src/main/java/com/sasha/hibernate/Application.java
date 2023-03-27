package com.sasha.hibernate;

import com.sasha.hibernate.util.DeveloperApplicationGenerator;

public class Application {
    public static void main(String[] args) {
        Runnable developerApllication = new DeveloperApplicationGenerator();
        developerApllication.run();
    }
}
