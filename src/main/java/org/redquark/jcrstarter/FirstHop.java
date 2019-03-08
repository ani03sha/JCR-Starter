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

		Session session = null;

		try {

			/**
			 * Get the instance of JCR Repository interface. The actual implementation
			 * depends on the jar files available on the classpath and in this example is a
			 * TransientRepository. The implementation contains a utility feature that will
			 * take care of the initial configuration and repository construction when the
			 * first session is started. Thus there is no need for manual configuration for
			 * now unless you want direct control over the repository setup.
			 * 
			 * The TransientRepository implementation will automatically initialize the
			 * content repository when the first session is started and shut it down when
			 * the last session is closed. Thus there is no need for explicit repository
			 * shutdown as long as all sessions are properly closed. Note that a Jackrabbit
			 * repository directory contains a lock file that prevents it from being
			 * accessed simultaneously by multiple processes. You will see repository
			 * startup exceptions caused by the lock file if you fail to properly close all
			 * sessions or otherwise shut down the repository before leaving the process
			 * that accesses a repository. Normally you can just manually remove the lock
			 * file in such cases but such cases always present a chance of repository
			 * corruption especially if you use a non-transactional persistence manager.
			 */
			Repository repository = JcrUtils.getRepository();

			/**
			 * Get the session of the repository by logging into it. Here we are using Guest
			 * Credentials. This example uses GuestCredentials and Jackrabbit maps it to the
			 * read-only anonymous user.
			 * 
			 * Since we use the TransientRepository class as the Repository implementation,
			 * this step will also cause the repository to be initialized.
			 */
			session = repository.login(new GuestCredentials());

			/**
			 * Getting the user id. The username or identifier of the user associated with a
			 * session is available using the Session.getUserID() method. Jackrabbit returns
			 * "anonymous" by default.
			 */
			String userId = session.getUserID();

			/**
			 * Getting the name of the user. Each content repository implementation
			 * publishes a number of string descriptors that describe the various
			 * implementation properties, like the implementation level and the supported
			 * optional JCR features. See the Repository interface for a list of the
			 * standard repository descriptors. The REP_NAME_DESC descriptor contains the
			 * name of the repository implementation, in this case "Jackrabbit".
			 */
			String name = repository.getDescriptor(Repository.REP_NAME_DESC);

			System.out.println("Logged in as:" + userId + " to a repo: " + name);

		} catch (RepositoryException e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

}
