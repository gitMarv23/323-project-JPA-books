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

// Import all the entity classes that we have written for this application.
import csulb.cecs323.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * A simple application to demonstrate how to persist an object in JPA.
 * <p>
 * This is for demonstration and educational purposes only.
 * </p>
 * <p>
 *     Originally provided by Dr. Alvaro Monge of CSULB, and subsequently modified by Dave Brown.
 * </p>
 */
public class Library {
   /**
    * You will likely need the entityManager in a great many functions throughout your application.
    * Rather than make this a global variable, we will make it an instance variable within the CarClub
    * class, and create an instance of CarClub in the main.
    */
   private EntityManager entityManager;

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
    * The constructor for the Library class.  All that it does is stash the provided EntityManager
    * for use later in the application.
    * @param manager    The EntityManager that we will use.
    */
   public Library(EntityManager manager) { this.entityManager = manager; }

   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("Library");
      EntityManager manager = factory.createEntityManager();

      // Create an instance of Library and store our new EntityManager as an instance variable.
      Library library = new Library(manager);

      EntityTransaction tx = manager.getTransaction();

      tx.begin();

      System.out.println("Welcome to JPA Books!");
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
         String userChoice = input.next();
         input.nextLine();
         switch (userChoice){
            case "1":
               System.out.println("Add New Object\n" +
                       "1. Authoring Entity Instance\n" +
                       "2. Add a new Publisher\n" +
                       "3. Add a new Book");
               userChoice = input.next();
               input.nextLine();
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
               }
               break;
            case "2":
               System.out.println("List all the Information about a Specific Object\n" +
                       "1. Publisher\n" +
                       "2. Book\n" +
                       "3. Writing Group\n");
               userChoice = input.next();
               input.nextLine();
               switch (userChoice){
                  case "1":
                     System.out.println("List Publisher Information");
                     break;
                  case "2":
                     System.out.println("List Book information");
                     break;
                  case "3":
                     System.out.println("List Writing Group Information");
                     break;
               }
               break;
            case "3":
               System.out.println("Delete a Book");
               break;
            case "4":
               System.out.println("Update a Book");
               break;
            case "5":
               System.out.println("List primary keys of all rows\n" +
                       "1. Publisher Keys\n" +
                       "2. Books Keys\n" +
                       "3. Authoring Entities Keys (Includes Authoring Entity Type)");
               userChoice = input.next();
               input.nextLine();
               switch (userChoice){
                  case "1":
                     System.out.println("List Publisher Keys");
                     for(Publishers publisherPrimary : library.entityManager.createNamedQuery("ReturnAllPublisher", Publishers.class).getResultList()){
                        System.out.println(publisherPrimary.getName());
                     }
                     break;
                  case "2":
                     System.out.println("List Books Keys");
                     break;
                  case "3":
                     System.out.println("List Authoring Entities Keys");
                     for(Authoring_Entities Authoring_Entities: library.entityManager.createNamedQuery("ReturnAllAuthors", Authoring_Entities.class).getResultList()){
                        System.out.println(Authoring_Entities.toString());
                     }
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
         String userChoice = input.next();
         input.nextLine();
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
   }

