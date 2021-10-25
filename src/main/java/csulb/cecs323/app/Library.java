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
    * The constructor for the CarClub class.  All that it does is stash the provided EntityManager
    * for use later in the application.
    * @param manager    The EntityManager that we will use.
    */
   public Library(EntityManager manager) {
      this.entityManager = manager;
   }

   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("Library");
      EntityManager manager = factory.createEntityManager();
      // Create an instance of CarClub and store our new EntityManager as an instance variable.
      Library library = new Library(manager);


      EntityTransaction tx = manager.getTransaction();

      tx.begin();
      List<Publishers> publishers = new ArrayList<Publishers>();
      publishers.add(new Publishers("Bob","Bob Inc.","44532"));

      library.deletePublishers();
      library.addPublisher();
      library.createEntity(publishers);
      tx.commit();

      System.out.println("Completed Satisfactorily");

      LOGGER.fine("End of Transaction");


      // Any changes to the database need to be done within a transaction.
      // See: https://en.wikibooks.org/wiki/Java_Persistence/Transactions

      /*LOGGER.fine("Begin of Transaction");
      EntityTransaction tx = manager.getTransaction();

      tx.begin();
      // List of owners that I want to persist.  I could just as easily done this with the seed-data.sql
      List <Owners> owners = new ArrayList<Owners>();
      // Load up my List with the Entities that I want to persist.  Note, this does not put them
      // into the database.
      owners.add(new Owners("Reese", "Mike", "714-892-5544"));
      owners.add(new Owners("Leck", "Carl", "714-321-3729"));
      owners.add(new Owners("Guitierez", "Luis", "562-982-2899"));
      // Create the list of owners in the database.
      library.createEntity (owners);

      //give each owner at least 2 cars
      List <Cars> cars = new ArrayList<Cars>();

      cars.add((new Cars("TRUWT28N651011265", "Ford", "F-150", 2007, owners.get(0), new auto_body_styles())));
      cars.add((new Cars("1HD1BX510BB027648", "Chevy", "Suburban", 2001, owners.get(0), new auto_body_styles())));
      cars.add((new Cars("1GNDV23L26D236839", "Toyota", "Tacoma", 2016, owners.get(1), new auto_body_styles())));
      cars.add((new Cars("2HGES26772H566107", "Acura", "Integra", 2011, owners.get(1), new auto_body_styles())));
      cars.add((new Cars("1FTSX21P05EC23578", "Lincoln", "Navigator", 2015, owners.get(2), new auto_body_styles())));
      cars.add((new Cars("4T4BF1FK4CR236137", "Lexus", "RX", 2020, owners.get(2), new auto_body_styles())));

      // Create list of owners in the database
      library.createEntity(cars);

      // Commit the changes so that the new data persists and is visible to other users.
      tx.commit();
      LOGGER.fine("End of Transaction");*/

   } // End of the main method

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

   public void deletePublishers(){
      List<Publishers> publishers = this.entityManager.createNamedQuery("ReturnAllPublisher", Publishers.class).getResultList();
      boolean userChoice = false;
      while(!userChoice){
         for(Publishers publisher : publishers){
            System.out.println(publisher.toString());
         }
         System.out.println("Please enter the name of the publisher you want to delete.");
         String userName = input.next();
         input.nextLine();
         try{
            List<Publishers> publisher = this.entityManager.createNamedQuery("ReturnPublisher",
                    Publishers.class).setParameter(1, userName).getResultList();
            if (publisher.size() == 0) {
            }
            else{
               System.out.println("Publisher has been successfully deleted.");
               this.entityManager.remove(entityManager.find(Publishers.class, userName));
               userChoice = true;
            }
         }
         catch(Exception e){
            System.out.println("Why you gotta break on me?");
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

   /**
    * Think of this as a simple map from a String to an instance of auto_body_styles that has the
    * same name, as the string that you pass in.  To create a new Cars instance, you need to pass
    * in an instance of auto_body_styles to satisfy the foreign key constraint, not just a string
    * representing the name of the style.
    * @param name       The name of the autobody style that you are looking for.
    * @return           The auto_body_styles instance corresponding to that style name.
    */
   public auto_body_styles getStyle (String name) {
      // Run the native query that we defined in the auto_body_styles entity to find the right style.
      List<auto_body_styles> styles = this.entityManager.createNamedQuery("ReturnAutoBodyStyle",
              auto_body_styles.class).setParameter(1, name).getResultList();
      if (styles.size() == 0) {
         // Invalid style name passed in.
         return null;
      } else {
         // Return the style object that they asked for.
         return styles.get(0);
      }
   }// End of the getStyle method
} // End of CarClub class
