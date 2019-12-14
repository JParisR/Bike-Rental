package es.udc.ws.bikes.adminClient.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class AdminClientBikeServiceFactory {

	private final static String CLASS_NAME_PARAMETER
			= "ClientBikeServiceFactory.className";
	private static Class<AdminClientBikeService> serviceClass = null;
	
	private AdminClientBikeServiceFactory() {
	}
	
	@SuppressWarnings("unchecked")
	private synchronized static Class<AdminClientBikeService> getServiceClass() {
	
		if (serviceClass == null) {
		    try {
		        String serviceClassName = ConfigurationParametersManager
		                .getParameter(CLASS_NAME_PARAMETER);
		        serviceClass = (Class<AdminClientBikeService>) Class.forName(serviceClassName);
		    } catch (Exception e) {
		        throw new RuntimeException(e);
		    }
		}
		return serviceClass;
		
	}
	
	public static AdminClientBikeService getService() {
	
		try {
		    return (AdminClientBikeService) getServiceClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
		    throw new RuntimeException(e);
		}
	
	}
}
