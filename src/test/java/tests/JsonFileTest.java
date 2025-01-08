package tests;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Fruit;
import models.QuestionModel;
import models.QuizModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class JsonFileTest {

    private static final ClassLoader cl = JsonFileTest.class.getClassLoader();
    ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("Валидация .json файла")
    @Test
    void jsonFileValidateTest() throws Exception {
       try (InputStream is = cl.getResourceAsStream("test.json")){
           assert is != null;
           try (InputStreamReader isr = new InputStreamReader(is)) {
               Fruit fruit = objectMapper.readValue(isr,Fruit.class);
               Assertions.assertEquals("Apple",fruit.getFruit());
               Assertions.assertEquals("Large",fruit.getSize());
               Assertions.assertEquals("Red",fruit.getColor());
           }
       }
    }

    @DisplayName("Валидация .json файла c array")
    @Test
    void jsonWithArrayFileValidateTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("test2.json")) {
            assert is != null;
            try (InputStreamReader isr = new InputStreamReader(is)) {
                QuizModel quiz = objectMapper.readValue(isr, QuizModel.class);
                Assertions.assertEquals("5 + 7 = ?", quiz.getMaths().getQ1().getQuestion());
                List<String> x = new ArrayList<>(Arrays.asList("10","11","12","13"));
                Assertions.assertEquals(x,quiz.getMaths().getQ1().getOption());
                Assertions.assertEquals("12", quiz.getMaths().getQ1().getAnswer());
            }
        }
    }
}
