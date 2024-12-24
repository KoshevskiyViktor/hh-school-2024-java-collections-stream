package tasks;

import common.Area;
import common.Person;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
Имеются
- коллекция персон Collection<Person>
- словарь Map<Integer, Set<Integer>>, сопоставляющий каждой персоне множество id регионов
- коллекция всех регионов Collection<Area>
На выходе хочется получить множество строк вида "Имя - регион". Если у персон регионов несколько, таких строк так же будет несколько
 */
public class Task6 {

  public static String concatenateNameAndArea(Person person, Area area) {
    return person.firstName() + " - " + area.getName();
  }

  public static Set<String> getPersonDescriptions(Collection<Person> persons,
                                                  Map<Integer, Set<Integer>> personAreaIds,
                                                  Collection<Area> areas) {
    Map<Integer, Area> areaNameById = areas.stream()
        .collect(Collectors.toMap(Area::getId, area -> area));

    return persons.stream()
        .flatMap(person -> {
          Set<Integer> areaIds = personAreaIds.getOrDefault(person.id(), Set.of());
          return areaIds.stream()
              .map(areaNameById::get)
              .map(area -> concatenateNameAndArea(person, area));
        })
        .collect(Collectors.toSet());
  }
}
