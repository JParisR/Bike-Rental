package es.udc.ws.bikes.client.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.udc.ws.bikes.client.service.UserClientBikeService;
import es.udc.ws.bikes.client.service.UserClientBikeServiceFactory;
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
		SimpleDateFormat dateFormatter = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
		
		UserClientBikeService clientBikeService = UserClientBikeServiceFactory.getService();
		
		
		if("-f".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[] {});

            // [find] bikeServiceClient -f <keywords> <availabilityDate>
                   
            try {
            	Date date = null;
            	date = dateFormatter.parse(args[2]);
            	Calendar startDate = Calendar.getInstance();
            	startDate.setTime(date);
                
            	List<UserClientBikeDto> bikes = clientBikeService.findBikes(args[1], startDate);
                
            	System.out.println("Found " + bikes.size() + " bike(s) with keywords '" + args[1] + "'");
                
            	for (int i = 0; i < bikes.size(); i++) {
                    UserClientBikeDto bikeDto = bikes.get(i);
                    
                    System.out.println("Id: " + bikeDto.getBikeId() +
                            ", Description: " + bikeDto.getDescription() +
                            ", StartDate: " + bikeDto.getStartDate().toString() +                            
                            ", Price: " + bikeDto.getPrice() +
                    		", Units: " + bikeDto.getUnits());
                }
            	
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            
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
            	
                bookId = clientBikeService.rentBike(new UserClientBookDto(bikeId, email, creditCard, startDate, endDate, units, 0));

                System.out.println("bike " + args[2] +
                        " purchased sucessfully with book number " +
                        bookId);

            } catch (NumberFormatException | InstanceNotFoundException |
                     InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            
		} else if("-fr".equalsIgnoreCase(args[0])) {
        	validateArgs(args, 2, new int[] {});
        	
        	try {
        		
        		List<UserClientBookDto> Lista = clientBikeService.findBooks(args[1]);
        		String scoreString;
        		
        		System.out.println("Found "+ Lista.size() + " reservation(s) with mail '"+ args[1]+"'");
        		
        		for(int i = 0 ; i< Lista.size();i++) {
        			UserClientBookDto book = Lista.get(i);
        			/*if(book.getScore()== -1){
        				scoreString = "Not rated yet";
        			} else {
        				scoreString = book.getScore().toString();
        			}*/
                	System.out.println("ReservationId: "+ book.getBookId()+",\n"+
                						"BikeId: " +book.getBikeId()+",\n"+
                						"Mail: "+ book.getEmail()+",\n"+
                						"CreditCardNumber: "+ book.getCreditCard()+",\n"+
                						"StartDate: " + book.getStartDate().getTime()+",\n"+
                						"Duration: " + book.getEndDate().getTime()+",\n"+
                						"NumBikes: "+ book.getUnits()+",\n");
                						
                }
        		
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
				"    [find]   	bikeserviceClient -f <keywords> <date>\n" +
				"    [reserve]	bikeserviceClient -r <bikeId> <userId> <creditCardNumber> <startDate> <endDate> <units>\n" +
				"    [findId]  	bikeserviceClient -fb <bikeId>\n");
	}

}
