package org.redquark.jcrstarter;

import java.io.FileInputStream;

import javax.jcr.ImportUUIDBehavior;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;

import org.apache.jackrabbit.commons.JcrUtils;

/**
 * @author Anirudh Sharma
 * 
 *         To add content a bit more efficiently, you may want to try JCR’s
 *         import facilities, such as Session.importXML. The following XML
 *         document by Elliotte Rusty Harold provides an interesting example
 *         that demonstrates a repository’s namespace capabilities.
 * 
 *         This example application will import the XML file called thirdHop.xml
 *         into a new content repository node called importxml. Once the XML
 *         content is imported, the application recursively dumps the contents
 *         of the entire workspace using the simple dump() method.
 *
 */
public class ThirdHop {

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
			 * Import the XML file unless already imported
			 */
			if (!root.hasNode("importXML")) {

				System.out.println("Importing XML");

				/**
				 * Create an unstructured node under which to import the XML
				 */
				Node node = root.addNode("importXML", "nt:unstructured");

				/**
				 * Import the file "thirdHop.xml" under the created node
				 */
				FileInputStream xml = new FileInputStream("resources\\thirdHop.xml");

				/**
				 * This deserializes an XML document and adds the resulting item subgraph as a
				 * child of the node at the provided path.
				 * 
				 * The flag ImportUUIDBehavior governs how the identifiers of incoming nodes are
				 * handled.
				 */
				session.importXML(node.getPath(), xml, ImportUUIDBehavior.IMPORT_UUID_CREATE_NEW);

				xml.close();

				session.save();

				System.out.println("Done!!!");
			}

			/**
			 * Output the repository content
			 */
			dump(root);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

	/**
	 * @param node
	 * 
	 *            Recursively outputs the contents of the given node.
	 */
	private static void dump(Node node) {

		try {

			/**
			 * First output the node path
			 */
			System.out.println(node.getPath());

			/**
			 * Skip the virtual (and large!) jcr:system subtree
			 */
			if (node.getName().equals("jcr:system")) {

				return;
			}

			/**
			 * Then output the properties
			 */
			PropertyIterator propertyIterator = node.getProperties();

			while (propertyIterator.hasNext()) {

				Property property = propertyIterator.nextProperty();

				if (property.getDefinition().isMultiple()) {

					/**
					 * A multi-valued property, print all values
					 */
					Value[] values = property.getValues();

					for (Value value : values) {

						System.out.println(property.getPath() + " = " + value.getString());
					}
				} else {

					/**
					 * A single-valued property
					 */
					System.out.println(property.getPath() + " = " + property.getString());
				}

			}

			NodeIterator nodeIterator = node.getNodes();

			/**
			 * Finally output all the child nodes recursively
			 */
			while (nodeIterator.hasNext()) {
				dump(nodeIterator.nextNode());

			}

		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

}
