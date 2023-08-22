package ke.co.safaricom.dao;

import ke.co.safaricom.models.Hero;
import java.util.List;

public interface HeroesDao {
  // LIST
  List<Hero> getAll();

  //CREATE
  void add(Hero hero);

  //READ
  Hero findById(int id);

  //UPDATE
  void update(int id,String name, int age, String origin_story, String powers, String weaknesses, int squad_id);

  //DELETE
  void deleteById(int id);
  void clearAllHeroes();
}
