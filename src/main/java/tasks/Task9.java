package tasks;

import common.Person;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Objects;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    //т.к. из List первый элемент удалится за ~O(n), экономичнее применить skip
    if (persons.isEmpty()) {
      return Collections.emptyList();
    }
    return persons.stream()
        .skip(1)
        .map(Person::firstName)
        .collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  // Distinkt явно лишним был
  public Set<String> getDifferentNames(List<Person> persons) {
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  // Логичнее выглядит без дублирования фамилии, ещё отредактировал сам метод конкатенации
  public String convertPersonToString(Person person) {
    return Stream.of(person.secondName(), person.firstName())
        .filter(Objects::nonNull)
        .collect(Collectors.joining(" "));
  }


  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    Map<Integer, String> map = new HashMap<>(persons.size());
    for (Person person : persons) {
      if (!map.containsKey(person.id())) { // Проверяем на наличие такого ключа
        map.put(person.id(), convertPersonToString(person)); // Добавляем, если ключ отсутствует
      }
    }
    return map;
  }

  // есть ли совпадающие в двух коллекциях персоны?
  // Не самый идуальный вариант по ассимптотике, зато красиво
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    Set<Person> set1 = new HashSet<>(persons1);
    Set<Person> set2 = new HashSet<>(persons2);
    set1.retainAll(set2);
    return !set1.isEmpty();
  }

  // Посчитать число четных чисел
  // Сделаем через встроенный count
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(el -> el % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    // Числа от 1 до 10000 подряд включительно
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    // Хорошо обусловленная хеш функция должна обеспечивать равномерное распределение по бакетам,
    // в данном случае входные данные сами по себе уменьшают вероятность коллизии, являсь диапазоном подряд идущих чисел.
    // Предположу, что здесь отрабатывает хеш-функция, возвращающая само число в качестве хеша,
    // поэтому строковые представления snapshot и set и совпадают
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
