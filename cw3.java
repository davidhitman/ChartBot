package cw3;

//importing of the java classes to be used
import java.io.File;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

//public class
public class cw3 {
	// variable and scanner initialisation
	private static Scanner shellInput;    
	private static boolean shellOpen = false;
	// Array lis initialisation
	private static ArrayList<String> infectionDeck = new ArrayList<String>();
	private static ArrayList<String> infectionDeckDiscard = new ArrayList<String>();
	private static ArrayList<String> playerDeck = new ArrayList<String>();
	private static ArrayList<String> playerDeckDiscard = new ArrayList<String>();
	private static ArrayList<ArrayList<String>> cardInHands = new ArrayList<ArrayList<String>>();
	private static ArrayList<String> researchStation = new ArrayList<String>();
	private static ArrayList<String> curedDiseases =  new ArrayList<String>();
	// array initialisation
	private static String[] cities;
	private static int[][] connections;
	private static int[] diseaseCubes;
	// variable initialisation
	private static Random rand = new Random();
	private static int numberCities = -1;
	private static int numberConnections = -1;
	private static int currentUser = 0;
	private static int action=0;
	private static final int NUMBER_USERS = 4;
	// declare the users to be used and there location
	private static final String[] userNames = {"Al","Bob","Dav","Dan"};
	private static int[] userLocation = {0,0,0,0};
	// place cities in either the red, blue, black or yellow category
	private static String[] redGroup = {"Seoul","Tokyo","Osaka","Beijing", "Shanghai", "Taipei", "Hong Kong", "Manilla", "Bangkok", "Ho Chi Minh City", "Jakarta", "Sydney"};
	private static String[] yellowGroup = {"Los Angeles","Mexico City","Miami","Sao Paulo", "Lagos", "Khartoum", "Santiago", "Lima", "Bogota", "Buenos Aires", "Kinshasa", "Johannesburg"}; 
	private static String[] blueGroup = {"Atlanta","San Franciso","Chicago","Montreal", "Washington", "New York", "London", "Madrid", "Essen", "Paris", "Milan", "St. Petersburg"};
	private static String[] blackGroup = {"Moscow","Istanbul","Baghdad","Tehran", "Algiers", "Cairo", "Riyadh", "Karachi", "Delhi", "Kolkata", "Mumbai", "Chennai"};
	
	// path to the city file
	private static final String cityMapFileName= "/Users/david/Desktop/fullMap.txt";

	// print the available actions
	private static void printActions() {
		System.out.println ("Enter the action you wish to perform in the terminal, from the list below.");
		System.out.println ("1: quit");
		System.out.println ("2: location");
		System.out.println ("3: cities");
		System.out.println ("4: connections");
		System.out.println ("5: adjacent");
		System.out.println ("6: infections");
		System.out.println ("7: researchStations");
		System.out.println ("8: buildResearchStation");
		System.out.println ("9: removeDiseaseCube");
		System.out.println("10: moveToConnectedCity");
		System.out.println("11: moveToResearchCentre");
		System.out.println("12: moveToAnyCity");
		System.out.println("13: moveToCardCity");
		System.out.println("14: ViewTheCardsInYourHand");
		System.out.println("15: DiscoverCure");
		System.out.println("16: Agent");
		System.out.println ("Enter Your Choice:");
		
	}
	
