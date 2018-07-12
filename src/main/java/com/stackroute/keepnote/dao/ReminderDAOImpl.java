package com.stackroute.keepnote.dao;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.Reminder;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */
@Repository
@Transactional
public class ReminderDAOImpl implements ReminderDAO {
	
	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
    SessionFactory sessionFactory;
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
    @Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
    @Autowired
	public ReminderDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory=sessionFactory;
	}

	/*
	 * Create a new reminder
	 */

	public boolean createReminder(Reminder reminder) {
		Session session=sessionFactory.getCurrentSession();
		session.save(reminder);
		session.flush();
		return true;

	}
	
	/*
	 * Update an existing reminder
	 */

	public boolean updateReminder(Reminder reminder) {
		try {
			if(getReminderById(reminder.getReminderId())==null) {
				return false;
			} else {
				sessionFactory.getCurrentSession().clear();
			
				sessionFactory.getCurrentSession().update(reminder);
				sessionFactory.getCurrentSession().flush();;

				return true;
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReminderNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;


	}

	/*
	 * Remove an existing reminder
	 */
	
	public boolean deleteReminder(int reminderId) {
		try {
			Reminder reminder=getReminderById(reminderId);
			if(reminder!=null) {
				Session session=sessionFactory.getCurrentSession();
				session.delete(reminder);
				session.flush();
				return true;
			}
			
		} catch (ReminderNotFoundException e) {
			// TODO Auto-generated catch block
			
		}
		
      return false;

	}

	/*
	 * Retrieve details of a specific reminder
	 */
	
	public Reminder getReminderById(int reminderId) throws ReminderNotFoundException {
		Session session=sessionFactory.getCurrentSession();
		Reminder reminder = (Reminder)session.get(Reminder.class, reminderId);
		if(reminder==null) {
			throw new ReminderNotFoundException("Reminder not found");
		}
		session.flush();
		return reminder;

	}

	/*
	 * Retrieve details of all reminders by userId
	 */
	
	public List<Reminder> getAllReminderByUserId(String userId) {
		String hql = "FROM Reminder reminder Where reminder.reminderCreatedBy=:userId"; 
		Query query = getSessionFactory().getCurrentSession().createQuery(hql).setParameter("userId", userId);
		return query.getResultList();



	}

}
