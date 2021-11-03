/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2018 Alvaro Monge <alvaro.monge@csulb.edu>
 *
 */

package csulb.cecs323.app;

/**
 * imports necessary for current project
 */
import csulb.cecs323.model.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * A simple application to demonstrate how to persist an object in JPA.
 * This is for demonstration and educational purposes only.
 * Originally provided by Dr. Alvaro Monge of CSULB, and subsequently modified by Dave Brown.
 */
public class Library {
   /**
    * You will likely need the entityManager in a great many functions throughout your application.
    * Rather than make this a global variable, we will make it an instance variable within the Library
    * class, and create an instance of CarClub in the main.
    */
   private EntityManager entityManager;

   /**
    * Default input scanner variable for user input into database
   */
   private static Scanner input = new Scanner(System.in);

   /**
    * The Logger can easily be configured to log to a file, rather than, or in addition to, the console.
    * We use it because it is easy to control how much or how little logging gets done without having to
    * go through the application and comment out/uncomment code and run the risk of introducing a bug.
    * Here also, we want to make sure that the one Logger instance is readily available throughout the
    * application, without resorting to creating a global variable.
    */
   private static final Logger LOGGER = Logger.getLogger(Library.class.getName());

   /**
    * The constructor for the Library class to be utilized throughout the entire application
    * @param manager    The EntityManager that we will use.
    */
   public Library(EntityManager manager) { this.entityManager = manager; } // end Library constructor

   /**
    * Main Library program to run upon execution.
    * Simulates entity relationship model provided by Dave Brown
    */
   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("Library");
      EntityManager manager = factory.createEntityManager();

      // Create an instance of Library and store our new EntityManager as an instance variable.
      Library library = new Library(manager);

      EntityTransaction tx = manager.getTransaction();

      tx.begin();