	// assign an integer to the action
	private static final int QUIT = 1;
	private static final int PRINT_LOCATION = 2;
	private static final int PRINT_CITIES = 3;
	private static final int PRINT_CONNECTIONS = 4;
	private static final int PRINT_ADJACENT_CITIES = 5;
	private static final int PRINT_DISEASES = 6;
	private static final int PRINT_RESEARCHSTATION = 7;
	private static final int PRINT_BUILDRESEARCHSTATION = 8;
	private static final int REMOVEDISEASECUBE = 9;
	private static final int MOVETOCONNECTEDCITY = 10;
	private static final int MOVETORESEARCHCENTRE = 11;
	private static final int MOVETOANYCITY = 12;
	private static final int MOVETOCARDCITY = 13;
	private static final int VIEWCARDS = 14;
	private static final int DISCOVERCURE = 15;
	private static final int AGENT = 16;
	
	
	//check if the user input is correct the call action
	private static int ProcessUserInput(String inputString) {
		if (inputString.compareTo("quit") == 0)
			return QUIT;
		else if (inputString.compareTo("location") == 0)
			return PRINT_LOCATION;
		else if (inputString.compareTo("cities") == 0)
			return PRINT_CITIES;
		else if (inputString.compareTo("connections") == 0)
			return PRINT_CONNECTIONS;
		else if (inputString.compareTo("adjacent") == 0)
			return PRINT_ADJACENT_CITIES;
		else if (inputString.compareTo("infections") == 0)
			return PRINT_DISEASES;
		else if (inputString.compareTo("researchStations") == 0)
			return PRINT_RESEARCHSTATION;
		else if (inputString.compareTo("buildResearchStation") == 0)
			return PRINT_BUILDRESEARCHSTATION;
		else if (inputString.compareTo("removeDiseaseCube") == 0)
			return REMOVEDISEASECUBE;
		else if (inputString.compareTo("moveToConnectedCity") == 0)
			return MOVETOCONNECTEDCITY;
		else if (inputString.compareTo("moveToResearchCentre") == 0)
			return MOVETORESEARCHCENTRE;
		else if (inputString.compareTo("moveToAnyCity") == 0)
			return MOVETOANYCITY;
		else if (inputString.compareTo("moveToCardCity") == 0)
			return MOVETOCARDCITY;
		else if (inputString.compareTo("ViewTheCardsInYourHand") == 0)
			return VIEWCARDS;
		else if (inputString.compareTo("DiscoverCure") == 0)
			return DISCOVERCURE;
		else if (inputString.compareTo("Agent") == 0)
			return AGENT;
		else 
			return -1;
	}
	
	//Loop through the city array, and return the offset of the cityName parameter in that
	//array.  Return -1 if the cityName is not in the array.
	private static int getCityOffset(String cityName) {
		for (int cityNumber = 0; cityNumber < numberCities; cityNumber++) {
			if (cityName.compareTo(cities[cityNumber]) == 0) 
				return cityNumber;
		}
		return -1;
	}
	
	// method to read the city
	private static void readCities(int numCities, Scanner scanner) {
		//A simple loop reading cities in.  It assumes the file is text with the last character 
		//of the line being the last letter of the city name.
		for (int cityNumber = 0; cityNumber < numCities; cityNumber++) {
			String cityName = scanner.nextLine();
			cities[cityNumber] = cityName;

			infectionDeck.add(cityName);
			playerDeck.add(cityName);
			
		}
	}
	
	//Look through the connections and see if the city numbers are in them.  If
	//Return whether they are in the list.
	private static boolean citiesAdjacent(int city1,int city2) {
		for (int compareConnection = 0; compareConnection < numberConnections; compareConnection ++) {
			if ((connections[0][compareConnection] == city1) &&
				(connections[1][compareConnection] == city2))
				return true;
			//Need to check both ways A to B and B to A as only one connection is stored.
			else if ((connections[0][compareConnection] == city2) &&
					(connections[1][compareConnection] == city1))
					return true;		
		}
		return false;
	}
	// method to read the connection
	private static void readConnections(int numConnections, Scanner scanner) {
		//A simple loop reading connections in.  It assumes the file is text with the last 
		//character of the line being the last letter of the city name.  The two 
		//cities are separated by a ; with no spaces
		for (int connectionNumber = 0; connectionNumber < numConnections; connectionNumber++) {
			String connectionName = scanner.nextLine();
			String cityName[] = connectionName.split(";");
			int firstCityOffset = getCityOffset(cityName[0]);
			int secondCityOffset = getCityOffset(cityName[1]);
			connections[0][connectionNumber] = firstCityOffset;
			connections[1][connectionNumber] = secondCityOffset;
		}
	}
	// reading the city file
	private static void readCityGraph() {

		//Open the file and read it.  
		try {
		      File fileHandle = new File(cityMapFileName);
		      Scanner mapFileReader = new Scanner(fileHandle);

		      //read the number of cities and allocate variables.
		      numberCities = mapFileReader.nextInt();
		      String data = mapFileReader.nextLine();  //read the rest of the line after the int
		      cities = new String[numberCities]; //allocate the cities array
		      diseaseCubes = new int[numberCities];
		      
		      //tead the number of connections and allocate variables.
		      numberConnections = mapFileReader.nextInt();
		      data = mapFileReader.nextLine();  //read the rest of the line after the int
		      connections = new int[2][numberConnections];

		      //read cities
		      readCities(numberCities,mapFileReader);
		      //readConnections 
		      readConnections(numberConnections,mapFileReader);
		      
		      mapFileReader.close();
		    } 
		 
		 catch (FileNotFoundException error) {
		      System.out.println("An error occurred reading the city graph.");
		      error.printStackTrace();
		    }
	}
	
