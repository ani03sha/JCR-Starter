package org.redquark.jcrstarter;

import javax.jcr.GuestCredentials;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.commons.JcrUtils;

/**
 * @author Anirudh Sharma
 *
 */
public class FirstHop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			/**
			 * Get the instance of repository
			 */
			Repository repository = JcrUtils.getRepository();

			/**
			 * Get the session of the repository by logging into it. Here we are using Guest
			 * Credentials
			 */
			Session session = repository.login(new GuestCredentials());

			/**
			 * Getting the user id
			 */
			String userId = session.getUserID();

			/**
			 * Getting the name of the user
			 */
			String name = repository.getDescriptor(Repository.REP_NAME_DESC);

			System.out.println("Logged in as:" + userId + " to a repo: " + name);

		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

}
