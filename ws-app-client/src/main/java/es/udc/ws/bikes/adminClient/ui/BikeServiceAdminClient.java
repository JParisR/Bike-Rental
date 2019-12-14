package es.udc.ws.bikes.adminClient.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikes.adminClient.service.AdminClientBikeService;
import es.udc.ws.bikes.adminClient.service.AdminClientBikeServiceFactory;
import es.udc.ws.bikes.adminClient.service.dto.AdminClientBikeDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class BikeServiceAdminClient {
	
	
	
    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        AdminClientBikeService clientBikeService =
        		AdminClientBikeServiceFactory.getService();
        if("-a".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[] {2, 3, 5});

            // [add] BikeServiceClient -a <title> <hours> <minutes> <description> <price>

            try {
                Long bikeId = clientBikeService.addBike(new AdminClientBikeDto(null, //cambiar null
                        args[1], null, Float.valueOf(args[3]),
                        Short.valueOf(args[4]), Short.valueOf(args[5]), Double.valueOf(args[6])));

                System.out.println("Bike " + bikeId + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-u".equalsIgnoreCase(args[0])) {
           validateArgs(args, 7, new int[] {1, 3, 4, 6});

           // [update] BikeServiceClient -u <bikeId> <title> <hours> <minutes> <description> <price>

           try {
                clientBikeService.updateBike(new AdminClientBikeDto(null, //CAMBIAR NULL
                        args[1], null, Float.valueOf(args[3]),
                        Short.valueOf(args[4]), Short.valueOf(args[5]), Double.valueOf(args[6])));

                System.out.println("Bike " + args[1] + " updated sucessfully");

            } catch (NumberFormatException | InputValidationException |
                     InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-f".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // [find] BikeServiceClient -f <keywords>

            try {
                List<AdminClientBikeDto> bikes = AdminClientBikeService.findBikes(args[1]);
                System.out.println("Found " + bikes.size() +
                        " bike(s) with keywords '" + args[1] + "'");
                for (int i = 0; i < bikes.size(); i++) {
                	AdminClientBikeDto bikeDto = bikes.get(i);
                    System.out.println("Id: " + bikeDto.getBikeId() +
                            ", Description: " + bikeDto.getDescription() +
                            ", StartDate: " + bikeDto.getStartDate() +
                            ", Price: " + bikeDto.getPrice() +
                            ", Description: " + bikeDto.getUnits());
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
                "    [add]    BikeServiceClient -a <title> <hours> <minutes> <description> <price>\n" +
                "    [remove] BikeServiceClient -r <bikeId>\n" +
                "    [update] BikeServiceClient -u <bikeId> <title> <hours> <minutes> <description> <price>\n" +
                "    [find]   BikeServiceClient -f <keywords>\n" +
                "    [buy]    BikeServiceClient -b <bikeId> <userId> <creditCardNumber>\n" +
                "    [get]    BikeServiceClient -g <saleId>\n");
    }

}
