package com.sasha.hibernate.util;

import com.sasha.hibernate.pojo.Skill;
import com.sasha.hibernate.pojo.Specialty;
import com.sasha.hibernate.util.constant.Constants;
import com.sasha.hibernate.exception.NotFoundException;
import com.sasha.hibernate.pojo.Developer;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.InputStream;
import java.util.Properties;

import static com.sasha.hibernate.util.constant.Constants.FAILED_DATABASE_CONNECTION;

public class HibernateUtil {
    private static HibernateUtil instance;
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();
    private HibernateUtil() {}
    public static HibernateUtil getInstance() {
        if (instance == null) {
            synchronized(HibernateUtil.class) {
                if (instance == null) {
                    instance = new HibernateUtil();
                }
            }
        }
        return instance;
    }

    private static SessionFactory buildSessionFactory() {
        try(InputStream input = HibernateUtil.class.getClassLoader().getResourceAsStream(Constants.HIBERNATE_PROPERTIES)){
            Properties properties = new Properties();
            properties.load(input);
            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(properties)
                    .build();
            Metadata metadata = new MetadataSources(registry)
                    .addAnnotatedClass(Developer.class)
                    .addAnnotatedClass(Specialty.class)
                    .addAnnotatedClass(Skill.class)
                    .buildMetadata();
            return metadata.getSessionFactoryBuilder().build();
        } catch (Exception e){
            throw new NotFoundException(FAILED_DATABASE_CONNECTION, e);
        }
    }

    public SessionFactory getSessionFactory(){
        return SESSION_FACTORY;
    }
}

