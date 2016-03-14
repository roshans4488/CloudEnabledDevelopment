package com.DaaS.core.objects;

import java.lang.reflect.InvocationTargetException;

import javax.transaction.TransactionManager;

/**
 * @author rosha
 *
 */
public class TransactionManagerClass {

	private static final String JBOSS_TM_CLASS_NAME = "com.arjuna.ats.jta.TransactionManager";
	
	public static TransactionManager getTransactionManager() {
		try {
			Class<?> tmClass = CloudEnabledDevelopmentApplication.class.getClassLoader().loadClass( JBOSS_TM_CLASS_NAME );
			return (TransactionManager) tmClass.getMethod( "transactionManager" ).invoke( null );
		} catch ( ClassNotFoundException e ) {
			e.printStackTrace();
		} catch ( InvocationTargetException e ) {
			e.printStackTrace();
		} catch ( NoSuchMethodException e ) {
			e.printStackTrace();
		} catch ( IllegalAccessException e ) {
			e.printStackTrace();
		}
		return null;
	}
}