      System.out.println("Welcome to JPA Books!");
      //This variable is used to keep the user inside of the program
      boolean userLeaves = false;
      while(!userLeaves) {
         System.out.println("Please select what you want to do today. Make absolutely sure that you enter '1', and nothing more. " +
                 "Otherwise, you'll exit the entire program.\n" +
                 "1. Add New Object\n" +
                 "2. List all the Information about a Specific Object\n" +
                 "3. Delete a Book\n" +
                 "4. Update a Book\n" +
                 "5. List primary keys of all rows\n" +
                 "Anything Else: Exit Program");
         //Used to determine where in the program we go based on input
         String userChoice = input.nextLine();
         switch (userChoice){
            case "1":
               System.out.println("Add New Object\n" +
                       "1. Authoring Entity Instance\n" +
                       "2. Add a new Publisher\n" +
                       "3. Add a new Book");
               userChoice = input.nextLine();
               switch (userChoice){
                  case "1":
                     library.authoringMenu();
                     break;
                  case "2":
                     library.addPublisher();
                     break;
                  case "3":
                     library.addBook();
                     break;
               }//end of switch case for adding a new object
               break;
            case "2":
               System.out.println("List all the Information about a Specific Object\n" +
                       "1. Publisher\n" +
                       "2. Book\n" +
                       "3. Writing Group\n");
               userChoice = input.nextLine();
               switch (userChoice){
                  case "1":
                     System.out.println("List Publisher information");
                     System.out.println( library.pickPublisher().toString());
                     break;
                  case "2":
                     System.out.println("List Book information");
                     System.out.println(library.pickBook().toString());
                     break;
                  case "3":
                     System.out.println("List Writing Group Information");
                     //this is a lot larger simply because we had methods directly for ALL authoring entities, and not specifically for writing groups
                     boolean userCheck = false;
                     while(!userCheck) {
                        List<WritingGroups> writingGroups = library.entityManager.createNamedQuery("ReturnAllWriting",
                                WritingGroups.class).getResultList(); //gets all of the writing groups from the database
                        for (WritingGroups writing : writingGroups) { //displays all writing group's email
                           System.out.println("Writing Email: " + writing.getEmail());
                        }//end of for loop that displays all writing emails
                        System.out.println("Please enter in one of the writing emails displayed");
                        String userEmail = input.nextLine();//user puts in the email
                        //author is supposed to save whatever rows it returns for userEmail is inside the tables of writing groups
                        List<WritingGroups> author = library.entityManager.createNamedQuery("ReturnWriting",WritingGroups.class).setParameter(1,userEmail).getResultList();
                        if(author.size()==0){//if its empty, it means theres no row with that user's email
                           System.out.println("Sorry, you entered an invalid writing email.");
                        }//end of if statement that checks if there is any person under that email
                        else{//if there is, we print out the author details
                           userCheck = true;
                           System.out.println(author.get(0).toString());
                        }//end of else statement that prints out author details
                     }//end of while loop that forces user to select a valid email
                     break;
               }//end of switch case of selecting to see a specific object
               break;
            case "3":
               System.out.println("Delete a Book");
               library.deleteBook();
               break;
            case "4":
               System.out.println("Update a Book");
               library.updateBook();
               break;
            case "5":
               System.out.println("List primary keys of all rows\n" +
                       "1. Publisher Keys\n" +
                       "2. Books Keys\n" +
                       "3. Authoring Entities Keys (Includes Authoring Entity Type)");
               //determines which primary keys of the table we display
               userChoice = input.nextLine();
               switch (userChoice){
                  case "1":
                     System.out.println("List Publisher Keys");
                     library.displayAllPublisherPK();
                     break;
                  case "2":
                     System.out.println("List Books Keys");
                     library.displayAllBookPK();
                     break;
                  case "3":
                     System.out.println("List Authoring Entities Keys");
                     library.displayAllAuthoring_EntitiesPK();
                     break;
               }//end of switch case that displayed the PK of the different tables
               break;

            default:userLeaves = true;
               break;
         }//end of the switch case that determines where in the program we go from the start of the program; the first menu we see
      }//end of the while loop that forces the user to stay in the program unless the user puts in anything that isn't 1-5

      tx.commit();

      System.out.println("Completed Satisfactorily");

