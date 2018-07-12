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
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Note;

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
public class NoteDAOImpl implements NoteDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */
    SessionFactory sessionFactory;
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Autowired
	public NoteDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory=sessionFactory;
	}

	/*
	 * Create a new note
	 */
	
	public boolean createNote(Note note) {
		Session session=sessionFactory.getCurrentSession();
		session.save(note);
		session.flush();
		return true;

	}

	/*
	 * Remove an existing note
	 */
	
	public boolean deleteNote(int noteId) {
		try {
			Note note=getNoteById(noteId);
			if(note!=null) {
				Session session=sessionFactory.getCurrentSession();
				session.delete(note);
				session.flush();
				return true;
			}
			
		} catch (NoteNotFoundException e) {
			// TODO Auto-generated catch block
			
		}
		
      return false;
	}

	/*
	 * Retrieve details of all notes by userId
	 */
	
	public List<Note> getAllNotesByUserId(String userId) {
		String hql = "FROM Note note Where note.createdBy=:userId"; 
		Query query = getSessionFactory().getCurrentSession().createQuery(hql).setParameter("userId", userId);
		return query.getResultList();

	}

	/*
	 * Retrieve details of a specific note
	 */
	
	public Note getNoteById(int noteId) throws NoteNotFoundException {
		Session session=sessionFactory.getCurrentSession();
		Note note = (Note)session.get(Note.class, noteId);
		if(note==null) {
			throw new NoteNotFoundException("Note not found");
		}
		session.flush();
		return note;

	}

	/*
	 * Update an existing note
	 */

	public boolean UpdateNote(Note note) {
		try {
			if(getNoteById(note.getNoteId())==null) {
				return false;
			} else {
				sessionFactory.getCurrentSession().clear();
			
				sessionFactory.getCurrentSession().update(note);
				sessionFactory.getCurrentSession().flush();;

				return true;
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoteNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;


	}

}
