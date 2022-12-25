package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        //обробляємо excel файл, який знаходиться по заданому шляху
        FileInputStream file = new FileInputStream(new File("/Users/yehorshytikov/test1.xlsx"));
        Workbook workbook = new XSSFWorkbook(file);
        //беремо перший лист
        Sheet sheet = workbook.getSheetAt(0);

        boolean firstItr = true;

        //проходимо по строкам та записуємо які колонки числові, які текстові
        //якщо в колонці є клітинка з текстовим значенням, присвоюємо колонці, що вона текстова
        // + дізнаємося які колонки заповнені та їх кількість
        Map<Integer, String> columnsType = new HashMap<>();
        for (Row row : sheet) {
            for (Cell cell : row) {
                //для першої ітерації
                if (firstItr) {
                    if (cell.getCellType() == CellType.NUMERIC) {
                        columnsType.put(cell.getColumnIndex(), "num");
                    } else {
                        columnsType.put(cell.getColumnIndex(), "str");
                    }
                } else {
                    if (cell.getCellType() != CellType.NUMERIC) {
                        columnsType.put(cell.getColumnIndex(), "str");
                    }
                }
            }
            firstItr = false;
        }
        //для числових столбців
        Map<Integer, List<Integer>> mapInt = new HashMap<>();
        //для текстових
        Map<Integer, List<String>> mapStr = new HashMap<>();


        for (Map.Entry<Integer, String> entry : columnsType.entrySet()) {
            //заповнюємо масив даними із текстових столбців
            if (entry.getValue().equals("str")) {
                List<String> resultStr = new ArrayList<>();
                for (Row row : sheet) {
                    Cell cell = row.getCell(entry.getKey());
                    if (cell != null) {
                        //оскільки функція виводить цілі числи з ".0" в кінці, то обрізаємо їх
                        resultStr.add(cellProcessor(cell));
                    }
                }
                mapStr.put(entry.getKey(), resultStr);
                //заповнюємо масив даними із числових столбців
            } else {
                List<Integer> resultInt = new ArrayList<>();
                for (Row row : sheet) {
                    Cell cell = row.getCell(entry.getKey());
                    if (cell != null) {
                        resultInt.add(Integer.valueOf(cellProcessor(cell)));
                    }
                }
                mapInt.put(entry.getKey(), resultInt);
            }
        }


        //повертаємо результати по колонкам
        for (Map.Entry<Integer, String> entry : columnsType.entrySet()) {

            if (entry.getValue().equals("str")) {
                List<String> results = getMinMaxStr(mapStr, entry.getKey());
                System.out.println("Column " + entry.getKey() + ": Min " + results.get(0) + " Max " + results.get(1));
            } else {
                List<Integer> results = getMinMaxInt(mapInt, entry.getKey());
                System.out.println("Column " + entry.getKey() + ": Min " + results.get(0) + " Max " + results.get(1));
            }
        }
    }

    //для обрізання цілих чисел
    private static String cellProcessor(Cell cell) {
        String result = cell.toString();
        if (result.endsWith(".0")) {
            result = result.substring(0, result.length() - 2);
        }
        return result;
    }

    //мін макс для числових колонок
    private static List<Integer> getMinMaxInt(Map<Integer, List<Integer>> map, int index) {
        List<Integer> input = map.get(index);
        List<Integer> sortedList = input.stream().sorted().collect(Collectors.toList());
        List<Integer> result = new ArrayList<>();
        int min = sortedList.get(0);
        int max = sortedList.get(sortedList.size() - 1);
        result.add(min);
        result.add(max);
        return result;
    }

    //мін макс для текстових колонок
    private static List<String> getMinMaxStr(Map<Integer, List<String>> map, int index) {
        List<String> input = map.get(index);
        List<String> result = new ArrayList<>();
        Collections.sort(input, Comparator.comparingInt(String::length));
        String min = input.get(0);
        String max = input.get(input.size() - 1);
        result.add(min);
        result.add(max);
        return result;
    }

}