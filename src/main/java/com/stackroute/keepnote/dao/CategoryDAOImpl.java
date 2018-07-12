package com.stackroute.keepnote.dao;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;

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
public class CategoryDAOImpl implements CategoryDAO {

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
	public CategoryDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory=sessionFactory;
	}

	/*
	 * Create a new category
	 */
	public boolean createCategory(Category category) {
		Session session=sessionFactory.getCurrentSession();
		session.save(category);
		session.flush();
		return true;

	}

	/*
	 * Remove an existing category
	 */
	public boolean deleteCategory(int categoryId) {
		try {
			Category category=getCategoryById(categoryId);
			if(category!=null) {
				Session session=sessionFactory.getCurrentSession();
				session.delete(category);
				session.flush();
				return true;
			}
			
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			
		}
		
      return false;
	}
	/*
	 * Update an existing category
	 */

	public boolean updateCategory(Category category) {
		try {
			if(getCategoryById(category.getCategoryId())==null) {
				return false;
			} else {
				sessionFactory.getCurrentSession().clear();
			
				sessionFactory.getCurrentSession().update(category);
				sessionFactory.getCurrentSession().flush();;

				return true;
			}
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CategoryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}
	/*
	 * Retrieve details of a specific category
	 */

	public Category getCategoryById(int categoryId) throws CategoryNotFoundException {
		Session session=sessionFactory.getCurrentSession();
		Category category = (Category)session.get(Category.class, categoryId);
		if(category==null) {
			throw new CategoryNotFoundException("Category not found");
		}
		session.flush();
		return category;

	}

	/*
	 * Retrieve details of all categories by userId
	 */
	public List<Category> getAllCategoryByUserId(String userId) {
		String hql = "FROM Category category Where category.categoryCreatedBy=:userId"; 
		Query query = getSessionFactory().getCurrentSession().createQuery(hql).setParameter("userId", userId);
		return query.getResultList();

	}

}
