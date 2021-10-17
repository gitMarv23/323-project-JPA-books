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

// Import all of the entity classes that we have written for this application.
import csulb.cecs323.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
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
public class CarClub {
   /**
    * You will likely need the entityManager in a great many functions throughout your application.
    * Rather than make this a global variable, we will make it an instance variable within the CarClub
    * class, and create an instance of CarClub in the main.
    */
   private EntityManager entityManager;

   /**
    * The Logger can easily be configured to log to a file, rather than, or in addition to, the console.
    * We use it because it is easy to control how much or how little logging gets done without having to
    * go through the application and comment out/uncomment code and run the risk of introducing a bug.
    * Here also, we want to make sure that the one Logger instance is readily available throughout the
    * application, without resorting to creating a global variable.
    */
   private static final Logger LOGGER = Logger.getLogger(CarClub.class.getName());

   /**
    * The constructor for the CarClub class.  All that it does is stash the provided EntityManager
    * for use later in the application.
    * @param manager    The EntityManager that we will use.
    */
   public CarClub(EntityManager manager) {
      this.entityManager = manager;
   }

   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("CarClub");
      EntityManager manager = factory.createEntityManager();
      // Create an instance of CarClub and store our new EntityManager as an instance variable.
      CarClub carclub = new CarClub(manager);


      // Any changes to the database need to be done within a transaction.
      // See: https://en.wikibooks.org/wiki/Java_Persistence/Transactions

      LOGGER.fine("Begin of Transaction");
      EntityTransaction tx = manager.getTransaction();

      tx.begin();
      // List of owners that I want to persist.  I could just as easily done this with the seed-data.sql
      List <Owners> owners = new ArrayList<Owners>();
      // Load up my List with the Entities that I want to persist.  Note, this does not put them
      // into the database.
      owners.add(new Owners("Reese", "Mike", "714-892-5544"));
      owners.add(new Owners("Leck", "Carl", "714-321-3729"));
      owners.add(new Owners("Guitierez", "Luis", "562-982-2899"));

      //added new owners as stated
      owners.add(new Owners("Lawrence","Lim","234-561-1257"));
      owners.add(new Owners("Jacob","Sanders","123-451-1678"));
      owners.add(new Owners("Tyler","Jung","854-229-8512"));
      // Create the list of owners in the database.
      carclub.createEntity (owners);

      List<Cars> carsList = new ArrayList<Cars>();
      //twelve different cars belonging to their respective owners
      carsList.add(new Cars("1FTRW14W84KC76110","BMW","Mercedes",2019,owners.get(0),carclub.getStyle("sports car")));
      carsList.add(new Cars("JH4KA3263KC011910","Acura","Legend",1989,owners.get(0),carclub.getStyle("sedan")));
      carsList.add(new Cars("1G8MG35X48Y106575","Saturn","Saturn Sky",2008,owners.get(1),carclub.getStyle("sedan")));
      carsList.add(new Cars("1G8ZF5287XZ363384","Saturn","S Series",1999,owners.get(1),carclub.getStyle("sports car")));
      carsList.add(new Cars("1C4BJWDG8DL559834","Jeep","Wrangler Unlimited",2013,owners.get(2),carclub.getStyle("sport-utility vehicle")));
      carsList.add(new Cars("JH4DB1650NS000627","Acura","Integra",1992,owners.get(2),carclub.getStyle("sedan")));

      carsList.add(new Cars("5J6RM4H75CL059384","Honda","CR V",2012,owners.get(3),carclub.getStyle("sedan")));
      carsList.add(new Cars("1G2JB12F047226515","Pontiac","Sunfire",2004,owners.get(3),carclub.getStyle("hatchback")));
      carsList.add(new Cars("YV1612FH6D2170397","Volvo","S60",2013,owners.get(4),carclub.getStyle("sedan")));
      carsList.add(new Cars("1MEBP88U0GG643233","Mercury","Sable",1986,owners.get(4),carclub.getStyle("minivan")));
      carsList.add(new Cars("JH4DB8580SS001230","Acura","Integra",1995,owners.get(5),carclub.getStyle("sports car")));
      carsList.add(new Cars("2C3HD46R4WH170262","Chrysler","Concorde",1998,owners.get(5),carclub.getStyle("station wagon")));

      // Commit the changes so that the new data persists and is visible to other users.
      carclub.createEntity(carsList);//cars are entered into the db.
      tx.commit();
      LOGGER.fine("End of Transaction");

   } // End of the main method

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