	// getting the user input
	private static int getUserInput() {
		boolean gotReasonableInput = false;
		int processedUserInput = -1;

		// check if the shell is open, the shell allows the user to input 
		if (!shellOpen) {
			shellInput = new Scanner(System. in);
			shellOpen = true;
			
		}
		// if the input is not reasonable then try again
		while (!gotReasonableInput) {
			String userInput = shellInput.nextLine();
	
			processedUserInput = ProcessUserInput(userInput); // check which number from the user list has the user selected					
			if (processedUserInput >= 0)
				gotReasonableInput = true;
			else
				System.out.println(userInput + "is not a good command. Try 'location'.");				
		}		
		return processedUserInput;
	}

	private static void  defaultResearchStation() { // set a research station Atlanta by default
		researchStation.add(cities[0]);

	}
	
	// method to build a research station
	private static void buildResearchStation() { 
		boolean cardFound = false;
		boolean buildResearchStation = true;
		String Card ="";
		int integerCurrentLocation = userLocation[currentUser]; // get user current location
		String currentLocation = cities[integerCurrentLocation];// get user current location
		System.out.println("Enter the City you wish to build a research centre");
		String cityToBuildResearchCentre = shellInput.nextLine();
		
		// check if the city you wish to build a research station does not have one already
		if (researchStation.contains(cityToBuildResearchCentre)) {
			System.out.println("This City Already has a research station");
			buildResearchStation = false;
				
		}
		
		// if the buildResearchStation is true then 
		if (buildResearchStation) {
			if (currentLocation.equals(cityToBuildResearchCentre)) { // your current location is the same as the city you wish to build on
				for (int cardNumber=0; cardNumber<(cardInHands.get(currentUser).size()); cardNumber++) { // loop through your card in hands
					if (currentLocation.equals(cardInHands.get(currentUser).get(cardNumber))) { // if you have the card that matches your current location then
						cardFound = true;
						Card = cardInHands.get(currentUser).get(cardNumber);	// assign the variable card to card
						cardInHands.get(currentUser).remove(cardNumber); // discard the card
					}
				}
				if(cardFound) { // if the card is found (card that matche the city your in)
					researchStation.add(Card); // build a research station
					playerDeckDiscard.add(Card); // add the card to the player discard
					System.out.println("You have built a research Centre in"+currentLocation); 
				}else {
					System.out.println("You Don't have the card required to build in this city");
				}
			}else {
				System.out.println("You are not currently present in the city you wish to build in");
			}	
		}	
	}
	// method to print the cards in the players hand
	private static void printCardInHand() {
		for (int cardNumber=0; cardNumber<(cardInHands.get(currentUser).size()); cardNumber++) {
			System.out.println(cardInHands.get(currentUser).get(cardNumber));
		}
	}
	// method for the user to move to adjacent city
	private static void moveToAdjacentCity() {
		boolean moved = false;
		while (!moved) {
			System.out.println ("Type where you'd like to move.");
			String userInput =shellInput.nextLine();;
			int cityToMoveTo = getCityOffset(userInput); // get the city number
		
			if (cityToMoveTo == -1) {
				System.out.println(userInput + " is not a valid city. Try one of these.");
				printAdjacentCities(); // if not found print these
			}
		
			//If adjacent move the user, if not print an error.
			else if (citiesAdjacent(userLocation[currentUser],cityToMoveTo)) {
				System.out.println("The user has moved from " +
					cities[userLocation[currentUser]] + " to " + 
					cities[cityToMoveTo] + ".");
				userLocation[currentUser] = cityToMoveTo;
				moved = true;
			}
			else {
				System.out.println ("You can't move to " + userInput + ".  Try one of these.");
				printAdjacentCities();
				}
		}
	}
	// method to fly to any city
	private static void flyToAnyCity() {
		boolean moving = false;
		int currentLocation = userLocation[currentUser];  // get the current location
		
		while (!moving) {
			System.out.println ("Type where you'd like to move."); 
			String inputUser = shellInput.nextLine();
			int cityToMoveTo = getCityOffset(inputUser); // get the integer related to that city
			// if the city entered is not found print error message
			if (cityToMoveTo == -1) {
				System.out.println(inputUser + " is not a valid city.");
				
			}
			// check if the user has the card matching his current location then fly to the city entered
			else if (cardInHands.get(currentUser).contains(cities[currentLocation])) {
				System.out.println("The user has moved from " + cities[userLocation[currentUser]] + " to " + cities[cityToMoveTo] + ".");
				userLocation[currentUser] = cityToMoveTo;
				playerDeckDiscard.add(cities[currentLocation]);
				cardInHands.get(currentUser).remove(cities[currentLocation]);
				moving = true;
			}
			// else print the error message
			else {
				System.out.println ("You can't move to " + inputUser);
				System.out.println("You need to have a card matching your current city");
			}
		}

	}
	// method fly to city that matches the card in your hands
	private static void flyToCity() {
		boolean moved = false;
		
		while (!moved) {
			System.out.println ("Type where you'd like to move.");
			String userInput = shellInput.nextLine();
			int cityToMoveTo = getCityOffset(userInput); // get the integer related to that city
			// if the city entered is not found print error message
			if (cityToMoveTo == -1) {
				System.out.println(userInput + " is not a valid city");
				
			}
			// check if the user has the card matching the city he want to move to if so move to the city else print error message
			else if (cardInHands.get(currentUser).contains(userInput)) {
				System.out.println("The user has moved from " + cities[userLocation[currentUser]] + " to " + cities[cityToMoveTo] + ".");
				userLocation[currentUser] = cityToMoveTo;
				moved = true;
				playerDeckDiscard.add(userInput);
				cardInHands.get(currentUser).remove(userInput);
			}
			else {
				System.out.println ("You can't move to " + userInput);
				System.out.println("You can only move to cities corresponding with the card you have");
			}
		}

	}
	// method move from one research centre to another
	private static void flyToResearchCentre() {
		boolean moved = false;
		int currentLocation = userLocation[currentUser];
		
		while (!moved) {
			System.out.println ("Type where you'd like to move.");
			String userInput = shellInput.nextLine();
			int cityToMoveTo = getCityOffset(userInput); // get the integer related to that city
			// if the city entered is not found print error message
			if (cityToMoveTo == -1) {
				System.out.println(userInput + " is not a valid city.");
				
			}
			// check if the users current location has a research centre and check if the location he wishes to move to has a research centre
			else if (researchStation.contains(cities[currentLocation])) {
				if (researchStation.contains(userInput)) {
					System.out.println("The user has moved from " + cities[currentLocation] + " to " + cities[cityToMoveTo] + ".");
					userLocation[currentUser] = cityToMoveTo;
					moved = true;
				}
				// if not print an error message
				else {
					System.out.println ("You can't move to " + userInput);
					System.out.println("Both Cities Need to have a Research Station");
				}
			}
			// if not print an error message
			else {
				System.out.println ("You can't move to " + userInput);
				System.out.println("Both Cities Need to have a Research Station");
			}
		}

	}
	
