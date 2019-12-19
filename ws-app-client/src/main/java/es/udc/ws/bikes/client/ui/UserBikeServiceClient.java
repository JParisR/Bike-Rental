package es.udc.ws.bikes.client.ui;

import java.util.List;

import es.udc.ws.bikes.client.service.UserClientBikeService;
import es.udc.ws.bikes.client.service.UserClientBikeServiceFactory;
import es.udc.ws.bikes.client.service.dto.UserClientBikeDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class UserBikeServiceClient {

	public static void main(String[] args) {

		if(args.length == 0) {
            printUsageAndExit();
        }
        
		UserClientBikeService clientBikeService = UserClientBikeServiceFactory.getService();

		if("-f".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // [find] bikeServiceClient -f <keywords> <availabilityDate>

            /*try {
                List<UserClientBikeDto> bikes = clientBikeService.findBikes(args[1]);
                System.out.println("Found " + bikes.size() +
                        " bike(s) with keywords '" + args[1] + "'");
                for (int i = 0; i < bikes.size(); i++) {
                    AdminClientBikeDto bikeDto = bikes.get(i);
                    System.out.println("Id: " + bikeDto.getBikeId() +
                            ", Description: " + bikeDto.getDescription() +
                            ", StartDate: " + bikeDto.getStartDate().toString() +                            
                            ", Price: " + bikeDto.getPrice() +
                    		", Units: " + bikeDto.getUnits());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }*/
		} else if("-r".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {2});

            // [reserve] bikeServiceClient -r <email> <bikeId> <creditCardNumber> <startDate> <endDate> <units>

            Long bookId;
            try {
                bookId = clientBikeService.rentBike(Long.parseLong(args[2]),
                        args[1], args[3]);

                System.out.println("bike " + args[2] +
                        " purchased sucessfully with book number " +
                        bookId);

            } catch (NumberFormatException | InstanceNotFoundException |
                     InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
		}

	}
		
	public static void validateArgs(String[] args, int expectedArgs,
                int[] numericArguments) {
		if(expectedArgs != args.length) {
			printUsageAndExit();
		}
		for(int i = 0 ; i< numericArguments.length ; i++) {
			int position = numericArguments[i];
			try {
				Double.parseDouble(args[position]);
			} catch(NumberFormatException n) {
				printUsageAndExit();
			}
		}
	}

	public static void printUsageAndExit() {
		printUsage();
		System.exit(-1);
	}

	public static void printUsage() {
		System.err.println("Usage:\n" +
				"    [add]    	bikeserviceClient -a <name> <description> <startDate> <price> <units>\n" +
				"    [delete] 	bikeserviceClient -d <bikeId>\n" +
				"    [update] 	bikeserviceClient -u <bikeId> <name> <description> <availabilityDate> <price> <units>\n" +
				"    [find]   	bikeserviceClient -f <keywords>\n" +
				"    [reserve]	bikeserviceClient -r <bikeId> <userId> <creditCardNumber>\n" +
				"    [findId]  	bikeserviceClient -fb <bikeId>\n");
	}

}
