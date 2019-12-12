package es.udc.ws.bikes.client.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikes.client.service.ClientBikeService;
import es.udc.ws.bikes.client.service.ClientBikeServiceFactory;
import es.udc.ws.bikes.client.service.dto.ClientBikeDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class BikeServiceClientAdm {
    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientBikeService clientBikeService =
                ClientBikeServiceFactory.getService();
        if("-a".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[] {2, 3, 5});

            // [add] BikeServiceClient -a <bikeId><description> <price> <units>

            try {
                Long bikeId = clientBikeService.addBike(new ClientBikeDto(Long.valueOf(args[1]), 
                		args[2], null, Short.valueOf(args[3]),Short.valueOf(args[4]), Short.valueOf(args[5]), Double.valueOf(args[6])));
                
            //    (Long bikeId, String description, Calendar startDate, float price,
              //  		int units, int numberOfRates, double avgRate)

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
        	   Calendar startDate = Calendar.getInstance();  //REPASAR ESTO
        	   SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        	   startDate.setTime(sdf.parse(args[3]));
               
        	   clientBikeService.updateBike(new ClientBikeDto(Long.valueOf(args[1]), 
                args[2], startDate, Short.valueOf(args[4]),Short.valueOf(args[5]), Short.valueOf(args[6]), Double.valueOf(args[7])));

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
                List<ClientBikeDto> bikes = clientBikeService.findBikes(args[1]);
                System.out.println("Found " + bikes.size() +
                        " bike(s) with keywords '" + args[1] + "'");
                for (int i = 0; i < bikes.size(); i++) {
                    ClientBikeDto bikeDto = bikes.get(i);
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
