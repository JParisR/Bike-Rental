package es.udc.ws.bikes.client.ui;

import java.util.List;
import es.udc.ws.bikes.client.service.ClientBikeService;
import es.udc.ws.bikes.client.service.ClientBikeServiceFactory;
import es.udc.ws.bikes.client.service.dto.ClientBikeDto;
import es.udc.ws.bikes.client.service.dto.ClientBookDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class BikeServiceClient {

    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientBikeService clientBikeService =
                ClientBikeServiceFactory.getService();
        
        if("-f".equalsIgnoreCase(args[0])) {
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
                            ", Units: " + bikeDto.getUnits());
                    
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-b".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {1});

            // [book] BikeServiceClient -b <bikeId> <userId> <creditCardNumber>

            Long bookId;
            try {
                bookId = clientBikeService.bookBike(Long.parseLong(args[1]),
                        args[2], args[3]);

                System.out.println("Bike " + args[1] +
                        " purchased sucessfully with sale number " +
                        bookId);

            } catch (NumberFormatException | InstanceNotFoundException |
                     InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-fb".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // [findBooks] BikeServiceClient -fb <email> Obtener las reservas de un usuario.

            try {
                List<ClientBookDto> books = clientBikeService.findBooks(args[1]);
                System.out.println("Found " + books.size() +
                        " bike(s) with keywords '" + args[1] + "'");
                for (int i = 0; i < books.size(); i++) {
                    ClientBookDto bookDto = books.get(i);
                    System.out.println("Id: " + bookDto.getBookId() +
                            ", bikeId: " + bookDto.getBikeId() +
                            ", InitDate: " + bookDto.getInitDate() +
                            ", EndDate: " + bookDto.getEndDate() +
                            ", NumberBikes: " + bookDto.getNumberBikes() +
                            ", BookRate: " + bookDto.getBookRate());
                            
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            
        } else if("-p".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // [PuntuarBooks] BikeServiceClient -p <keywords> Puntuar las reservas

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