      LOGGER.fine("End of Transaction");

   } // End of the main method

   /**
    * Function to display user menu options to add a new authoring entity instance.
    * Based on the input the user will be able to add any of the following entities:
    * Writing Group, Individual Author, Ad Hoc Team, or an Individual Author to an Existing Ad Hoc Team.
    */
   public void authoringMenu(){
      //makes sure the user can't leave this page yet
      boolean userLeaves = false;
      while(!userLeaves) {
         System.out.println("Please select one of the options. Be sure to ONLY input '1' if you want the first option;" +
                 "otherwise, you will leave this menu\n" +
                 "1. Add A New Writing Group\n" +
                 "2. Add a New Individual Author\n" +
                 "3. Add a New Ad Hoc Team\n" +
                 "4. Add an Author to an Ad Hoc Team\n" +
                 "Anything Else: Exit Authoring Menu");
         //determines which adding of entity we go to, or leave the menu
         String userChoice = input.nextLine();
         switch (userChoice) {
            case "1": this.addWritingGroup();
               break;
            case "2": this.addIndividualAuthor();
               break;
            case "3": this.addAdHocTeam();
               break;
            case "4": this.addAuthorToAdHoc();
               break;
            default:
               userLeaves = true;
               break;
         }//end of switch case for navigation to adding to authoring entities
      }//end of while loop that will force the user to stay in the menu until they say so
   } // end of authoringMenu

   /**
    * Adds a Writing Group entity to the database utilizing JPA Annotations from associated classes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void addWritingGroup(){
      //this is to ensure the user doesn't leave this function until they enter a valid writing group
      boolean nameCheck = false;
      while(!nameCheck){
         System.out.println("What is the email of the writing group?");
         //get the email of the writing group to ensure that the email does not exist currently
         String userEmail = input.nextLine();
         try{
            List<Authoring_Entities> writingGroups = this.entityManager.createNamedQuery("ReturnAuthor",
                    Authoring_Entities.class).setParameter(1, userEmail).getResultList();//retrieves if there's any row inside with that email
            if (writingGroups.isEmpty()) {//if the query is empty, we can safely assume that the user can add the new writing group
               System.out.println("What is the name of the head writer?");
               String userHeadName = input.nextLine(); //get the head name
               System.out.println("What is the name of the authoring group?");
               String userName = input.nextLine(); //get the name of the authoring group
               System.out.println("What is the year that the group was formed?");
               try {
                  //we check if the input is actually an int, since we store it as one.
                  int userYear = input.nextInt();
                  input.nextLine();
                  ArrayList<WritingGroups> userWritingGroup = new ArrayList<WritingGroups>();//this is to match the createEntity format of the program
                  userWritingGroup.add(new WritingGroups(userEmail,userName,userHeadName,userYear));//append the newly generated writing group that the user made
                  this.createEntity(userWritingGroup);//add this entry into the entity manager
                  nameCheck= true;//gets us out once it has been successfully created
               }//end of try block that checks for the year being an integer and creating the object
               catch (Exception e){
                  System.out.println("Sorry, you entered in a bad entry for year. please try again.");
               }//end of catch block that gets the user's integer input
            } // end of if statement that checks for an existing writing group under that email
            else{//if it is not empty, that means we already have that email in the database
               System.out.println("Sorry, that email already exists in the Authoring Entity database, so please use a different email");
            }//end of else that tells the user that we cannot use that email
         }//end of the try block that tests to see if there's an issue with trying to get the query; not sure if needed, but we have it in case
         catch(Exception e){//catch any potential error
            System.out.println("Sorry, someone by that email already exists.");
         }//end of catch statement for issues related to query
      }//end of the while loop to make sure the user puts in a valid writing group entry
   } // end of addWritingGroup

   /**
    * Adds an Individual Author entity to the database utilizing JPA Annotations from associated classes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void addIndividualAuthor(){
      //forces user to create a valid individual author
      boolean nameCheck = false;
      while(!nameCheck) {
         System.out.println("What is the email of the new author?");
         //extracts user input for email
         String userEmail = input.nextLine();
         try{
            List<Authoring_Entities> individualAuthors = this.entityManager.createNamedQuery("ReturnAuthor",
                    Authoring_Entities.class).setParameter(1, userEmail).getResultList();//checks if a user with the user email exists in the table
            if (individualAuthors.size() == 0) {//if there is no user email, we prompt for the name of the author as well
               nameCheck= true;
               System.out.println("What is the name of the author?");
               String userName = input.nextLine();
               ArrayList<Individual_Authors> userAuthor = new ArrayList<Individual_Authors>(); //used to fit into createEnitity
               userAuthor.add(new Individual_Authors(userEmail,userName));//add the new author into author entity
               this.createEntity(userAuthor);//adds to entity manager
            } // end of if statement that checks if the email exists or not
            else{//if there is a user, we cannot add them to the entity manager because it would cause an error in the program
               System.out.println("Sorry, there is another authoring entity with that email.");
            }//end of else statement that checks if the user is empty
         }//end of try block that will attempt to create a new individual author
         catch(Exception e){
            System.out.println("Sorry, someone with that email already exists.");
         }//catch block ends in case searching for the email fails
      }//end of while loop that forces user to enter a valid entry
   } // end of addIndividualAuthor

   /**
    * Adds an Ad Hoc Team entity to the database utilizing JPA Annotations from associated classes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void addAdHocTeam(){
      //make sure that the user stays here until they make a valid ad hoc team
      boolean nameCheck = false;
      while(!nameCheck) {
         System.out.println("What is the email of the new adhocteam?");
         String userEmail = input.nextLine(); //gets the email of the new adhocteam
         try{
            List<Authoring_Entities> adHocTeamMember = this.entityManager.createNamedQuery("ReturnAuthor",
                    Authoring_Entities.class).setParameter(1, userEmail).getResultList();//checks if that email already exists or not
            if (adHocTeamMember.size() == 0) {//if there is no user email, we prompt for the name of the team as well
               nameCheck= true;
               System.out.println("What is the name of the Ad Hoc Team?");
               String userName = input.nextLine();
               ArrayList<Ad_Hoc_Teams_Members> adHocTeam = new ArrayList<Ad_Hoc_Teams_Members>();//used for createEntity
               adHocTeam.add(new Ad_Hoc_Teams_Members(userEmail,userName));//add a new adhoc team based on user input
               this.createEntity(adHocTeam);//add the entry into entity manager
            } // end of if statement that checks if there is a potential duplicate entry
            else{
               System.out.println("Sorry, that email already exists in the database.");
            }//end of else statement that is executed if the adhoc team email already exists
         }//end of try block that attempts to create a new ad hoc team
         catch(Exception e){
            System.out.println("Sorry, someone with that email already exists.");
         }//end of catch statement if the queries or entry would fail
      }//end of loop that forces user to add a new ad hoc team
   } // end of addHocTeam

   /**
    * Adds an individual author to an EXISTING Ad Hoc Team entity.
    * Subsequent to inclusion, the database utilizing JPA Annotations from associated classes is updated.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void addAuthorToAdHoc(){
      //ensure that user can't leave until they make a valid entry
      boolean nameCheck = false;
      while(!nameCheck) {
         List<Individual_Authors> individualAuthors = this.entityManager.createNamedQuery("ReturnAllIndividualAuthors",
                 Individual_Authors.class).getResultList();//gets all individual authors from the database
         for(Individual_Authors individual_author : individualAuthors){//prints out all the individual authors
            System.out.println("Individual Authors: "+individual_author.toString());
         }//end of printing out individual authors
         System.out.println("Which email of the author needs to be added to an ad hoc team?");
         String userEmail = input.nextLine();//prompt for user email for the individual author
         try{
            List<Individual_Authors> adHocTeamMember = this.entityManager.createNamedQuery("ReturnIndividualAuthor",
                    Individual_Authors.class).setParameter(1, userEmail).getResultList();//check to see if that individual author email is inside the database
            if (adHocTeamMember.size() != 0) {//if there is a row, we now prompt for the ad hoc team
               List<Ad_Hoc_Teams_Members> adHocTeamsMembers = this.entityManager.createNamedQuery("ReturnAllAdHocTeamMembers",
                       Ad_Hoc_Teams_Members.class).getResultList();//generates all ad hoc team emails
               for(Ad_Hoc_Teams_Members ad_hoc_teams_members : adHocTeamsMembers){//prints out the ad hoc team emails
                  System.out.println("Ad Hoc Team Members: "+ad_hoc_teams_members.toString());
               }//end of printing out ad hoc teams
               System.out.println("What is the email of the ad hoc team you want to add the author to?");
               String userAdHocEmail = input.nextLine();//prompt the user to enter in a valid ad hoc team email
               List<Ad_Hoc_Teams_Members> adHocTeam = this.entityManager.createNamedQuery("ReturnAdHocTeamMember",
                       Ad_Hoc_Teams_Members.class).setParameter(1, userAdHocEmail).getResultList();//use the user's ad hoc email and see if it exists or not
               if(adHocTeam.size()!=0){//if it does exist, we check to see if there  is already an entry in the ad hoc table
                  if(adHocTeam.get(0).getIndividualAuthors().contains(adHocTeamMember.get(0))) {//this checks if there is already that individual author inside of the ad hoc team object
                     System.out.println("You already have an individual under that ad hoc team.");
                  }//end of checking if the adhocteam already has that individual author
                  else {//if there isn't that individual author inside of the ad hoc team list, we can add them
                     nameCheck= true;
                     adHocTeam.get(0).addIndividualAuthors(adHocTeamMember.get(0));//this will add the user's chosen individual author into the adhocteam object
                     this.createEntity(adHocTeam);//update the adhocteam table with the new individual author
                  }//end of else statement that will add the individual author to the ad hoc team if it doesn't already exist
               }//end of if statement that determines whether or not the adhocteam exists
               else{
                  System.out.println("Sorry, can't find anything related to the email of the ad hoc team");
               }//end of else statement that is executed if the adhoc team doesn't exist
            } // end of if statement that checks if the individual author does exist
            else{
               System.out.println("Sorry, we cannot find the individual author you entered");
            }//end of else statement if the individual author does not exist
         }//end of try block that attempts to add an individual author to the ad hoc team
         catch(Exception e){
            System.out.println("Sorry, we couldn't find anything related to the individual author.");
         }//end of catch block for issues related to the query
      }//end of while loop that forces the user to stay unless they add a valid author to the team
   } // end of addAuthorToAdHoc

   /**
    * Displays all Authoring Entities primary keys and their respective author type contained in the database
    * */
   public void displayAllAuthoring_EntitiesPK(){
      try {
         for (Authoring_Entities author : this.entityManager.createNamedQuery("ReturnAllAuthors", Authoring_Entities.class).getResultList()) {//iterate through all rows in the database
            System.out.println("Author Email: "+author.getEmail() + "   Author Writing Type: " + author.getDiscriminatorValue());//prints out the email and discriminator type
         }//end of displaying for loop
      }//end of try block that attempts to display all authors
      catch(Exception e){
         System.out.println("Sorry, you have no existing authors in the database.");
      }//this is to ensure that there won't be a null pointer with the assumption there is no authoring entity to print
   } // end of DisplayAllAuthoring_Entities

   /**
    * Allow user to select an existing Authoring Entity to utilize for other functionalities.
    * This function will be used in other methods such as addBook.
    * @return first index from List object containing desired Entity
    * @return null if an invalid publisher name has been selected
    */
   public Authoring_Entities pickAuthoringEntity(){
      //forces the user to pick an authoring entity
      boolean userChoice = false;
      while(!userChoice){
         this.displayAllAuthoring_EntitiesPK();//display the primary keys of all authoring entities and their type
         System.out.println("Please enter the email of the author you want to pick.");
         String userAuthor = input.nextLine();//prompt for the user email/author
         //author will attempt to grab an existing author in the database
         List<Authoring_Entities> author = this.entityManager.createNamedQuery("ReturnAuthor",Authoring_Entities.class).setParameter(1,userAuthor).getResultList();
         if(author.size()==0){//if there are not results, we tell them that the author doesnt exist
            System.out.println("Sorry, you entered an invalid author email.");
         }//end of if statement that checks if it exists in the database or not
         else{//if there are results, we grab the first index from author
            return author.get(0);
         }//end of else that will return the author that was chosen
      }//end of while loop that forces the user to enter data
      return null;//this is here because IntelliJ will not allow us to compile the program because this method doesn't have a return since our actual return is inside a conditional statement
   } // end of pickAuthoringEntity

   /**
    * Displays all the book's primary keys in the current database
    */
   public void displayAllBookPK(){
      try {//attempt to display all the primary keys
         for (Books book : this.entityManager.createNamedQuery("ReturnAllBooks", Books.class).getResultList()) {//for loop extracts the data in the Books table and prints them out
            System.out.println("Book ISBN: " + book.getISBN());//only gets the ISBN since this is for primary keys only
         }//end of displaying for loop
      }//end of try block
      catch(Exception e){
         System.out.println("Sorry, you have no existing books in the database.");
      }//end of catch if there are no books in the database
   } // end of displayAllBooks

   /**
    * Select a book from database based on user's input for ISBN
    * @return  null
    */
   public Books pickBook(){
      //forces user to pick a book
      boolean userChoice = false;
      while(!userChoice){
         this.displayAllBookPK();//display all the PK of books
         System.out.println("Please enter the ISBN of the book you would like");
         String userBook = input.nextLine(); //ask for ISBN of the book they want to choose
         List<Books> book = this.entityManager.createNamedQuery("ReturnBook",Books.class).setParameter(1,userBook).getResultList();//gets the query result based on user ISBN
         if(book.size()==0){//if there is nothing returned, they didn't have a valid book isbn
            System.out.println("Sorry, you entered an invalid ISBN for the book.");
         }//end of if statement that checks if nothing is returned
         else{
            return book.get(0);
         }//end of else statement that will return a book
      }//end of while loop that makes the user stay in the loop until they return a valid book
      //this is here because IntelliJ will not allow us to compile the program because this method doesn't have a return since our actual return is inside a conditional statement
      return null;
   } // end of pickBook

   /**
    * Adds a Book entity to the database utilizing JPA Annotations from associated classes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void addBook(){
      //force user to stay inside the loop until they create a valid book
      boolean bookCheck = false;
      while(!bookCheck) {
         System.out.println("What is the ISBN of the new book?");
         String userISBN = input.nextLine();//prompt for the isbn
         try{
            List<Books> book = this.entityManager.createNamedQuery("ReturnBook",
                    Books.class).setParameter(1, userISBN).getResultList();//returns the books with that ISBN name
            if (book.size() == 0) {//if the query result is empty, we can assume the user ISBN is not in the database
               bookCheck= true;
               System.out.println("What is the title of the publisher?");
               String userTitle = input.nextLine();//prompt for the title
               System.out.println("What year was this book published?");
               int userYear = input.nextInt();//prompt for the year it was made
               input.nextLine();
               Publishers userPublisher = this.pickPublisher();//we retrieve the publisher the user picks here
               Authoring_Entities userAuthor = this.pickAuthoringEntity();//we retrieve the authoring entity the user picks here
               ArrayList<Books> userBook = new ArrayList<Books>();//for createEntity
               userBook.add(new Books(userISBN,userTitle,userYear, userPublisher,userAuthor));//create the new Book entry
               this.createEntity(userBook);//store it into the entity manager
            } // end of if statement for creating a new book
            else{
               System.out.println("Sorry, that ISBN has already been used.");
            }//end of else statement if the ISBN already exists in the database
         }//end of try block that attempts to create a new book
         catch(Exception e){
            System.out.println("Sorry, but it seems like there exists a book with that title and publisher, or that title and author.");
         }//catches any issues related to adding a new book that violates the unique columns or an unexpected query issue
      }//end of while loop that traps the ser until they add a valid book to the entity manager
   } // end of addBook

   /**
    * Deletes a Book entity to the database utilizing JPA Annotations from associated classes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void deleteBook(){
      List<Books> booksList = this.entityManager.createNamedQuery("ReturnAllBooks", Books.class).getResultList();//gets all the books from the database
      if(booksList.size()>0) {//with the assumption that there are books, we will go here
         boolean userChoice = false;
         //force the user to delete a book
         while (!userChoice) {
            for (Books book : booksList) {//prints out all available books
               System.out.println("Books: "+book.toString());
            }//end of displaying for loop
            System.out.println("Please enter the ISBN of the book you want to delete.");
            String userName = input.nextLine();//prompt for isbn
            try {
               List<Books> book = this.entityManager.createNamedQuery("ReturnBook",
                       Books.class).setParameter(1, userName).getResultList();//gets all the books with the ISBN
               if (book.size() == 0) {//if this is empty, we cannot delete a non existent row
                  System.out.println("Sorry, that ISBN doesn't exist in our tables. Please input one of the ISBN's displayed.");
               }//end of if statement that checks if there is no ISBN in that table
               else {
                  System.out.println("ISBN has been successfully deleted.");
                  this.entityManager.remove(entityManager.find(Books.class, userName));//this line will remove the book through the entity manager
                  userChoice = true;
               }// end of else statement if there is a book with that ISBN, where we then delete it
            }//end of trying to delete a book
            catch (Exception e) {
               System.out.println("It seems like you entered an invalid ISBN. Please input the correct ISBN from the list we provided.");
            }//this is here in case we somehow attempt to delete a non existent book, or the ISBN causes a crash in the query
         }//end of loop that traps user until they delete a book
      }//end of if statement that checks if there are books
      else{//if there are no books in the database, we go here
         System.out.println("Sorry, but we don't have any books stored in our database. Add new books to remove some.");
      }//end of else statement that tells the user there are no books in the database.
   } // end of deleteBook

   /**
    * Updates current desired Book entity in the database based on user input.
    * JPA Annotations from associated classes are then used with persistence module to make database changes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void updateBook(){
      List<Books> booksList = this.entityManager.createNamedQuery("ReturnAllBooks", Books.class).getResultList();//gets all of the books in the database
      if(booksList.size()>0) {//if there are books in the database, go here
         boolean userChoice = false;
         //force user to stay inside the function until they update a book
         while (!userChoice) {
            for (Books book : booksList) {//prints out books like by line
               System.out.println(book.toString());
            }//end of displaying loop
            System.out.println("Please enter the ISBN of the book you want to update.");
            String userName = input.nextLine();//prompt for isbn
            try {
               List<Books> book = this.entityManager.createNamedQuery("ReturnBook",
                       Books.class).setParameter(1, userName).getResultList();//tries to find the book the user enters
               if (book.size() == 0) {//if that book doesnt exist, tell them
                  System.out.println("Sorry, that ISBN doesn't exist in our tables. Please input one of the ISBN's displayed.");
               }//end of checking if the book exists
               else {//if it does exist, ask the user for an authoring entity
                  Books updateUserBook = this.entityManager.find(Books.class,userName);//this is the book the user wants to update
                  Authoring_Entities chosenAuthor = pickAuthoringEntity();//ask for an authoring entity
                  if(chosenAuthor.getEmail().equals(updateUserBook.getAuthoringName().getEmail())){//this statement is basically checking that the email for the old author is the same as the new one
                     System.out.println("Sorry, but that authoring entity is already the author for that book");
                  }//end if statement that checks if the emails are the same or not
                  else {//if the emails are different, we can then update the book
                     updateUserBook.setAuthoringName(chosenAuthor);//this line updates the book
                     System.out.println("The book has been successfully updated");
                     userChoice = true;
                  }//end of else statement that updates the book with the new email
               }//end of else statement that asks for the user's new authoring entity email
            }//end of try block that attempts to prompt and update for a book
            catch (Exception e) {
               System.out.println("It seems like you entered an invalid ISBN. Please input the correct ISBN from the list we provided.");
            }//should this fail at some point, we can assume that the ISBN is the reason the query broke
         }//end of while loop for user trying to update the book
      }//end of if statement that checks if there are books in the database currently
      else{
         System.out.println("Sorry, but we don't have any books stored in our database. Add new books to remove some.");
      }//end of else statement that tells the user theres not books in the database if the database for books is empty
   } // end of updateBook

   /**
    * Displays all publishers' primary keys in the current database
    */
   public void displayAllPublisherPK(){
      try {
         for (Publishers publisher : this.entityManager.createNamedQuery("ReturnAllPublisher", Publishers.class).getResultList()) {//grabs and iterates over all publishers in the database
            System.out.println("Publisher Name: " + publisher.getName());
         }//end of for loop to print out all of the publisher primary keys
      }//end of try block to display the publishers
      catch(Exception e){
         System.out.println("Sorry, there are no existing publishers inside the database.");
      }//end of catch block if there is no publishers in the database
   } // end of displayAllPublishers

   /**
    * Allow user to select a publisher from the existing Publishers table in our database
    * @return Publisher at first index from matched list
    * @return null with message if no Publisher match found
    */
   public Publishers pickPublisher(){
      boolean userChoice = false;
      //traps user until they pick a publisher
      while(!userChoice){
         this.displayAllPublisherPK();
         System.out.println("Please enter the name of the publisher you want to select.");
         String userPublisher = input.nextLine();//prompt for the name of the publisher the user wants
         //publisher gets the instance/object of the user's choice
         List<Publishers> publisher = this.entityManager.createNamedQuery("ReturnPublisher",Publishers.class).setParameter(1,userPublisher).getResultList();
         if(publisher.size()==0){//if this instance is empty, we tell the user that there isn't a publisher by that name
            System.out.println("Sorry, you entered an invalid publisher name.");
         }//end of if statement for checking if the publisher eixsts
         else{//if it does exist, return that publisher
            return publisher.get(0);
         }//end of else statement that will return a publisher if the list is not empty
      }//end of the while loop that traps the user here until they enter a valid publisher
      return null;//here because IntelliJ will throw a fit because the other return is inside a conditional function
   } // end of pickPublisher

   /**
    * Adds a Publisher entity to the database utilizing JPA Annotations from associated classes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void addPublisher(){
      boolean nameCheck = false;
      //traps user until they add a publisher
      while(!nameCheck) {
         System.out.println("What is the name of the new publisher?");
         String userName = input.nextLine();//prompt for the name of the publisher
         try{
            List<Publishers> publisher = this.entityManager.createNamedQuery("ReturnPublisher",
                    Publishers.class).setParameter(1, userName).getResultList();//returns all publishers with that name
            if (publisher.size() == 0) {//if there is no publisher with that name, the user can freely enter in the other data
               nameCheck= true;
               System.out.println("What is the email of the publisher?");
               String userEmail = input.nextLine();//email
               System.out.println("What is the phone number?");
               String userPhone = input.nextLine();// prompt for phone number
               ArrayList<Publishers> userPublisher = new ArrayList<Publishers>();//used for createEntity
               userPublisher.add(new Publishers(userName,userEmail,userPhone));//make a valid entry of the new publisher
               this.createEntity(userPublisher);//adds this valid and new publisher into the database
            } // end of if statement that checks if there are no publishers under that name
            else{//if there are publishers with that name, tell the user that
               System.out.println("Sorry, there already is a publisher under that name.");
            }//end of else statement if the user enters a potential duplicate name
         }//end of try block that attempts to add a new publisher
         catch(Exception e){//if there is a violation of constraints, go here
            System.out.println("Sorry, someone by that name already exists, or you entered a phone number or email that already exists.");
         }//end of catch block that catches errors related to unique constraints
      }//end of while loop that checks for valid input
   } // end of addPublisher


   /**
    * Create and persist a list of objects to the database.
    * @param entities   The list of entities to persist.  These can be any object that has been
    *                   properly annotated in JPA and marked as "persistable."  I specifically
    *                   used a Java generic so that I did not have to write this over and over.
    */
   public <E> void createEntity(List <E> entities) {
      for (E next : entities) {
         LOGGER.info("Persisting: " + next);
         // Use the CarClub entityManager instance variable to get our EntityManager.
         this.entityManager.persist(next);
      }

      // The auto generated ID (if present) is not passed in to the constructor since JPA will
      // generate a value.  So the previous for loop will not show a value for the ID.  But
      // now that the Entity has been persisted, JPA has generated the ID and filled that in.
      for (E next : entities) {
         LOGGER.info("Persisted object after flush (non-null id): " + next);
      }
   } // End of createEntity member method

} // End of Library class