	// method that counts the action done by the player
	private static void actionDone() {
		if (action<3) { // every player gets turns to take an action
			action++;
		}
		// after the four actions of the user the following is done
		else {
			//after every action two cards are drawn from the player deck
			for(int numberOfCards=0; numberOfCards<2; numberOfCards++) {
				int int_random = rand.nextInt(playerDeck.size()); 
				cardInHands.get(currentUser).add(playerDeck.get(int_random));
				playerDeck.remove(int_random);			
			}
			// a card is drawn from the infection deck to infect the cities
			int random_num = rand.nextInt(infectionDeck.size());
			String cityInfect = infectionDeck.get(random_num);

			if (diseaseCubes[getCityOffset(cityInfect)]>0) {
				diseaseCubes[getCityOffset(cityInfect)]++;
			}else {
				diseaseCubes[getCityOffset(cityInfect)]=1;
			}
			infectionDeckDiscard.add(cityInfect);
			infectionDeck.remove(random_num);
			
			// reset the user action
			action = 0;
			currentUser++;
			currentUser%=NUMBER_USERS;
			System.out.println("It's now " + userNames[currentUser] + " turn.");
			
		}
	}
	
	// method discover cure
	private static void discorveCure() {
		// variables to keep count of the coloured cards in the users hand
		int redCards = 0;
		int blueCards = 0;
		int yellowCards = 0;
		int blackCards = 0;
		// array list to store the location of the coloured cards in the users hand
		ArrayList<Integer> redCardIndex =  new ArrayList<Integer>();
		ArrayList<Integer> blueCardIndex =  new ArrayList<Integer>();
		ArrayList<Integer> yellowCardIndex  =  new ArrayList<Integer>();
		ArrayList<Integer>  blackCardIndex =  new ArrayList<Integer>();

		// if the player is currently in a research station
		if (researchStation.contains(cities[userLocation[currentUser]])) {
			// and has 5 cards
			if (cardInHands.get(currentUser).size()>5) {
				// then check how many cards he has by colour
				for(int cardNumber=0; cardNumber<cardInHands.get(currentUser).size(); cardNumber++) {
					boolean containsRedCard = Arrays.asList(redGroup).contains(cardInHands.get(currentUser).get(cardNumber)); 
					boolean containsBlueCard = Arrays.asList(blueGroup).contains(cardInHands.get(currentUser).get(cardNumber)); 
					boolean containsyellowCard = Arrays.asList(yellowGroup).contains(cardInHands.get(currentUser).get(cardNumber)); 
					boolean containsBlackCard = Arrays.asList(blackGroup).contains(cardInHands.get(currentUser).get(cardNumber));
					if(containsRedCard) {
						redCards++;
						redCardIndex.add(cardInHands.get(currentUser).indexOf(cardInHands.get(currentUser).get(cardNumber)));
					}
					else if(containsBlueCard) {
						blueCards++;
						blueCardIndex.add(cardInHands.get(currentUser).indexOf(cardInHands.get(currentUser).get(cardNumber)));
					}
					else if(containsyellowCard) {
						yellowCards++;
						yellowCardIndex.add(cardInHands.get(currentUser).indexOf(cardInHands.get(currentUser).get(cardNumber)));
					}
					else if(containsBlackCard) {
						blackCards++;
						blackCardIndex.add(cardInHands.get(currentUser).indexOf(cardInHands.get(currentUser).get(cardNumber)));
					}
				}// close for loop
				// if the user is currently in the red city
				if (Arrays.asList(redGroup).contains(cities[userLocation[currentUser]])) {
					if (curedDiseases.contains("Red")) { // if the disease has already been cured then 
						System.out.println("The Disease has already been Cured");
					}else { // if not
						if (redCards>=5) { // check if he has 5 red cards
							for (int count=0; count<redCards; count++) { // remove the cards from the users hand and discard them
								playerDeckDiscard.add(cardInHands.get(currentUser).get(redCardIndex.get(count)));
								cardInHands.get(currentUser).remove(redCardIndex.get(count));
							}
							curedDiseases.add("Red"); // cure the disease
							System.out.println("you have cured the red disease");
						}
						else { // else error message
							System.out.println("you need 5 red cards to cure a disease");
						}
					}
				}
				// if the user is currently in the blue city
				else if (Arrays.asList(blueGroup).contains(cities[userLocation[currentUser]])) {
					if (curedDiseases.contains("Blue")) { // if the disease has already been cured then 
						System.out.println("The Disease has already been Cured");
					}else { // if not
						if (blueCards>=5) { // check if he has 5 blue cards
							for (int count=0; count<blueCards; count++) { // remove the cards from the users hand and discard them
								playerDeckDiscard.add(cardInHands.get(currentUser).get(blueCardIndex.get(count)));
								cardInHands.get(currentUser).remove(blueCardIndex.get(count));
							}
							curedDiseases.add("Blue");  // cure the disease
							System.out.println("you have cured the blue disease");
						}
						else { // else error message
							System.out.println("you need 5 blue cards to cure a disease");
						}
					}
				}
				// if the user is currently in the yellow city
				else if(Arrays.asList(yellowGroup).contains(cities[userLocation[currentUser]])) {
					if (curedDiseases.contains("Yellow")) { // if the disease has already been cured then 
						System.out.println("The Disease has already been Cured");
					}else { // if not
						if (yellowCards>=5) { // check if he has 5 yellow cards
							for (int count=0; count<yellowCards; count++) { // remove the cards from the users hand and discard them
								playerDeckDiscard.add(cardInHands.get(currentUser).get(yellowCardIndex.get(count)));
								cardInHands.get(currentUser).remove(yellowCardIndex.get(count));
							}
							curedDiseases.add("Yellow"); // cure the disease
							System.out.println("you have cured the yellow disease");
						}
						else {
							System.out.println("you need 5 yellow cards to cure a disease");
						}
					}
				}
				// if the user is currently in the black city
				else if(Arrays.asList(blackGroup).contains(cities[userLocation[currentUser]])){
					if (curedDiseases.contains("Black")) { // if the disease has already been cured then 
						System.out.println("The Disease has already been Cured");
					}else { // if not
						if (blackCards>=5) { // check if he has 5 yellow cards
							for (int count=0; count<yellowCards; count++) { // remove the cards from the users hand and discard them
								playerDeckDiscard.add(cardInHands.get(currentUser).get(blackCardIndex.get(count)));
								cardInHands.get(currentUser).remove(blackCardIndex.get(count));
							}
							curedDiseases.add("Black"); // cure the disease
							System.out.println("you have cured the black disease");
						}
						else {
							System.out.println("you need 5 black cards to cure a disease");
						}
					}
				}
			} // close second if
			else { //  error message if you don't have 5 cards
				System.out.println("You need 5 cards to cure the disease");
			}
		} // close first if
		else { // error message if you are not in the research centre
			System.out.println("You Need to be in a City with a research centre");
		}	
	}// close method
	
