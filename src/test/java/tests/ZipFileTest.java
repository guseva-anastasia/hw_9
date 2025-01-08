package tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.*;
import static com.codeborne.pdftest.assertj.Assertions.assertThat;


public class ZipFileTest {

    private void readZipFileStream (InputStream zipFileStream){
        final ZipInputStream is = new ZipInputStream(zipFileStream);
        ZipEntry entry;
        try {
            while ((entry = is.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().contains(".pdf")) {

                    PDF pdf = new PDF (is);
                    assertThat(pdf).containsExactText("Тестовый pdf файл");

                } else if (!entry.isDirectory() && entry.getName().contains(".csv")){

                    CSVReader csvReader = new CSVReader(new InputStreamReader(is,StandardCharsets.UTF_8));
                    List<String[]> strings = csvReader.readAll();
                    Assertions.assertEquals(10, strings.size());

                    Assertions.assertArrayEquals(
                            new String [] {"1","Eldon Base for stackable storage shelf, platinum","Muhammed MacIntyre","3","-213.25","38.94","35","Nunavut","Storage & Organization","0.8"},strings.get(0)
                    );

                } else if (!entry.isDirectory() && entry.getName().contains(".xlsx")){
                    XLS xls = new XLS(is);
                    String value = xls.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue();
                    Assertions.assertTrue(value.contains("Тестовые данные 1"));
                }
            }
        } catch (IOException | CsvException e) {
            System.out.println("error reading zip file stream"+ e);
        }
    }

    @DisplayName("Валидация .pdf файла внутри .zip архива")
    @Test
    void validatePdfInZipFileTest() {
        InputStream zipFileStream = getClass().getClassLoader().getResourceAsStream("ArchiveWithPdf.zip");
        readZipFileStream (zipFileStream);
    }

    @DisplayName("Валидация .csv файла внутри .zip архива")
    @Test
    void validateCsvInZipFileTest() {
        InputStream zipFileStream = getClass().getClassLoader().getResourceAsStream("ArchiveWithCsv.zip");
        readZipFileStream (zipFileStream);
    }

    @DisplayName("Валидация .xlsx файла внутри .zip архива")
    @Test
    void validateXlsxInZipFileTest() {
        InputStream zipFileStream = getClass().getClassLoader().getResourceAsStream("ArchiveWithXlsx.zip");
        readZipFileStream (zipFileStream);
    }


}