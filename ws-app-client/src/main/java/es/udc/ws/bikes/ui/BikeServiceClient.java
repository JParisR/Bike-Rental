package es.udc.ws.bikes.ui;

import java.util.Calendar;
import es.udc.ws.bikes.service.ClientBikeService;
import es.udc.ws.bikes.service.ClientBikeServiceFactory;
import es.udc.ws.bikes.service.dto.ClientBikeDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.util.List;

public class BikeServiceClient {

    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientBikeService clientBikeService =
                ClientBikeServiceFactory.getService();
        if("-a".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[] {4, 5});

            // [add] bikeServiceClient -a <name> <description> <startDate> <price> <units>

            try {
            	Calendar startDate = Calendar.getInstance();
         	   	String date[] = args[3].split("-");
         	   	startDate.set(Integer.valueOf(date[2]), 
         			   			Integer.valueOf(date[1]), 
         			   			Integer.valueOf(date[0]));
            	
                Long bikeId = clientBikeService.addBike(new ClientBikeDto(null, args[2], startDate, 
                		Float.valueOf(args[4]), Integer.valueOf(args[5])));

                System.out.println("bike " + bikeId + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-d".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            // [delete] bikeserviceClient -d <bikeId>

            try {
                clientBikeService.removeBike(Long.parseLong(args[1]));

                System.out.println("bike with id " + args[1] +
                        " removed sucessfully");

            } catch (NumberFormatException | InstanceNotFoundException ex) {
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
        			   			Integer.valueOf(date[1]), 
        			   			Integer.valueOf(date[0]));
        	   
               clientBikeService.updateBike(new ClientBikeDto(Long.valueOf(args[1]), args[3], startDate, 
               		Float.valueOf(args[5]), Integer.valueOf(args[6])));

               System.out.println("bike " + args[1] + " updated sucessfully");

            } catch (NumberFormatException | InputValidationException |
                     InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-f".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // [find] bikeServiceClient -f <keywords>

            try {
                List<ClientBikeDto> bikes = clientBikeService.findBikes(args[1]);
                System.out.println("Found " + bikes.size() +
                        " bike(s) with keywords '" + args[1] + "'");
                for (int i = 0; i < bikes.size(); i++) {
                    ClientBikeDto bikeDto = bikes.get(i);
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
            validateArgs(args, 4, new int[] {1});

            // [reserve] bikeServiceClient -r <email> <bikeId> <creditCardNumber> <startDate> <endDate> <units>

            Long bookId;
            try {
                bookId = clientBikeService.bookBike(Long.parseLong(args[2]),
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

        } else if("-fb".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            // [find bikeId] bikeserviceClient -fb <bikeId>

            try {
                List<ClientBikeDto> bikes = clientBikeService.findBikesById(Long.parseLong(args[1]));

                for (int i = 0; i < bikes.size(); i++) {
                    ClientBikeDto bikeDto = bikes.get(i);
                    System.out.println("Id: " + bikeDto.getBikeId() +
                            ", Description: " + bikeDto.getDescription() +
                            ", StartDate: " + bikeDto.getStartDate().toString() +                            
                            ", Price: " + bikeDto.getPrice() +
                    		", Units: " + bikeDto.getUnits());
                }
                
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