	private static void infectCities() { // infect cities at the start of the game
		for(int diseaseCount=0; diseaseCount<9;diseaseCount++) {
			if(diseaseCount<3) { // for the first 3 cities
				diseaseCubes[diseaseCount] = 3; // add 3 cubes
				
			}
			else if(diseaseCount<6) { //for the second 3 cities
				diseaseCubes[diseaseCount] = 2; // add 2 cubes
			}
			else if(diseaseCount<9) { //for the third 3 cities
				diseaseCubes[diseaseCount] = 1; // add 1 cube
			}
			infectionDeckDiscard.add(infectionDeck.get(diseaseCount));  // discard the card
			infectionDeck.remove(diseaseCount); //  remove the cards form the infection deck
		}
	}
	
	// method to remove the cubes
	private static boolean removeCube() {
		int currentUserLocation = userLocation[currentUser]; //  get user location
		if (diseaseCubes[currentUserLocation] > 0) // if the diseases in that location is greater that 0
			{
			diseaseCubes[currentUserLocation]--; // remove one cube
			System.out.println("There are " + diseaseCubes[currentUserLocation] + " left");
			return true;
			}
		else { //  if not print error message
			System.out.println("The space you're on has no disease cubes.");
			return false;
		}
	}
	
	// distributing cards at the start of the game
	private static void playerCardDistribution() {
		boolean start = true;
		int cityNumber = 48;
		
		if(start) { // loop through the number of players
			for(int playernumber=0; playernumber<userNames.length; playernumber++) {
				ArrayList<String> list1 = new ArrayList<String>(); 
				// give them 2 random cards each
				for(int numberOfCards=0; numberOfCards<2; numberOfCards++) {
					int int_random = rand.nextInt(cityNumber); 
					list1.add(playerDeck.get(int_random));
					playerDeck.remove(int_random);		
					cityNumber--;	
					
				}
				cardInHands.add(list1); // add the cards to their hands
				
			}
		}
	}
	
	
	
