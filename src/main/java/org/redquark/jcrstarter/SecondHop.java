package org.redquark.jcrstarter;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.commons.JcrUtils;

/**
 * @author Anirudh Sharma
 * 
 *         The main function of a content repository is allow applications to
 *         store and retrieve content. The content in a JCR content repository
 *         consists of structured or unstructured data modeled as a hierarchy of
 *         nodes with properties that contain the actual data.
 * 
 *         The following example application first stores some content to the
 *         initially empty content repository, then retrieves the stored content
 *         and outputs it, and finally removes the stored content.
 *
 */
public class SecondHop {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Session session = null;

		try {
			Repository repository = JcrUtils.getRepository();

			session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));

			Node root = session.getRootNode();

			// Storing the content
			Node red = root.addNode("red");
			Node quark = red.addNode("quark");
			quark.setProperty("title", "Red Quark");

			// Retrieving the content
			Node node = root.getNode("red/quark");
			System.out.println(node.getPath());
			System.out.println(node.getProperty("title").getString());

			// Remove content
			root.getNode("red").remove();

			// Saving stuff
			session.save();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

}
