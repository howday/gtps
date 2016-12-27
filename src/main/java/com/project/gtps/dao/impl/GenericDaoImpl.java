package com.project.gtps.dao.impl;

import com.project.gtps.dao.GenericDao;
import com.project.gtps.util.HibernateUtil;
import org.hibernate.HibernateException;
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

        System.out.println("Inside dao impl---before------add");
        Session session = HibernateUtil.getSessionFactory().openSession();
        System.out.println("Inside dao impl----after-----add");
        session.beginTransaction();

        System.out.println(entity);
        session.save(entity);
        session.getTransaction().commit();
//        HibernateUtil.shutdown();
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

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            System.out.println("updated :" + entity);
            session.merge(entity);
            System.out.println("updated------------ :" + entity);
            tx.commit();

            return null;
        } catch (HibernateException ex) {
            ex.printStackTrace();
            return null;
        }

    }

}