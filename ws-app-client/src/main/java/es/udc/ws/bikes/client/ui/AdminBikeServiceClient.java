package es.udc.ws.bikes.client.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import es.udc.ws.bikes.client.service.AdminClientBikeService;
import es.udc.ws.bikes.client.service.AdminClientBikeServiceFactory;
import es.udc.ws.bikes.client.service.dto.AdminClientBikeDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.util.List;
import java.util.Locale;

public class AdminBikeServiceClient {
	
	public final static String CONVERSION_PATTERN = "dd-MM-yyyy";
	
    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        AdminClientBikeService clientBikeService =
                AdminClientBikeServiceFactory.getService();
        
        SimpleDateFormat dateFormatter = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
        if("-a".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[] {4, 5});

            // [add] bikeServiceClient -a <name> <description> <startDate> <price> <units>
            
            Date dateStart = null;
            
            try {
            	dateStart = dateFormatter.parse(args[3]);
            	Calendar startDate = Calendar.getInstance();
            	startDate.setTime(dateStart);
            	
                Long bikeId = clientBikeService.addBike(new AdminClientBikeDto(args[1], args[2], 
                		startDate, Float.valueOf(args[4]), Integer.valueOf(args[5])));

                System.out.println("bike " + bikeId + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-u".equalsIgnoreCase(args[0])) {
           validateArgs(args, 7, new int[] {1, 5, 6});

           // [update] bikeServiceClient -u <bikeId> <name> <description> <availabilityDate> <price> <units>

           try {
        	   Calendar startDate = Calendar.getInstance();
        	   String date[] = args[4].split("-");
        	   startDate.set(Integer.valueOf(date[2]), 
        			   			Integer.valueOf(date[1]) - 1, 
        			   			Integer.valueOf(date[0]));
        	   
               clientBikeService.updateBike(new AdminClientBikeDto(Long.valueOf(args[1]), args[2], args[3], 
            		   startDate, Float.valueOf(args[5]), Integer.valueOf(args[6])));

               System.out.println("bike " + args[1] + " updated sucessfully");

            } catch (NumberFormatException | InputValidationException |
                     InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-fb".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            // [find bikeId] bikeserviceClient -fb <bikeId>

            try {
                AdminClientBikeDto bikeDto = clientBikeService.findBikesById(Long.valueOf(args[1]));
                /*for (int i = 0; i < bikes.size(); i++) {
                	AdminClientBikeDto bikeDto = bikes.get(i);*/
	            System.out.println("Id: " + bikeDto.getBikeId() +
	                            ", Description: " + bikeDto.getDescription() +
	                            //", StartDate: " + bikeDto.getStartDate().toString() +                            
	                            ", Price: " + bikeDto.getPrice() +
	                    		", Units: " + bikeDto.getUnits());
	                
      
            } catch (NumberFormatException ex) {
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
        for (int i = 0; i < args.length; i++) {
			if (args[i].equals(" ")) {
				args[i] = "";
			}
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
