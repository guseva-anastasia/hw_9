package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.*;
import static com.codeborne.pdftest.assertj.Assertions.assertThat;


public class ZipFileTest {

    private ClassLoader cl = ZipFileTest.class.getClassLoader();


    @DisplayName("Валидация .pdf файла внутри .zip архива")
    @Test
    void validatePdfInZipFileTest2() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(cl.getResourceAsStream("ArchiveWithPdf.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                    assertThat(entry.getName()).endsWith(".pdf");
                    PDF pdf = new PDF(zis);
                    assertThat(pdf).containsExactText("Тестовый pdf файл");
                }
            }
        }

    @DisplayName("Валидация .csv файла внутри .zip архива")
    @Test
    void validateCsvInZipFileTest()  throws Exception {
        try (ZipInputStream zis = new ZipInputStream(cl.getResourceAsStream("ArchiveWithCsv.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                assertThat(entry.getName()).endsWith(".csv");
                CSVReader csvReader = new CSVReader(new InputStreamReader(zis,StandardCharsets.UTF_8));
                List<String[]> strings = csvReader.readAll();
                Assertions.assertEquals(10, strings.size());
                Assertions.assertArrayEquals(
                            new String [] {"1","Eldon Base for stackable storage shelf, platinum","Muhammed MacIntyre","3","-213.25","38.94","35","Nunavut","Storage & Organization","0.8"},strings.get(0)
                    );
            }
        }
    }

    @DisplayName("Валидация .xlsx файла внутри .zip архива")
    @Test
    void validateXlsxInZipFileTest() throws Exception {
        try (ZipInputStream zis = new ZipInputStream(cl.getResourceAsStream("ArchiveWithXlsx.zip"))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                assertThat(entry.getName()).endsWith(".xlsx");
                XLS xls = new XLS(zis);
                String value = xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue();
                Assertions.assertTrue(value.contains("Тестовые данные 1"));
            }
        }
    }

}