package com.project.gtps.dao.impl;

import com.project.gtps.dao.GenericDao;
import com.project.gtps.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/*@SuppressWarnings("unchecked")
@Repository*/
@Transactional
public abstract class GenericDaoImpl<T> implements GenericDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected Class<T> daoType;

    public void setDaoType(Class<T> type) {
        daoType = type;
    }

    @Override
    public void save(T entity) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void delete(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public void delete(Long id) {
        T entity = findOne(id);
        delete(entity);
    }

    @Override
    public T findOne(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return (T) session.get(daoType, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        System.out.println("listing dao impl---before------add" + daoType);
        Session session = HibernateUtil.getSessionFactory().openSession();
        System.out.println("listing dao impl----after-----add" + daoType);
        session.beginTransaction();

        return session.createQuery("from " + daoType.getName()).list();
    }

    @Override
    public T update(T entity) {


        System.out.println("----Updating Entity-----");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {

            transaction = session.beginTransaction();
            System.out.println("Merging Entity....");
            session.merge(entity);
            System.out.println("Updated User:\n" + entity);
            transaction.commit();
            System.out.println("Commit successful");

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            session.close();
        }

        return null;

    }

}