	//print out the integer associated with what the user typed.
	private static void echoUserInput(int userInput) {
		System.out.println("The user chose:"+ userInput);
	}
	
	//Print out the list of all the cities.
	private static void printCities() {
		System.out.println(numberCities + " Cities.");
		for (int cityNumber = 0; cityNumber < numberCities; cityNumber++) {
			System.out.println(cities[cityNumber]);
		}
	}
	
	//Print out the full list of connections.
	private static void printConnections( ) {
		System.out.println(numberConnections + " Connections.");
		for (int connectionNumber = 0; connectionNumber < numberConnections; connectionNumber++) {
			String firstCity = cities[connections[0][connectionNumber]];
			String secondCity = cities[connections[1][connectionNumber]];
			System.out.println(firstCity + " " + secondCity);
		}
	}
	// print all users locations
	private static void printUserLocations() {
		System.out.println("The current user is " + userNames[currentUser]);
		for (int userNumber = 0; userNumber<NUMBER_USERS;userNumber++) {
			int printUserLocation = userLocation[userNumber];
			
			System.out.println (userNames[userNumber] + " is in " + cities[printUserLocation]);
		}
	}
	
	//print cities with research station
	private static void printResearchStation() {
		int count = 1;
		System.out.println("The Research Stations are built in the cities below");
		for(int cityNumber = 0; cityNumber<researchStation.size(); cityNumber++) {
			System.out.println( count+ "."+researchStation.get(cityNumber));
			count++;
		}
	}
	
	
	//Print out the cities adjacent to the userLocation
	private static void printAdjacentCities () {
		for (int cityNumber = 0; cityNumber < numberCities; cityNumber++) {
			if (citiesAdjacent(userLocation[currentUser],cityNumber)) {
				System.out.println(cities[cityNumber]);
			}
		}
	}
	// print cities with infections and the number of cubes in each city
	private static void printInfectedCities() {
		for (int cityNumber = 0;  cityNumber < numberCities; cityNumber ++) {
			if (diseaseCubes[cityNumber] > 0) {
				System.out.println(cities[cityNumber] + " has " + diseaseCubes[cityNumber] + " cubes.");
			}
		}
	}
	
