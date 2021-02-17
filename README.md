# Тесты к курсу «Парадигмы программирования»

[Условия домашних заданий](http://www.kgeorgiy.info/courses/paradigms/homeworks.html)


## Домашнее задание 2. Очередь на массиве

Модификации
 * *Базовая*
    * Классы должны находиться в пакете `queue`
    * [Исходный код тестов](java/queue/ArrayQueueTest.java)
    * [Откомпилированные тесты](artifacts/queue/ArrayQueueTest.jar)


## Домашнее задание 1. Бинарный поиск

Модификации
 * *Базовая*
    * Класс `BinarySearch` должен находиться в пакете `search`
    * [Исходный код тестов](java/search/BinarySearchTest.java)
    * [Откомпилированные тесты](artifacts/search/BinarySearchTest.jar)
 * *Missing* (32-33)
    * Если в массиве `a` отсутствует элемент, равный `x`, то требуется
      вывести индекс вставки в формате, определенном в
      [`Arrays.binarySearch`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Arrays.html#binarySearch(int%5B%5D,int)).
    * Класс должен иметь имя `BinarySearchMissing`
    * [Исходный код тестов](java/search/BinarySearchMissingTest.java)
    * [Откомпилированные тесты](artifacts/search/BinarySearchMissingTest.jar)


Для того, чтобы протестировать базовую модификацию домашнего задания:

 1. Скачайте тесты ([BinarySearchTest.jar](artifacts/search/BinarySearchTest.jar))
 1. Откомпилируйте `BinarySearch.java`
 1. Проверьте, что создался `BinarySearch.class`
 1. В каталоге, в котором находится `search/BinarySearch.class` выполните команду

    ```
       java -jar <путь к BinarySearchTest.jar>
    ```

    Например, если `BinarySearchTest.jar` находится в текущем каталоге, 
    а `BinarySearch.class` в каталоге `search`, выполните команду

    ```
        java -jar BinarySearchTest.jar
    ```
