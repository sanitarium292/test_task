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
        DataFormatter formatter = new DataFormatter();


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

        Map<Integer, Cell> resultsMin = new HashMap<>();
        Map<Integer, Cell> resultsMax = new HashMap<>();


        for (Map.Entry<Integer, String> entry : columnsType.entrySet()) {
            //заповнюємо масив даними із текстових столбців
            if (entry.getValue().equals("str")) {
                List<Cell> inputStr = new ArrayList<>();
                for (Row row : sheet) {
                    Cell cell = row.getCell(entry.getKey());
                    if (cell != null) {
                        //оскільки функція виводить цілі числи з ".0" в кінці, то обрізаємо їх
                        inputStr.add(cell);
                    }
                }
                //сортировка
                Cell min = getMinMaxStr(inputStr).get("min");
                Cell max = getMinMaxStr(inputStr).get("max");

                System.out.println("Для колонки " + entry.getKey() + ": min=" + min.getStringCellValue() + " в строці "
                        + min.getAddress().getRow() + " max=" + max.getStringCellValue() + " в строці " + max.getAddress().getRow());

            } else {
                //заповнюємо масив даними із числових столбців
                List<Cell> resultInt = new ArrayList<>();
                for (Row row : sheet) {
                    Cell cell = row.getCell(entry.getKey());
                    if (cell != null) {
                        resultInt.add(cell);
                    }
                }
                Cell min = getMinMaxInt(resultInt, formatter).get("min");
                Cell max = getMinMaxInt(resultInt, formatter).get("max");

                System.out.println("Для колонки " + entry.getKey() + ": min=" + min.toString() + " в строці "
                        + min.getAddress().getRow() + " max=" + max.toString() + " в строці " + max.getAddress().getRow());
            }
        }
    }


    //мін макс для числових колонок
    private static Map<String, Cell> getMinMaxInt(List<Cell> cellList, DataFormatter formatter) {

        Map<String, Cell> result = new HashMap<>();

        //лист з перетворенними данними
        List<Integer> formatted = new ArrayList<>();
        for (Cell cell : cellList) {
            String input = formatter.formatCellValue(cell);
            if (input.endsWith(".0")) {
                input = input.substring(0, input.length() - 2);
            }
            formatted.add(Integer.valueOf(input));

        }
        //сортування
        formatted = formatted.stream().sorted().collect(Collectors.toList());

        int min = formatted.get(0);
        int max = formatted.get(formatted.size() - 1);

        //знаходимо мінімум та максимум порівнянням по значенням
        int countMin = 0;
        int countMax = 0;

        for (Cell cell : cellList) {
            //приводимо до числа
            int cellInt = Integer.valueOf(formatter.formatCellValue(cell));

            if (cellInt == min & countMin == 0) {
                result.put("min", cell);
                countMin++;
            } else if (cellInt == max & countMax == 0) {
                result.put("max", cell);
                countMax++;
            }
        }
        return result;
    }

    //мін макс для текстових колонок
    private static Map<String, Cell> getMinMaxStr(List<Cell> cellList) {

        List<String> cellListStr = new ArrayList<>();
        Map<String, Cell> result = new HashMap<>();


        //масив з текстовим значенням колонок
        for (Cell cell : cellList) {
            cellListStr.add(cell.toString());
        }

        //сортування по розміру тексту
        Collections.sort(cellListStr, Comparator.comparingInt(String::length));
        String min = cellListStr.get(0);
        String max = cellListStr.get(cellListStr.size() - 1);

        int countMin = 0;
        int countMax = 0;

        //знаходимо мінімум та максимум порівнянням по значенням
        for (Cell cell : cellList) {
            if (cell.toString().equals(min) & countMin == 0) {
                result.put("min", cell);
                countMin++;
            } else if (cell.toString().equals(max) & countMax == 0) {
                result.put("max", cell);
                countMax++;
            }
        }
        return result;
    }
}

