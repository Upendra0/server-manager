/**
 * 
 */
package com.elitecore.sm.test;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.MappingStyle;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.PropertyChange;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.metamodel.property.Property;
import org.javers.core.metamodel.type.ManagedType;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.services.model.Service;

/**
 * @author vandana.awatramani
 *
 */
public class TestJavers {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// test java objects no relation to db, but with relation
			Server s = new Server();

			s.setId(1);
			s.setServerType(null);
			s.setName("name");
			s.setDescription("Mediation Server Type");
			s.setIpAddress(BaseConstants.IPADDRESS);
			//s.setSynced(true);
			s.setUtilityPort(1617);
			
			Server s1 = new Server();

			s1.setId(1);
			s1.setServerType(null);
			s1.setName("name");
			s1.setDescription("Mediation Server Type");
			s1.setIpAddress("ipAddress 1s ");
			//s1.setSynced(true);
			s1.setUtilityPort(1617);
			
			ServerType stp1 = new ServerType();
			stp1.setId(1);
			stp1.setName("MEDIATION");
			stp1.setAlias("Mediation");
			stp1.setDescription("Mediation Server Type");
			
			ServerType stp2 = new ServerType();
			stp2.setId(1);
			stp2.setName("MEDIATION");
			stp2.setAlias("Mediation ALIAS ");
			stp2.setDescription("Mediation Server Type");
			

			s.setServerType(stp1);
			s1.setServerType(stp2);

			System.out.println("stp1 " + stp1);
			System.out.println("stp2" + stp2);
			compareUsingJavers(null, s1);

			Session session = HibernateUtil.getSessionFactory().openSession();

			System.out.println("TestEntities.createServerInstance()");

			Server serverInsFromDB = (Server) session.get(Server.class,
					1);

			Server newS = new Server();
			newS.setId(1);
			newS.setName("VA");
			System.out.println(serverInsFromDB);
			System.out.println(newS);
			// in case of lazy loaded proxy comparing server type gives error so
			// it can be nullified before comparison
			// or it should be annotated with @DiffIgnore
			serverInsFromDB.setServerType(null);

			compareUsingJavers(serverInsFromDB, newS);
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			System.exit(0);
		}

	}

	public static void compareUsingJavers(Object oldObj, Object newObj) {

		// this is needed as our hibernate annotations are at getter level.
		JaversBuilder javersBuilder = JaversBuilder.javers().withMappingStyle(MappingStyle.BEAN);

		Javers javers = javersBuilder.build();

		System.out.println("Javers type of oldobj is " + javers.getTypeMapping(oldObj.getClass()).prettyPrint());

		Diff diff = javers.compare(oldObj, newObj);
		/*ValueChange change = diff.getChangesByType(ValueChange.class).get(0);
		
		System.out.println("Props Name is :: " + change.getPropertyName());
		System.out.println("Old Value :: " + change.getLeft());
		System.out.println("New Value :: " + change.getRight());*/
		
		List<Change> changeList = diff.getChanges();
		List<ValueChange> modifiedProps = new ArrayList<ValueChange>();
		
		for (int i = 0; i < changeList.size(); i++) {
			ValueChange tempValueChange = diff.getChangesByType(ValueChange.class).get(i);
			
			System.out.println("Props Name is :: " + tempValueChange.getPropertyName());
			System.out.println("Old Value :: " + tempValueChange.getLeft());
			System.out.println("New Value :: " + tempValueChange.getRight());
			modifiedProps.add(tempValueChange);
		}
		System.out.println(diff.prettyPrint());

	}

}
