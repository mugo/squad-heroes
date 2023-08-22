package ke.co.safaricom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ke.co.safaricom.dao.HeroesDaoSqlO;
import ke.co.safaricom.dao.SquadDaoSqlO;
import ke.co.safaricom.models.Hero;
import ke.co.safaricom.models.Squad;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import static spark.Spark.*;

public class App {
  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
  }
    public static void main(String[] args) {
      port(getHerokuAssignedPort());
    staticFileLocation("/public");

    String connectionString ="jdbc:postgresql://localhost:5432/squad_heroes";
    Sql2o sql2o = new Sql2o(connectionString, "postgresql", "pgadmin");
    HeroesDaoSqlO heroDao = new HeroesDaoSqlO(sql2o);
    SquadDaoSqlO squadDao = new SquadDaoSqlO(sql2o);
    Map<String,Object> model = new HashMap<>();

    // get: show all heroes in all squads and show all squads
    get("/", (req, res) -> {
      List<Squad> allSquads = squadDao.getAll();
      model.put("squads", allSquads);
      List<Hero> heroes = heroDao.getAll();
      model.put("heroes", heroes);
      return new ModelAndView(model, "index.hbs");
    }, new HandlebarsTemplateEngine());


    // get: add new squads
    get("/squads/new",(req,res)->{
      List<Squad> squads = squadDao.getAll();
      model.put("squads", squads);
      return new ModelAndView ( model, "squad-form.hbs");
    },new HandlebarsTemplateEngine());

    //    post: process a form to create a new squad
    post("/squads", (req, res) -> { //new
      String name = req.queryParams("name");
      int size = Integer.parseInt(req.queryParams("size"));
      String cause = req.queryParams("cause");
      Squad newSquad = new Squad(name, size, cause);
      squadDao.add(newSquad);
      res.redirect("/");
      return null;
    }, new HandlebarsTemplateEngine());

    //get: delete all squads and all heroes
    get("/squads/delete", (req, res) -> {
      squadDao.clearAllSquads();
      heroDao.clearAllHeroes();
      res.redirect("/");
      return null;
    }, new HandlebarsTemplateEngine());

    //get: delete all heroes
    get("/heroes/delete", (req, res) -> {
      heroDao.clearAllHeroes();
      res.redirect("/");
      return null;
    }, new HandlebarsTemplateEngine());

    //get a specific squad (and the heroes it contains)
    get("/squads/:id", (req, res) -> {
      int idOfSquadToFind = Integer.parseInt(req.params("id")); //new
      Squad foundSquad = squadDao.findById(idOfSquadToFind);
      model.put("squad", foundSquad);
      List<Hero> allHeroesBySquad = squadDao.getAllHeroesBySquad(idOfSquadToFind);
      model.put("heroes", allHeroesBySquad);
      model.put("squads", squadDao.getAll()); //refresh list of links for navbar
      return new ModelAndView(model, "squad-detail.hbs"); //new
    }, new HandlebarsTemplateEngine());

    //get: show a form to update a squad
    get("/squads/:id/edit", (req, res) -> {
      model.put("editSquad", true);
      Squad squad = squadDao.findById(Integer.parseInt(req.params("id")));
      model.put("squad", squad);
      model.put("squads", squadDao.getAll()); //refresh list of links for navbar
      return new ModelAndView(model, "squad-form.hbs");
    }, new HandlebarsTemplateEngine());

    //post: process a form to update a squad
    post("/squads/:id", (req, res) -> {
      int idOfSquadToEdit = Integer.parseInt(req.params("id"));
      String newName = req.queryParams("newName");
      int newSize = Integer.parseInt(req.queryParams("newSize"));
      String newCause = req.queryParams("newCause");
      squadDao.update(idOfSquadToEdit, newName, newSize, newCause);
      res.redirect("/");
      return null;
    }, new HandlebarsTemplateEngine());

    //get: delete an individual hero
    get("/squads/:squad_id/heroes/:hero_id/delete", (req, res) -> {
      int idOfHeroToDelete = Integer.parseInt(req.params("hero_id"));
      heroDao.deleteById(idOfHeroToDelete);
      res.redirect("/");
      return null;
    }, new HandlebarsTemplateEngine());

    //    get: add new heroes
    get("/heroes/new",(req,res)->{
      List<Squad> squads = squadDao.getAll();
      model.put("squads", squads);
      return new ModelAndView ( model, "hero-form.hbs");
    },new HandlebarsTemplateEngine());

    //    post: process new hero form
    post("/heroes", (req, res) -> { //new
      String name = req.queryParams("name");
      int age = Integer.parseInt(req.queryParams("age"));
      String origin_story = req.queryParams("origin_story");
      String powers = req.queryParams("powers");
      String weaknesses = req.queryParams("weaknesses");
      int squad_id = Integer.parseInt(req.queryParams("squad_id"));
      Hero newHero = new Hero(name,age, origin_story, powers,weaknesses,squad_id);
      heroDao.add(newHero);
      List<Squad> allSquads = squadDao.getAll();
      model.put("squads", allSquads);
      res.redirect("/");
      return null;
    }, new HandlebarsTemplateEngine());

    //get: show an individual hero that is nested in a squad
    get("/squads/:squad_id/heroes/:hero_id", (req, res) -> {
      int idOfHeroToFind = Integer.parseInt(req.params("hero_id")); //pull id - must match route segment
      Hero foundHero = heroDao.findById(idOfHeroToFind); //use it to find hero
      int idOfSquadToFind = Integer.parseInt(req.params("squad_id"));
      Squad foundSquad = squadDao.findById(idOfSquadToFind);
      model.put("squad", foundSquad);
      model.put("hero", foundHero); //add it to model for template to display
      model.put("squads", squadDao.getAll()); //refresh list of links for navbar
      return new ModelAndView(model, "hero-detail.hbs"); //individual hero page.
    }, new HandlebarsTemplateEngine());

    //get: show a form to update a hero
    get("/heroes/:id/edit", (req, res) -> {
      List<Squad> allSquads = squadDao.getAll();
      model.put("squads", allSquads);
      Hero hero = heroDao.findById(Integer.parseInt(req.params("id")));
      model.put("hero", hero);
      model.put("editHero", true);
      return new ModelAndView(model, "hero-form.hbs");
    }, new HandlebarsTemplateEngine());

    //hero: process a form to update a hero
    post("/heroes/:id", (req, res) -> { //URL to update hero on POST route
      int heroToEditId = Integer.parseInt(req.params("id"));
      String newName = req.queryParams("name");
      int newAge = Integer.parseInt(req.queryParams("age"));
      String newOrigin_story = req.queryParams("origin_story");
      String newPowers = req.queryParams("powers");
      String newWeaknesses = req.queryParams("weaknesses");
      int newSquad_id = Integer.parseInt(req.queryParams("squad_id"));
      heroDao.update(heroToEditId, newName, newAge, newOrigin_story, newPowers, newWeaknesses, newSquad_id);  // remember the hardcoded squadId we placed? See what we've done to/with it?
      res.redirect("/");
      return null;
    }, new HandlebarsTemplateEngine());

  }
}
////    get: show all heroes
//    get("/",(req,res)->{
//      List<Hero> heroes = heroDao.getAll();
//      model.put("heroes", heroes);
//      return new ModelAndView(model, "index.hbs");
//    }, new HandlebarsTemplateEngine());
