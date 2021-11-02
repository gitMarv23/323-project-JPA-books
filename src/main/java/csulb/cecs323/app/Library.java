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
                     //make sure user
                     boolean userCheck = false;
                     while(!userCheck) {
                        List<WritingGroups> writingGroups = library.entityManager.createNamedQuery("ReturnAllWriting",
                                WritingGroups.class).getResultList();
                        for (WritingGroups writing : writingGroups) {
                           System.out.println("Writing Email: " + writing.getEmail());
                        }
                        System.out.println("Please enter in one of the writing emails displayed");
                        String userEmail = input.nextLine();
                        List<WritingGroups> author = library.entityManager.createNamedQuery("ReturnWriting",WritingGroups.class).setParameter(1,userEmail).getResultList();
                        if(author.size()==0){
                           System.out.println("Sorry, you entered an invalid writing name name.");
                        }
                        else{
                           userCheck = true;
                           System.out.println(author.get(0).toString());
                        }
                     }
                     break;
               }
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
               }
               break;

            default:userLeaves = true;
               break;
         }
      }

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
      boolean userLeaves = false;
      while(!userLeaves) {
         System.out.println("Please select one of the options. Be sure to ONLY input '1' if you want the first option;" +
                 "otherwise, you will leave this menu\n" +
                 "1. Add A New Writing Group\n" +
                 "2. Add a New Individual Author\n" +
                 "3. Add a New Ad Hoc Team\n" +
                 "4. Add an Author to an Ad Hoc Team\n" +
                 "Anything Else: Exit Authoring Menu");
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
         }
      }
   } // end of authoringMenu

   /**
    * Adds a Writing Group entity to the database utilizing JPA Annotations from associated classes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void addWritingGroup(){
      boolean nameCheck = false;
      while(!nameCheck){
         System.out.println("What is the email of the head writer?");
         String userEmail = input.nextLine();
         try{
            List<Authoring_Entities> writingGroups = this.entityManager.createNamedQuery("ReturnAuthor",
                    Authoring_Entities.class).setParameter(1, userEmail).getResultList();
            if (writingGroups.isEmpty()) {
               nameCheck= true;
               System.out.println("What is the name of the head writer?");
               String userHeadName = input.nextLine();
               System.out.println("What is the name of the authoring group?");
               String userName = input.nextLine();
               System.out.println("What is the year that the group was formed?");
               try {
                  int userYear = input.nextInt();
                  input.nextLine();
                  ArrayList<WritingGroups> userWritingGroup = new ArrayList<WritingGroups>();
                  userWritingGroup.add(new WritingGroups(userEmail,userName,userHeadName,userYear));
                  this.createEntity(userWritingGroup);
               }
               catch (Exception e){
                  System.out.println("Sorry, you entered in a bad entry for year. please try again.");
               }
            } // end of if statement
            else{
               System.out.println("Sorry, there already is a publisher under that name.");
            }
         }
         catch(Exception e){
            System.out.println("Sorry, someone by that name already exists.");
         }
      }
   } // end of addWritingGroup

   /**
    * Adds an Individual Author entity to the database utilizing JPA Annotations from associated classes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void addIndividualAuthor(){
      boolean nameCheck = false;
      while(!nameCheck) {
         System.out.println("What is the email of the new author?");
         String userEmail = input.nextLine();
         try{
            List<Individual_Authors> individualAuthors = this.entityManager.createNamedQuery("ReturnAuthor",
                    Individual_Authors.class).setParameter(1, userEmail).getResultList();
            if (individualAuthors.size() == 0) {
               nameCheck= true;
               System.out.println("What is the name of the author?");
               String userName = input.nextLine();

               ArrayList<Individual_Authors> userAuthor = new ArrayList<Individual_Authors>();
               userAuthor.add(new Individual_Authors(userEmail,userName));
               this.createEntity(userAuthor);
            } // end of if statement
            else{
               System.out.println("Sorry, there already is a publisher under that name.");
            }
         }
         catch(Exception e){
            System.out.println("Sorry, someone by that name already exists.");
         }
      }
   } // end of addIndividualAuthor

   /**
    * Adds an Ad Hoc Team entity to the database utilizing JPA Annotations from associated classes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void addAdHocTeam(){
      boolean nameCheck = false;
      while(!nameCheck) {
         System.out.println("What is the email of the new adhocteam?");
         String userEmail = input.nextLine();
         try{
            List<Ad_Hoc_Teams_Members> adHocTeamMember = this.entityManager.createNamedQuery("ReturnAuthor",
                    Ad_Hoc_Teams_Members.class).setParameter(1, userEmail).getResultList();
            if (adHocTeamMember.size() == 0) {
               nameCheck= true;
               System.out.println("What is the name of the Ad Hoc Team?");
               String userName = input.nextLine();

               ArrayList<Ad_Hoc_Teams_Members> adHocTeam = new ArrayList<Ad_Hoc_Teams_Members>();
               adHocTeam.add(new Ad_Hoc_Teams_Members(userEmail,userName));
               this.createEntity(adHocTeam);
            } // end of if statement
            else{
               System.out.println("Sorry, there already is a publisher under that name.");
            }
         }
         catch(Exception e){
            System.out.println("Sorry, someone by that name already exists.");
         }
      }
   } // end of addHocTeam

   /**
    * Adds an individual author to an EXISTING Ad Hoc Team entity.
    * Subsequent to inclusion, the database utilizing JPA Annotations from associated classes is updated.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void addAuthorToAdHoc(){
      boolean nameCheck = false;
      while(!nameCheck) {
         List<Individual_Authors> individualAuthors = this.entityManager.createNamedQuery("ReturnAllIndividualAuthors",
                 Individual_Authors.class).getResultList();
         for(Individual_Authors individual_author : individualAuthors){
            System.out.println("Individual Authors: "+individual_author.toString());
         }
         System.out.println("Which email of the author needs to be added to an ad hoc team?");
         String userEmail = input.nextLine();
         try{
            List<Individual_Authors> adHocTeamMember = this.entityManager.createNamedQuery("ReturnIndividualAuthor",
                    Individual_Authors.class).setParameter(1, userEmail).getResultList();
            if (adHocTeamMember.size() != 0) {
               List<Ad_Hoc_Teams_Members> adHocTeamsMembers = this.entityManager.createNamedQuery("ReturnAllAdHocTeamMembers",
                       Ad_Hoc_Teams_Members.class).getResultList();
               for(Ad_Hoc_Teams_Members ad_hoc_teams_members : adHocTeamsMembers){
                  System.out.println("Ad Hoc Team Members: "+ad_hoc_teams_members.toString());
               }
               System.out.println("What is the email of the ad hoc team you want to add the author to?");
               String userAdHocEmail = input.nextLine();
               List<Ad_Hoc_Teams_Members> adHocTeam = this.entityManager.createNamedQuery("ReturnAdHocTeamMember",
                       Ad_Hoc_Teams_Members.class).setParameter(1, userAdHocEmail).getResultList();
               if(adHocTeam.size()!=0){
                  if(adHocTeam.get(0).getIndividualAuthors().contains(adHocTeamMember.get(0))) {
                     System.out.println("You already have an individual under that ad hoc team.");
                  }
                  else {
                     nameCheck= true;
                     adHocTeam.get(0).addIndividualAuthors(adHocTeamMember.get(0));
                     this.createEntity(adHocTeam);
                  }
               }
               else{
                  System.out.println("Sorry, can't find anything related to the email of the ad hoc team");
               }
            } // end of if statement
            else{
               System.out.println("Sorry, there already is a publisher under that name.");
            }
         }
         catch(Exception e){
            System.out.println("Sorry, someone by that name already exists.");
         }
      }
   } // end of addAuthorToAdHoc

   /**
    * Displays all Authoring Entities contained in the database
    * @return true if table contains existing Authoring Entities
    * @return false with console output message if no Authoring Entities are found
    * */
   public boolean displayAllAuthoring_EntitiesPK(){
      try {
         for (Authoring_Entities author : this.entityManager.createNamedQuery("ReturnAllAuthors", Authoring_Entities.class).getResultList()) {
            System.out.println("Author Email: "+author.getEmail() + "   Author Writing Type: " + author.getDiscriminatorValue());
         }
         return true;
      }
      catch(Exception e){
         System.out.println("Sorry, you have no existing authors in the database.");
         return false;
      }
   } // end of DisplayAllAuthoring_Entities

   /**
    * Allow user to select an existing Authoring Entity to utilize for other functionalities.
    * This function will be used in other methods such as addBook.
    * @return first index from List object containing desired Entity
    * @return null if an invalid publisher name has been selected
    */
   public Authoring_Entities pickAuthoringEntity(){
      boolean userChoice = false;
      while(!userChoice){
         this.displayAllAuthoring_EntitiesPK();
         System.out.println("Please enter the email of the author you want to pick.");
         String userAuthor = input.nextLine();
         List<Authoring_Entities> author = this.entityManager.createNamedQuery("ReturnAuthor",Authoring_Entities.class).setParameter(1,userAuthor).getResultList();
         if(author.size()==0){
            System.out.println("Sorry, you entered an invalid publisher name.");
         }
         else{
            return author.get(0);
         }
      }
      return null;
   } // end of pickAuthoringEntity

   /**
    * Displays all books in the current database
    * @return true and all information related to current books in database
    * @return false if no books found
    */
   public boolean displayAllBookPK(){
      try {
         for (Books book : this.entityManager.createNamedQuery("ReturnAllBooks", Books.class).getResultList()) {
            System.out.println("Book ISBN: " + book.getISBN());
         }
         return true;
      }
      catch(Exception e){
         System.out.println("Sorry, you have no existing books in the database.");
         return false;
      }
   } // end of displayAllBooks

   /**
    * Select a book from database based on user's input for ISBN
    * @return  null if ISBN is invalid
    * @return  book from list if match found
    */
   public Books pickBook(){
      boolean userChoice = false;
      while(!userChoice){
         this.displayAllBookPK();
         System.out.println("Please enter the ISBN of the book you would like");
         String userBook = input.nextLine();
         List<Books> book = this.entityManager.createNamedQuery("ReturnBook",Books.class).setParameter(1,userBook).getResultList();
         if(book.size()==0){
            System.out.println("Sorry, you entered an invalid ISBN.");
         }
         else{
            return book.get(0);
         }
      }
      return null;
   } // end of pickBook

   /**
    * Adds a Book entity to the database utilizing JPA Annotations from associated classes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void addBook(){
      boolean bookCheck = false;
      while(!bookCheck) {
         System.out.println("What is the ISBN of the new book?");
         String userISBN = input.nextLine();
         try{
            List<Books> book = this.entityManager.createNamedQuery("ReturnBook",
                    Books.class).setParameter(1, userISBN).getResultList();
            if (book.size() == 0) {
               bookCheck= true;
               System.out.println("What is the title of the publisher?");
               String userTitle = input.nextLine();
               System.out.println("What year was this book published?");
               int userYear = input.nextInt();
               input.nextLine();
               Publishers userPublisher = this.pickPublisher();
               Authoring_Entities userAuthor = this.pickAuthoringEntity();
               ArrayList<Books> userBook = new ArrayList<Books>();
               userBook.add(new Books(userISBN,userTitle,userYear, userPublisher,userAuthor));
               this.createEntity(userBook);
            } // end of if statement
            else{
               System.out.println("Sorry, there already is a publisher under that name.");
            }
         }
         catch(Exception e){
            System.out.println("Sorry, someone by that name already exists.");
         }
      }
   } // end of addBook

   /**
    * Deletes a Book entity to the database utilizing JPA Annotations from associated classes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void deleteBook(){
      List<Books> booksList = this.entityManager.createNamedQuery("ReturnAllBooks", Books.class).getResultList();
      if(booksList.size()>0) {
         boolean userChoice = false;
         while (!userChoice) {
            for (Books book : booksList) {
               System.out.println("Books: "+book.toString());
            }
            System.out.println("Please enter the ISBN of the book you want to delete.");
            String userName = input.nextLine();
            try {
               List<Books> book = this.entityManager.createNamedQuery("ReturnBook",
                       Books.class).setParameter(1, userName).getResultList();
               if (book.size() == 0) {
                  System.out.println("Sorry, that ISBN doesn't exist in our tables. Please input one of the ISBN's displayed.");
               }
               else {
                  System.out.println("ISBN has been successfully deleted.");
                  this.entityManager.remove(entityManager.find(Books.class, userName));
                  userChoice = true;
               }
            }
            catch (Exception e) {
               System.out.println("It seems like you entered an invalid ISBN. Please input the correct ISBN from the list we provided.");
            }
         }
      }
      else{
         System.out.println("Sorry, but we don't have any books stored in our database. Add new books to remove some.");
      }
   } // end of deleteBook

   /**
    * Updates current desired Book entity in the database based on user input.
    * JPA Annotations from associated classes are then used with persistence module to make database changes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void updateBook(){
      List<Books> booksList = this.entityManager.createNamedQuery("ReturnAllBooks", Books.class).getResultList();
      if(booksList.size()>0) {
         boolean userChoice = false;
         while (!userChoice) {
            for (Books book : booksList) {
               System.out.println(book.toString());
            }
            System.out.println("Please enter the ISBN of the book you want to update.");
            String userName = input.nextLine();
            try {
               List<Books> book = this.entityManager.createNamedQuery("ReturnBook",
                       Books.class).setParameter(1, userName).getResultList();
               if (book.size() == 0) {
                  System.out.println("Sorry, that ISBN doesn't exist in our tables. Please input one of the ISBN's displayed.");
               }
               else {
                  Books updateUserBook = this.entityManager.find(Books.class,userName);
                  Authoring_Entities chosenAuthor = pickAuthoringEntity();
                  if(chosenAuthor.getEmail().equals(updateUserBook.getAuthoringName().getEmail())){
                     System.out.println("Sorry, but that authoring entity is already the author for that book");
                  }
                  else {
                     updateUserBook.setAuthoringName(chosenAuthor);
                     System.out.println("The book has been successfully updated");
                     userChoice = true;
                  }
               }
            }
            catch (Exception e) {
               System.out.println("It seems like you entered an invalid ISBN. Please input the correct ISBN from the list we provided.");
            }
         }
      }
      else{
         System.out.println("Sorry, but we don't have any books stored in our database. Add new books to remove some.");
      }
   } // end of updateBook

   /**
    * Displays all publishers in the current database
    * @return true if Publishers exist and display current entities
    * @return false if no Publishers found
    */
   public boolean displayAllPublisherPK(){
      try {
         for (Publishers publisher : this.entityManager.createNamedQuery("ReturnAllPublisher", Publishers.class).getResultList()) {
            System.out.println("Publisher Name: " + publisher.getName());
         }
         return true;
      }
      catch(Exception e){
         System.out.println("Sorry, there are no existing publishers inside the database.");
         return false;
      }
   } // end of displayAllPublishers

   /**
    * Allow user to select a publisher from the existing Publishers table in our database
    * @return Publisher at first index from matched list
    * @return null with message if no Publisher match found
    */
   public Publishers pickPublisher(){
      boolean userChoice = false;
      while(!userChoice){
         this.displayAllPublisherPK();
         System.out.println("Please enter the name of the publisher you want to select.");
         String userPublisher = input.nextLine();
         List<Publishers> publisher = this.entityManager.createNamedQuery("ReturnPublisher",Publishers.class).setParameter(1,userPublisher).getResultList();
         if(publisher.size()==0){
            System.out.println("Sorry, you entered an invalid publisher name.");
         }
         else{
            return publisher.get(0);
         }
      }
      return null;
   } // end of pickPublisher

   /**
    * Adds a Publisher entity to the database utilizing JPA Annotations from associated classes.
    * Will Prompt user for necessary input prior to executing Native Class Queries and other necessary functionalities
    */
   public void addPublisher(){
      boolean nameCheck = false;
      while(!nameCheck) {
         System.out.println("What is the name of the new publisher?");
         String userName = input.nextLine();
         try{
            List<Publishers> publisher = this.entityManager.createNamedQuery("ReturnPublisher",
                    Publishers.class).setParameter(1, userName).getResultList();
            if (publisher.size() == 0) {
               nameCheck= true;
               System.out.println("What is the email of the publisher?");
               String userEmail = input.nextLine();
               System.out.println("What is the phone number?");
               String userPhone = input.nextLine();
               ArrayList<Publishers> userPublisher = new ArrayList<Publishers>();
               userPublisher.add(new Publishers(userName,userEmail,userPhone));
               this.createEntity(userPublisher);
            } // end of if statement
            else{
               System.out.println("Sorry, there already is a publisher under that name.");
            }
         }
         catch(Exception e){
            System.out.println("Sorry, someone by that name already exists.");
         }
      }
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