	// process the user command by calling the associated method
	private static boolean processUserCommand(int userInput) {
		echoUserInput(userInput);
			

		if (userInput == QUIT) {
			System.out.println("You have Quit the Game");
			return true;
		}
		else if (userInput == PRINT_LOCATION)
			printUserLocations();
		else if (userInput == PRINT_CITIES)
			printCities();
		else if (userInput == PRINT_CONNECTIONS)
			printConnections();
		else if (userInput == PRINT_ADJACENT_CITIES)
			printAdjacentCities();
		else if (userInput == PRINT_DISEASES)
			printInfectedCities();
		else if (userInput == PRINT_RESEARCHSTATION)
			printResearchStation();
		else if (userInput == PRINT_BUILDRESEARCHSTATION) {
			buildResearchStation();
			actionDone();
		}
			
		else if (userInput == REMOVEDISEASECUBE) {
			if(removeCube()) {
				actionDone();
			}
		}
		else if (userInput == MOVETOCONNECTEDCITY) {
			moveToAdjacentCity();
			actionDone();
		}
		else if (userInput == MOVETORESEARCHCENTRE) {
			flyToResearchCentre();
			actionDone();
		}
		else if (userInput == MOVETOANYCITY) {
			flyToAnyCity();
			actionDone();
		}
		else if (userInput == MOVETOCARDCITY) {
			flyToCity();
			actionDone();
		}
		else if (userInput == VIEWCARDS)
			printCardInHand();
		else if (userInput == DISCOVERCURE) {
			discorveCure();
			actionDone();
		}
		else if (userInput == AGENT) {
			agent();
			actionDone();
		}
		return false;
	}
	// method for the agent
	public static void agent() {
		// print the functions of the agent
		System.out.println ("This is the agent: ");
		System.out.println ("Enter the Number of your choice");
		System.out.println ("1. How many cards are left in the deck");
		System.out.println ("2. How many coloured cards in deck");
		System.out.println ("3. Show cards which are left in the deck");
		int optionInput = shellInput.nextInt();
		
		// show the player deck sie if the option is one
		if (optionInput == 1) {
			System.out.println("There are " + playerDeck.size()+ " Cards left in the deck.");
		}
		// show the card types that you have in your hand
		else if (optionInput == 2) {
			chancesOfTypeCard();
		}
		// show the remaining cards in the deck
		else if (optionInput == 3) {
			showCards();
		}
		else { // else print error
			System.out.println("Invalid choice entered");
		}
	}
	// show the remaining cards in the deck for the agent to print
	public static void showCards() {
		System.out.println("Cards which are in the deck");
		for(int i=0; i<playerDeck.size(); i++) {
			System.out.println(playerDeck.get(i));
		}
	}
	// shows the type of cards in your hand for the agent to print 
	public static void chancesOfTypeCard() {
		System.out.println ("Enter Colour card number:");
		System.out.println ("1. Red");
		System.out.println ("2. Yellow");
		System.out.println ("3. Blue");
		System.out.println ("4. Black");
		int optionInput = shellInput.nextInt();
			// print all the red cards
		if(optionInput == 1) {
			System.out.println("Red is");
				
			for(int cardNumber=0; cardNumber<redGroup.length;cardNumber++) {
				System.out.println(redGroup[cardNumber]);
			}
				
		}
		// print all the yellow cards
		else if(optionInput == 2) {
			System.out.println("Yellow is:");
			for(int cardNumber=0; cardNumber<yellowGroup.length;cardNumber++) {
				System.out.println(redGroup[cardNumber]);
			}		
		}
		// print all the blue cards
		else if(optionInput == 3) {
			System.out.println("Blue is");
			for(int cardNumber=0; cardNumber<blueGroup.length;cardNumber++) {
				System.out.println(redGroup[cardNumber]);
			}
				
		}
		// print all the black cards
		else if(optionInput == 4) {
			System.out.println("Black is");
			for(int cardNumber=0; cardNumber<blackGroup.length;cardNumber++) {
				System.out.println(redGroup[cardNumber]);
			}
		}
			else {
				System.out.println("Invalid choice entered");
			}

			
	}
	
