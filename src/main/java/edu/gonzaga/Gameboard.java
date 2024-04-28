package edu.gonzaga;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Gameboard {
    
    public ArrayList<City> cityList; 
    private ArrayList<Cure> cureList;
    private ArrayList<Player> playerList;

    private Deck playerDeck; // just the city and event cards
    private Deck infectionDeck; // just the infection cards

    private Queue<Integer> infectionRate;
    private Player currentPlayerTurn;
    private Integer numOfResearchStations;
    private Boolean canBuildResearchStation;
    private Integer outbreakCount;

    private final static Integer MAX_RESEARCH_STATIONS = 6;
    private final static Integer MAX_INFECTION_RATE = 4;
    private final static Integer MAX_OUTBREAKS = 8;
    private final static Boolean DEFAULT_CAN_BUILD_SETTING = true;


    public Gameboard(ArrayList<City> newCityList, ArrayList<Cure> newCureList, ArrayList<Player> newPlayerList, Deck newPlayerDeck, Deck newInfectionDeck) {
        this.cityList = newCityList;
        this.cureList = newCureList;
        this.playerList = newPlayerList;
        this.playerDeck = newPlayerDeck;
        this.infectionDeck = newInfectionDeck;
        this.currentPlayerTurn = this.playerList.get(0);

        this.canBuildResearchStation = DEFAULT_CAN_BUILD_SETTING;

        this.infectionRate = new LinkedList<Integer>();
        setInfectionQueue();
    }

    /**
     * Determines if you can build more research stations - must have less than the maximum (6) number
     * @author Izzy T
     */
    public void setCanBuildResearchStation() {
        this.canBuildResearchStation = (this.numOfResearchStations < MAX_RESEARCH_STATIONS);
    }

    /**
     * Returns if you can build more research stations
     * @return true if you can build research stations, false if you have reached the max
     * @author Izzy T
     */
    public boolean getCanBuildResearchStation() {
        setCanBuildResearchStation();
        return this.canBuildResearchStation;
    }

    /**
     * Builds research station in passed in City IF there are less than the max number of research stations already built; increases counter of research stations by 1
     * @param city the City that you want to build a research station on
     * @author Izzy T
     */
    public void buildResearchStation(City city) {
        if (canBuildResearchStation) {
            city.addResearchStation();
            numOfResearchStations++;
        }
        else {
            System.out.println("You have exceeded the amount of research stations that can be built. Please remove one before building another.");
        }
    }

    /**
     * Removes a research station from a city, and decreases the counter for research stations by 1
     * @param city the City that you want to remove a research station from
     * @author Izzy T
     */
    public void removeResearchStation(City city) {
        city.removeResearchStation();;
        numOfResearchStations--;
    }

    /**
     * Gets the deck of player cards
     * 
     * @return A deck of BasicCards, EpidemicCards, and EventCards
     * @author Aiden T
     */
    public Deck getPlayerDeck() {
        return this.playerDeck;
    }

    /**
     * Gets the deck of infection cards
     * 
     * @return A deck of the Infection cards
     * @author Izzy T
     */
    public Deck getInfectionDeck() {
        return this.infectionDeck;
    }

    /**
     * Gets the current infection rate on the board (stored in a queue)
     * @return current infection rate 
     * @author Izzy T
     */
    public Integer getCurrentInfectionRate() {
        return infectionRate.peek();
    }

    /**
     * Pops value off of the top of the queue and next infection rate becomes the top one
     * Used during an epidemic 
     * @author Izzy T
     */
    public void changeInfectionRate() {
        // make sure there is something to delete in the infection rate queue
        if (!infectionRate.isEmpty()) {
            infectionRate.remove();
        }
        // if nothing exists after removing that infection rate, manually make it the maximum value so it stays at that level forever 
        if (infectionRate.isEmpty()) {
            infectionRate.add(MAX_INFECTION_RATE);
        }
    }

    /**
     * Gets a list of the cures for each color
     * 
     * @return An ArrayList of 4 cures
     * @author Aiden T
     */
    public ArrayList<Cure> getCures() {
        return this.cureList;
    }

    /**
     * Gets the player who is currently taking a turn
     * 
     * @return The player that's taking a turn
     * @author Aiden T
     */
    public Player getCurrentTurnPlayer() {
        return this.currentPlayerTurn;
    }

    /**
     * Draws the infectionRate amount of cards from the infection pile and infects those cities. Handles outbreaks and increases counter if needed.
     * @author Izzy T
     */
    public void takeInfectionTurn() {
        Integer cardsToDraw = getCurrentInfectionRate();
        // draw as many cards as the infection rate at start of turn; add an infection cube each time 
        for (int i = 0; i < cardsToDraw; i++) {
            BasicCard cityCard = (BasicCard)infectionDeck.drawCard();
            City tempCity = cityCard.getCity();
            Integer numOutbreaks = tempCity.addInfectionCube();
            // update how many outbreaks there have been in the game 
            outbreakCount += numOutbreaks;
            // change infection rate for every outbreak that occurs after adding cube
            for (int j = 0; j < numOutbreaks; j++) {
                this.changeInfectionRate();
            }
            infectionDeck.discardCard(cityCard);
        }
    }

    /**
     * Needs to be called after a player has finished their turn to change this.currentPlayerTurn variable.
     * 
     * @Author Aiden T
     */
    public void endPlayerTurn() {
        Player rotatePlayer = this.playerList.get(0);
        this.playerList.remove(0);
        this.playerList.add(rotatePlayer);

        this.currentPlayerTurn = this.playerList.get(0);
    }

    /**
     * Helper function for setting all of the values in the Queue-style linked list for keeping track of current infection rates
     * @author Izzy T
     */
    private void setInfectionQueue() {
        infectionRate.add(2);
        infectionRate.add(2);
        infectionRate.add(2);
        infectionRate.add(3);
        infectionRate.add(3);
        infectionRate.add(4);
        infectionRate.add(4);
    }



}
