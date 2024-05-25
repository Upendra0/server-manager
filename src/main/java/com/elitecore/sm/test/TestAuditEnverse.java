/**
 * 
 */
package com.elitecore.sm.test;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;

import com.elitecore.sm.server.model.Server;

/**
 * @author vandana.awatramani
 *
 */
public class TestAuditEnverse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println("TestAuditEnverse.main()");
			printRevisions();
		} catch (Exception e) {
			System.out.println("App.main() caught ex ");
			e.printStackTrace();
		} finally {
			// HibernateUtil.shutdown();
			System.out.println("call in finally");
			System.exit(1);
		}

	}

	/**
	 * this manipulates the audit read by envers package
	 */
	@SuppressWarnings("rawtypes")
	public static void printRevisions() {
		System.out.println("TestAuditEnverse.printRevisions()");

		Session session = HibernateUtil.getSessionFactory().openSession();

		AuditReader reader = AuditReaderFactory.get(session);
		Server s = (Server) reader.find(Server.class, "SVR0001", 1);

		System.out.println(s.getName() + " " + s.getDescription());

		List<Number> versions = reader.getRevisions(Server.class, "SVR0001");
		for (Number number : versions) {
			System.out.print(number + " ");
		}

		List<Number> revisions = reader.getRevisions(Server.class, "SVR0001");
		System.out.println(!revisions.isEmpty());
		System.out.println("@rev numbers@" + revisions);

		for (Number number : revisions) {
			System.out.print(number + " ");
		}
		List rsList = reader.createQuery()
				.forRevisionsOfEntity(Server.class, false, true)
				.add(AuditEntity.id().eq("SVR0001"))
				.add(AuditEntity.property("description").hasChanged())
				.getResultList();

		for (Object rsElem : rsList) {
			Object[] rsArray = (Object[]) rsElem;

			for (Object arrElem : rsArray) {
				System.out.println(arrElem);
			}

		}

	}

}
