package otus.processor.homework;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.model.Message;
import ru.otus.processor.homework.EvenSecondException;
import ru.otus.processor.homework.EvenSecondExceptionProcessor;
import ru.otus.processor.homework.TimeProvider;

@DisplayName("Класс EvenSecondThrowExceptionTest должен")
public class EvenSecondThrowExceptionTest {

  private TimeProvider timeMock;
  private Message messageMock;

  @BeforeEach
  void setUp() {
    timeMock = Mockito.mock(TimeProvider.class);
    messageMock = Mockito.mock(Message.class);
  }

  @Test
  @DisplayName("выбрасывать исключение в четную секунду")
  void throwExceptionOnEvenSecondTest() {
    var evenSecond = 2;
    var processor = new EvenSecondExceptionProcessor(timeMock);

    Mockito.when(timeMock.getSeconds()).thenReturn(evenSecond);

    Assertions.assertThatExceptionOfType(EvenSecondException.class)
        .isThrownBy(() -> processor.process(messageMock))
        .withMessage("Throw in even second exception");
  }

  @Test
  @DisplayName("не выбрасывать исключение в нечетную секунду")
  void doesNotThrowExceptionOnOddSecondTest() {
    var oddSecond = 1;
    var processor = new EvenSecondExceptionProcessor(timeMock);

    Mockito.when(timeMock.getSeconds()).thenReturn(oddSecond);

    Assertions.assertThatNoException().isThrownBy(() -> processor.process(messageMock));
  }
}
