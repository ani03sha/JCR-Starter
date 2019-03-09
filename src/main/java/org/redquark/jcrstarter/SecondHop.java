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

			/**
			 * The SimpleCredentials class is a simple implementation of the Credentials
			 * interface used for passing explicit user credentials to the
			 * Repository.login(Credentials) method.
			 * 
			 * The default Jackrabbit login mechanism accepts only username and password as
			 * valid credentials for known users. A Jackrabbit repository with a default
			 * configuration will create an admin user when it is first initialized. Thus we
			 * need to construct and use a SimpleCredentials instance with the username and
			 * initial default password of the admin user, in this case "admin" and "admin".
			 * 
			 * The SimpleCredentials constructor follows the JAAS convention of representing
			 * the username as a normal String, but the password as a character array, so we
			 * need to use the String.toCharArray() method to satisfy the constructor.
			 */
			session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));

			/**
			 * Each JCR session is associated with a workspace that contains a single node
			 * tree. A simple way to access the root node is to call the
			 * Session.getRootNode() method. Having a reference to the root node allows us
			 * to easily store and retrieve content in the current workspace.
			 */
			Node root = session.getRootNode();

			/**
			 * New content nodes can be added using the Node.addNode(String relPath) method.
			 * The method takes the name (or relative path) of the node to be added and
			 * creates the named node in the transient storage associated with the current
			 * session. Until the transient storage is persisted, the added node is only
			 * visible within the current session and not within any other session that is
			 * concurrently accessing the content repository.
			 */
			Node red = root.addNode("red");
			Node quark = red.addNode("quark");

			/**
			 * Like the added nodes, also the property is first created in the transient
			 * storage associated with the current session. If the named property already
			 * exists, then this method will change the value of that property.
			 */
			quark.setProperty("title", "Red Quark");

			/**
			 * The Session.save() method persists all pending changes in the transient
			 * storage. The changes are written to the persistent repository storage and
			 * they become visible to all sessions accessing the same workspace. Without
			 * this call all changes will be lost forever when the session is closed.
			 */
			session.save();

			/**
			 * The Node.getNode(String relPath) method returns a reference to the node at
			 * the given path relative to this node. The path syntax follows common file
			 * system conventions: a forward slash separates node names, a single dot
			 * represents the current node, and a double dot the parent node.
			 */
			Node node = root.getNode("red/quark");

			/**
			 * Each content node and property is uniquely identified by its absolute path
			 * within the workspace. The absolute path starts with a forward slash and
			 * contains all the names of the ancestor nodes in order before the name of the
			 * current node or property.
			 * 
			 * The path of a node or property can be retrieved using the Item.getPath()
			 * method. The Item interface is a superinterface of Node and Property, and
			 * contains all the functionality shared by nodes and properties.
			 */
			System.out.println(node.getPath());

			/**
			 * Properties can be accessed using the Node.getProperty(String relPath) method
			 * that returns an instance of the Property interface that represents the
			 * property at the given path relative to the current node. In this case the
			 * “message” property is the one we created a few lines earlier.
			 * 
			 * A JCR property can contain either a single or multiple values of a given
			 * type. There are property types for storing strings, numbers, dates, binary
			 * streams, node references, etc. We just want the single string value, so we
			 * use the Property.getString() method.
			 */
			System.out.println(node.getProperty("title").getString());

			/**
			 * Nodes and properties can be removed using theItem.remove() method. The method
			 * removes the entire content subtree, so we only need to remove the topmost
			 * "red" node to get rid of all the content we added before.
			 * 
			 * Removals are first stored in the session-local transient storage, just like
			 * added and changed content. Like before, the transient changes need to be
			 * explicitly saved for the content to be removed from the persistent storage.
			 */
			root.getNode("red").remove();

			/**
			 * The Session.save() method persists all pending changes in the transient
			 * storage. The changes are written to the persistent repository storage and
			 * they become visible to all sessions accessing the same workspace. Without
			 * this call all changes will be lost forever when the session is closed.
			 */
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
