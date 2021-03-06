package com.stackroute.keepnote.service;

import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.dao.CategoryDAO;
import com.stackroute.keepnote.dao.NoteDAO;
import com.stackroute.keepnote.dao.ReminderDAO;
import com.stackroute.keepnote.dao.UserDAO;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.model.User;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn�t currently 
* provide any additional behavior over the @Component annotation, but it�s a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class NoteServiceImpl implements NoteService {

	/*
	 * Autowiring should be implemented for the NoteDAO,CategoryDAO,ReminderDAO.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */

	@Autowired
	  CategoryDAO categoryDAO;
	@Autowired
	  NoteDAO noteDAO;
	@Autowired
	  ReminderDAO reminderDAO;
//	    @Autowired
//		public NoteServiceImpl( CategoryDAO categoryDAO,NoteDAO noteDAO,ReminderDAO reminderDAO) {
//	    	  this.noteDAO=noteDAO;
//			  this.categoryDAO=categoryDAO;
//			  this.reminderDAO=reminderDAO;
//		}
		/*
		 * This method should be used to save a new note.
		 */
	public boolean createNote(Note note) throws ReminderNotFoundException, CategoryNotFoundException {
		
			 Reminder reminder=note.getReminder();
			 Category category=note.getCategory();
			 if(reminder!=null) {
				 try {
				 reminderDAO.getReminderById(reminder.getReminderId());
				 }catch(ReminderNotFoundException r) {
					 throw new ReminderNotFoundException("reminder is not there");
				 }
			 }
			 if(category!=null) {
				 try {
					 categoryDAO.getCategoryById(category.getCategoryId());
				 }catch(CategoryNotFoundException r) {
					 throw new CategoryNotFoundException("category is not there");
				 }
			 }
			return noteDAO.createNote(note);
		 }
		

	

	/* This method should be used to delete an existing note. */

	public boolean deleteNote(int noteId) {
		return noteDAO.deleteNote(noteId);

	}
	/*
	 * This method should be used to get a note by userId.
	 */

	public List<Note> getAllNotesByUserId(String userId) {
		return noteDAO.getAllNotesByUserId(userId);

	}

	/*
	 * This method should be used to get a note by noteId.
	 */
	public Note getNoteById(int noteId) throws NoteNotFoundException {
		Note note=noteDAO.getNoteById(noteId);
		if(note==null) {
			 throw new NoteNotFoundException("UserNotFoundException.class");
		}
		else {
			return note;
		}

	}

	/*
	 * This method should be used to update a existing note.
	 */

	public Note updateNote(Note note, int id)
			throws ReminderNotFoundException, NoteNotFoundException, CategoryNotFoundException {
		noteDAO.UpdateNote(note);
		Note noteNew=noteDAO.getNoteById(id);
		 Reminder reminder=note.getReminder();
		 Category category=note.getCategory();
		if(reminder!=null) {
			 try {
			 reminderDAO.getReminderById(reminder.getReminderId());
			 }catch(ReminderNotFoundException r) {
				 throw new ReminderNotFoundException("reminder is not there");
			 }
		 }
		 if(category!=null) {
			 try {
				 categoryDAO.getCategoryById(category.getCategoryId());
			 }catch(CategoryNotFoundException r) {
				 throw new CategoryNotFoundException("category is not there");
			 }
		 }
		if(noteNew==null) {
			throw new NoteNotFoundException("java.lang.String");
		}
		else {
			return note;
		}
		
	}

}
