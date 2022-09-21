package com.ideas2it.employee.dao.impl;

import com.ideas2it.employee.model.Trainer;
import com.ideas2it.employee.model.Role;
import com.ideas2it.employee.model.Qualification;
import com.ideas2it.employee.model.Employee;
import com.ideas2it.employee.dao.TrainerDao;

import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import org.hibernate.Session;    
import org.hibernate.SessionFactory;    
import org.hibernate.Transaction;    
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.cfg.Configuration;


/**
 * <h1> Trainee Dao </h1>
 * <p> 
 * It provides service for CRUD operations.
 * </P>
 * version 1.0
 * @author ruban 11/08/22
 **/
public class TrainerDaoImpl implements TrainerDao {
    private Session session;
    private SessionFactory sessionFactory;

    /**
     * <p>
     * This method will add trainer in database.
     * </p>
     * @param trainee object
     * @return return nothig
     **/
    public void insertTrainer(Trainer trainer) {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();  
            session = sessionFactory.openSession();  
            Transaction transaction = session.beginTransaction(); 
            Criteria roleCriteria = session.createCriteria(Role.class);
            roleCriteria.add(Restrictions.eq("description", trainer.getEmployee().getRole().getDescription()));
            List<Role> roleResult = roleCriteria.list();
            if (0 < roleResult.size()) {
                trainer.getEmployee().setRole(roleResult.get(0));
            }
            Criteria qualificationCriteria = session.createCriteria(Qualification.class);
            qualificationCriteria.add(Restrictions.eq("description", trainer.getEmployee().getQualification().getDescription()));
            List<Qualification> roleResults = qualificationCriteria.list();
            if (0 < roleResults.size()) {
	        trainer.getEmployee().setQualification(roleResults.get(0));
            }
            session.save(trainer);
            transaction.commit();
        } catch(Throwable e) {
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }
    }

    /**
     * <p>
     * This method will return trainer object.
     * </p>
     * @param no parameters
     * @return returns trainers from database.
     **/
    public List<Trainer> retrieveTrainer() {
        List<Trainer> trainerDetails = new ArrayList<Trainer>();
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();   
            session = sessionFactory.openSession();  
            Transaction transaction = session.beginTransaction(); 
            String query = "from Trainer";
            trainerDetails = session.createQuery(query).list();
            transaction.commit(); 
        } catch(Throwable e) {
            e.printStackTrace();  
        } finally { 
            session.close();
        }
        return trainerDetails;   
    }

    /**
     * <p>
     * This method delete tariner object by using employee id
     * </p>
     * @param {@link int} empId
     * @return returns boolean
     **/
    public boolean deleteTrainerById(int empId) {
        boolean isDeleted = false;
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
            session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Criteria criteria = session.createCriteria(Trainer.class);
            criteria.add(Restrictions.eq("employee.id", empId));
            List<Trainer> results = criteria.list();
            if (0 < results.size()) {
                session.remove(results.get(0));
                isDeleted = true;
            }
            transaction.commit();
        } catch(Throwable e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return isDeleted;
    }  

    /**
     * <p>
     * This method check the trainer Id and retrieve trainer object.
     * </p>
     * @param {@link int} empId
     * @return returns Trainer object
     **/
    public Trainer retrieveTrainerById(int empId) {
        Trainer trainer = null;
        try {
            List<Trainer> available = new ArrayList<Trainer>();
            sessionFactory = new Configuration().configure().buildSessionFactory();   
            session = sessionFactory.openSession();  
            Transaction transaction = session.beginTransaction(); 
            String query = "from Trainer where employee.id =" + empId;
            available = session.createQuery(query).list();
            transaction.commit();
            if (0 < available.size()) {            
                trainer = available.get(0);
            } 
        } catch(Throwable e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return trainer;
    } 
  
    /**
     * <p>
     * This method for update trainer. 
     * </p>
     * @param {@link Trainer} trainer
     * @returns It returns nothing 
     **/
    public void updateTrainer(Trainer trainer) {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();  
            session = sessionFactory.openSession();  
            Transaction transaction = session.beginTransaction();
            session.update(trainer);
            transaction.commit();
        } catch(Throwable e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }                        
}
        