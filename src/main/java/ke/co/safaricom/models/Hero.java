package ke.co.safaricom.models;

import java.util.ArrayList;
import java.util.Objects;

public class Hero {
  private int id;
  private String name;
  private int age;
  private String origin_story;
  private String powers;
  private String weaknesses;
  private int squad_id;
  private static ArrayList<Hero> instances = new ArrayList<>();


  public Hero(String name, int age, String origin_story, String powers, String weaknesses, int squad_id) {
//    this.id = instances.size();
    this.name = name;
    this.age = age;
    this.origin_story = origin_story;
    this.powers = powers;
    this.weaknesses = weaknesses;
    this.squad_id = squad_id;
    instances.add(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Hero hero = (Hero) o;
    return id == hero.id &&
      age == hero.age &&
//      squad_id == hero.squad_id &&
      Objects.equals(name, hero.name) &&
      Objects.equals(origin_story, hero.origin_story) &&
      Objects.equals(powers, hero.powers) &&
      Objects.equals(weaknesses, hero.weaknesses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, age, origin_story, powers, weaknesses);
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public void setOrigin_story(String origin_story) {
    this.origin_story = origin_story;
  }

  public void setPowers(String powers) {
    this.powers = powers;
  }

  public void setWeaknesses(String weaknesses) {
    this.weaknesses = weaknesses;
  }

  public void setId(int id) { this.id = id ; }

  public void setSquad_id(int squad_id) { this.squad_id = squad_id; }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public String getOrigin_story() {
    return origin_story;
  }

  public String getPowers() {
    return powers;
  }

  public String getWeaknesses() {
    return weaknesses;
  }

  public int getSquad_id() {
    return squad_id;
  }

}
