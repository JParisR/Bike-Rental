package es.udc.ws.bikes.client.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.udc.ws.bikes.client.service.UserClientBikeService;
import es.udc.ws.bikes.client.service.UserClientBikeServiceFactory;
import es.udc.ws.bikes.client.service.dto.AdminClientBikeDto;
import es.udc.ws.bikes.client.service.dto.AdminClientBookDto;
import es.udc.ws.bikes.client.service.dto.UserClientBikeDto;
import es.udc.ws.bikes.client.service.dto.UserClientBookDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class UserBikeServiceClient {

	public final static String CONVERSION_PATTERN = "dd-MM-yyyy";
	
	public static void main(String[] args) {

		if(args.length == 0) {
            printUsageAndExit();
        }
        
		UserClientBikeService clientBikeService = UserClientBikeServiceFactory.getService();
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
		
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
            validateArgs(args, 7, new int[] {2, 6});

            // [reserve] bikeServiceClient -r <email> <bikeId> <creditCardNumber> <startDate> <endDate> <units>

            Long bookId;
            Date dateStart;
            Date dateEnd;
            try {
            	
            	String email = args[1];
         	   	Long bikeId = Long.valueOf(args[2]);
         	   	String creditCard = args[3];
         	   	
            	dateStart = dateFormatter.parse(args[4]);
            	Calendar startDate = Calendar.getInstance();
            	startDate.setTime(dateStart);
            	dateEnd = dateFormatter.parse(args[5]);
            	Calendar endDate = Calendar.getInstance();
            	endDate.setTime(dateEnd);
            	
            	int units = Integer.valueOf(args[6]);
            	
                bookId = clientBikeService.rentBike(new UserClientBookDto(bikeId, email, creditCard, startDate, endDate, units));

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
				"    [reserve]	bikeserviceClient -r <bikeId> <userId> <creditCardNumber> <startDate> <endDate> <units>\n" +
				"    [findId]  	bikeserviceClient -fb <bikeId>\n");
	}

}
