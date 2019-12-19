package es.udc.ws.bikes.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class UserClientBikeServiceFactory {
	
	private final static String CLASS_NAME_PARAMETER = "UserClientBikeServiceFactory.className";
	private static Class<UserClientBikeService> serviceClass = null;

	private UserClientBikeServiceFactory() {
	}

	@SuppressWarnings("unchecked")
	private synchronized static Class<UserClientBikeService> getServiceClass() {

		if (serviceClass == null) {
			try {
				String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
				serviceClass = (Class<UserClientBikeService>) Class.forName(serviceClassName);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		return serviceClass;

	}

	public static UserClientBikeService getService() {

		try {
			return (UserClientBikeService) getServiceClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
	}

}