	// check if the user has won or lost the game
	private static boolean gameStatus() {
		// IF THE DISEASE ARRAY HAS ALL FOUR DISEASES THEN
		if (curedDiseases.size()==4) { // if you have cured all the disease then you win
			System.out.println("You Have Won The Game. YOU CURED ALL THE DISEASES");
			return true;
		}
		// if the player deck is empty
		else if (playerDeck.size()==0) {
			System.out.println("You Have Failed The Game. The PlayerDeck is Empty");
			return true;
		}
		// if the infection deck is empty
		else if (infectionDeck.size()==0) {
			System.out.println("You Have Failed The Game. The InfectionDeck is Empty");
			return true;
		}
		return false;
	}
	// main method
	public static void main(String[] args) {
		boolean gameDone = false;
		// print game information
		System.out.println("Hello Welcome to Pandemic!!!");
		System.out.println("Pandemic is a board game where you need to solve dieases");
		System.out.println("There are four players to this Game. Bob, Al, Dav, Dan");
		System.out.println("The First Player is Al");
		// call methods
		printActions();
		readCityGraph();
		infectCities();
		defaultResearchStation();
		playerCardDistribution();
	
		
		// check if the game is not done or the user has not won or lost the game, if not process the games command
		while (!gameDone) {
			if (gameStatus()) {
				gameDone = true;
			}
			else {
				int userInput = getUserInput();	
				gameDone = processUserCommand(userInput);
			}
			
		}
	}

}
