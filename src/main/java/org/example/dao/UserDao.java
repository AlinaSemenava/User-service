package org.example.dao;

import org.example.model.*;
import org.hibernate.*;
import org.hibernate.cfg.*;

import java.util.*;

public class UserDao {

    public static void create(String name){
        SessionFactory sessionFactory = null;
        Session session = null;

        try {
            // Конфигурация Hibernate
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml") // Путь к файлу конфигурации
                    .buildSessionFactory();

            session = sessionFactory.openSession();
            session.beginTransaction();

            // Пример создания объекта
            User user = new User();
            user.setName(name);
            session.save(user);

            session.getTransaction().commit();
            System.out.println("User saved successfully!");

        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }

    public static User read(String name){

        Transaction tx = null;
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml") // Путь к файлу конфигурации
                .buildSessionFactory();

        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            String hql = "from User u where u.name = :name";
            User user = session.createQuery(hql, User.class)
                    .setParameter("name", name)
                    .uniqueResultOptional()
                    .orElse(null);
            tx.commit();
            return user;
        } catch (RuntimeException e) {
            if (tx != null && tx.getStatus().canRollback()) {
                try { tx.rollback(); } catch (Exception ignore) {}
            }
            throw e;
        }
    }

    public static int update(String name, String email){
        Transaction tx = null;
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml") // Путь к файлу конфигурации
                .buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            String hqlUpdate = "update User u set u.email = :email where u.name = :name";
            int updated = session.createQuery(hqlUpdate)
                    .setParameter("email", email)
                    .setParameter("name", name)
                    .executeUpdate();
            tx.commit();
            return updated;
        } catch (RuntimeException e) {
            if (tx != null && tx.getStatus().canRollback()) {
                try { tx.rollback(); } catch (Exception ignore) {}
            }
            throw e;
        }
    }

    public static boolean delete(String name){
        Transaction tx = null;
        SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml") // Путь к файлу конфигурации
                .buildSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            String hqlSelect = "from User u where u.name = :name";
            User user = session.createQuery(hqlSelect, User.class)
                    .setParameter("name", name)
                    .setMaxResults(1)
                    .uniqueResultOptional()
                    .orElse(null);
            if (user == null) {
                tx.commit();
                return false;
            }
            session.delete(user);
            tx.commit();
            return true;
        } catch (RuntimeException e) {
            if (tx != null && tx.getStatus().canRollback()) {
                try { tx.rollback(); } catch (Exception ignore) {}
            }
            throw e;
        }
    }
}