   public void addWritingGroup(){
      boolean nameCheck = false;
      while(!nameCheck){
         System.out.println("What is the name of the head writer?");
         String userHeadName = input.next();
         input.nextLine();
         try{
            List<Authoring_Entities> writingGroups = this.entityManager.createNamedQuery("ReturnWriting",
                    Authoring_Entities.class).setParameter(1, userHeadName).getResultList();
            if (writingGroups.isEmpty()) {
               nameCheck= true;
               System.out.println("What is the email of " + userHeadName + "?");
               String userEmail = input.next();
               input.nextLine();
               System.out.println("What is the name of the authoring group?");
               String userName = input.next();
               input.nextLine();
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
   }

   public void addIndividualAuthor(){
      boolean nameCheck = false;
      while(!nameCheck) {
         System.out.println("What is the name of the new author?");
         String userName = input.next();
         input.nextLine();
         try{
            List<IndividualAuthors> individualAuthors = this.entityManager.createNamedQuery("ReturnAuthor",
                    IndividualAuthors.class).setParameter(1, userName).getResultList();
            if (individualAuthors.size() == 0) {
               nameCheck= true;
               System.out.println("What is the email of " + userName + "?");
               String userEmail = input.next();
               input.nextLine();
               ArrayList<IndividualAuthors> userAuthor = new ArrayList<IndividualAuthors>();
               userAuthor.add(new IndividualAuthors(userEmail,userName));
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
   }
   // needs to be completed
   public void addAdHocTeam(){
      boolean nameCheck = false;
      while(!nameCheck) {
         System.out.println("What is the name of the new author?");
         String userName = input.next();
         input.nextLine();
         try{
            List<IndividualAuthors> individualAuthors = this.entityManager.createNamedQuery("ReturnPublisher",
                    IndividualAuthors.class).setParameter(1, userName).getResultList();
            if (individualAuthors.size() == 0) {
               nameCheck= true;
               System.out.println("What is the email of " + userName + "?");
               String userEmail = input.next();
               input.nextLine();
               ArrayList<IndividualAuthors> userAuthor = new ArrayList<IndividualAuthors>();
               userAuthor.add(new IndividualAuthors(userName,userEmail));
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
   }

   public void addAuthorToAdHoc(){

   }

   public boolean displayAllAuthoring_Entities(){
      try {
         for (Authoring_Entities author : this.entityManager.createNamedQuery("ReturnAllAuthors", Authoring_Entities.class).getResultList()) {
            System.out.println(author.toString());
         }
         return true;
      }
      catch(Exception e){
         System.out.println("Sorry, you have no existing authors in the database.");
         return false;
      }
   }

   public Authoring_Entities pickAuthoringEntity(){
      boolean userChoice = false;
      while(!userChoice){
         this.displayAllAuthoring_Entities();
         System.out.println("Please enter the email of the author you want to pick.");
         String userAuthor = input.next();
         input.nextLine();
         List<Authoring_Entities> author = this.entityManager.createNamedQuery("ReturnAuthor",Authoring_Entities.class).setParameter(1,userAuthor).getResultList();
         if(author.size()==0){
            System.out.println("Sorry, you entered an invalid publisher name.");
         }
         else{
            return author.get(0);
         }
      }
      return null;
   }

   public boolean displayAllBooks(){
      try {
         for (Books book : this.entityManager.createNamedQuery("ReturnAllBooks", Books.class).getResultList()) {
            System.out.println(book.toString());
         }
         return true;
      }
      catch(Exception e){
         System.out.println("Sorry, you have no existing books in the database.");
         return false;
      }
   }

   public void addBook(){
      boolean bookCheck = false;
      while(!bookCheck) {
         System.out.println("What is the ISBN of the new book?");
         String userISBN = input.next();
         input.nextLine();
         try{
            List<Books> book = this.entityManager.createNamedQuery("ReturnBook",
                    Books.class).setParameter(1, userISBN).getResultList();
            if (book.size() == 0) {
               bookCheck= true;
               System.out.println("What is the title of the publisher?");
               String userTitle = input.next();
               input.nextLine();
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
   }

   public void deleteBook(){
      List<Books> booksList = this.entityManager.createNamedQuery("ReturnAllBooks", Books.class).getResultList();
      if(booksList.size()>0) {
         boolean userChoice = false;
         while (!userChoice) {
            for (Books book : booksList) {
               System.out.println(book.toString());
            }
            System.out.println("Please enter the ISBN of the book you want to delete.");
            String userName = input.next();
            input.nextLine();
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
   }

   public void updateBook(){

   }

   public boolean displayAllPublishers(){
      try {
         for (Publishers publisher : this.entityManager.createNamedQuery("ReturnAllPublisher", Publishers.class).getResultList()) {
            System.out.println(publisher.toString());
         }
         return true;
      }
      catch(Exception e){
         System.out.println("Sorry, there are no existing publishers inside the database.");
         return false;
      }
   }

   public Publishers pickPublisher(){
      boolean userChoice = false;
      while(!userChoice){
         this.displayAllPublishers();
         System.out.println("Please enter the name of the publisher you want to select.");
         String userPublisher = input.next();
         input.nextLine();
         List<Publishers> publisher = this.entityManager.createNamedQuery("ReturnPublisher",Publishers.class).setParameter(1,userPublisher).getResultList();
         if(publisher.size()==0){
            System.out.println("Sorry, you entered an invalid publisher name.");
         }
         else{
            return publisher.get(0);
         }
      }
      return null;
   }

   public void addPublisher(){
      boolean nameCheck = false;
      while(!nameCheck) {
         System.out.println("What is the name of the new publisher?");
         String userName = input.next();
         input.nextLine();
         try{
            List<Publishers> publisher = this.entityManager.createNamedQuery("ReturnPublisher",
                    Publishers.class).setParameter(1, userName).getResultList();
            if (publisher.size() == 0) {
               nameCheck= true;
               System.out.println("What is the email of the publisher?");
               String userEmail = input.next();
               input.nextLine();
               System.out.println("What is the phone number?");
               String userPhone = input.next();
               input.nextLine();
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
   }


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
