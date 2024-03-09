package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.homework.EvenSecondExceptionProcessor;
import ru.otus.processor.homework.SwitchFieldsProcessor;
import ru.otus.processor.homework.TimeProviderImpl;

import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
             Секунда должна определяьться во время выполнения.
             Тест - важная часть задания
             Обязательно посмотрите пример к паттерну Мементо!
       4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
          Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
          Для него уже есть тест, убедитесь, что тест проходит
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */

      var processors = List.of(new SwitchFieldsProcessor(),
          new EvenSecondExceptionProcessor(new TimeProviderImpl()));

      var complexProcessor = new ComplexProcessor(processors, e -> {});
      var historyListener = new HistoryListener();
      complexProcessor.addListener(historyListener);

      var field13Obj = new ObjectForMessage();
      field13Obj.setData(List.of("field13"));

      var message = new Message.Builder(1L)
          .field1("field1")
          .field2("field2")
          .field3("field3")
          .field11("field11")
          .field12("field12")
          .field13(field13Obj)
          .build();

      var result = complexProcessor.handle(message);
      System.out.println("Result: " + result);

      complexProcessor.removeListener(historyListener);
    }
}
