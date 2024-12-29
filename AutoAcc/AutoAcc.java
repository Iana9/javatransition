package AutoAcc;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class AutoAcc {
    public HashMap<Integer, HashMap<String, Double[]>> getYearAcc() {
        return yearAcc;
    }

    public HashMap<Integer, HashMap<Integer, HashMap<String, HashMap<String, Double>>>> getMonthAcc() {
        return monthAcc;
    }

    public AutoAcc() {
    }

    private HashMap<Integer,HashMap<String,Double[]>> yearAcc = new HashMap<>();
    private HashMap<Integer,HashMap<Integer,HashMap<String,HashMap<String,Double>>>> monthAcc = new HashMap<>();

    public static void main(String[] arg) {
        AutoAcc autoAcc = new AutoAcc();
        autoAcc.GetDataMonth();
        autoAcc.GetDataYear();
        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        while (running) {
            System.out.println("Enter command:\n1 - Monthly Report\n2 - Year Report\n3 - Check Consistency\n4 - Exit");
            int userInput = scanner.nextInt();
            switch (userInput) {
                case 1:
                    autoAcc.MonthlyReport();
                    break;
                case 2:
                    autoAcc.YearReport();
                    break;
                case 3:
                    autoAcc.IsDataConsistent();
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    break;
            }
        }    
        scanner.close(); 
    }

    public class Item {
        public String item;
        public Double total;

        public Item() {
            this.total = 0.0;
        }
    }

    private void GetDataYear() {
        for (Path i : findFiles("glob:**y.*.csv", "")) {
            Pattern pattern = Pattern.compile("\\d{4}");
            Matcher matcher = pattern.matcher(i.toString());
            if (matcher.find()) {
                Double[] incomes = new Double[12];
                Double[] expenses = new Double[12];
                HashMap<String, Double[]> innerMap = new HashMap<>();
                String[] lines = readFileContentsOrNull(i).split("\\n");
                for (int j=1; j<lines.length;++j) {
                    String[] lineContents = lines[j].split(",");
                    Integer monthInt = Integer.parseInt(lineContents[0]);
                    if (lineContents[2].trim().equals("TRUE")) {
                        expenses[monthInt-1] = Double.parseDouble(lineContents[1]);
                    } else {
                        incomes[monthInt-1] = Double.parseDouble(lineContents[1]);
                    }
                }
                innerMap.put("expenses", expenses);
                innerMap.put("incomes", incomes);
                yearAcc.put(Integer.parseInt(matcher.group(0)), innerMap);
            }
        }
    }

    private void GetDataMonth() {
        for (Path i : findFiles("glob:**m.*.csv", "")) {
            Pattern pattern = Pattern.compile("(\\d{4})(\\d{2})");
            Matcher matcher = pattern.matcher(i.toString());
            if (matcher.find()) {
                Integer year = Integer.parseInt(matcher.group(1));
                Integer month = Integer.parseInt(matcher.group(2));
                HashMap<String,Double> innerMapExp = new HashMap<>();
                HashMap<String,Double> innerMapInc = new HashMap<>();
                String[] lines = readFileContentsOrNull(i).split("\\n");
                for (int j=1; j<lines.length;++j) {
                    String[] lineContents = lines[j].split(",");
                    Double total = Double.parseDouble(lineContents[2]) * Double.parseDouble(lineContents[3]);
                    if (lineContents[1].trim().equals("TRUE")) {
                        innerMapExp.put(lineContents[0], total);
                    } else {
                        innerMapInc.put(lineContents[0], total);
                    }
                }
                HashMap<String,HashMap<String,Double>> categoryMap = new HashMap<>();
                categoryMap.put("expenses", innerMapExp);
                categoryMap.put("incomes", innerMapInc);
                monthAcc.putIfAbsent(year, new HashMap<>());
                monthAcc.get(year).put(month, categoryMap);
            }
        }
    }

    private void YearReport() {
        GetDataYear();
        Double avgIncome = 0.0;
        Double avgExpanse = 0.0;
        for (int i : yearAcc.keySet()) {
            Double[] income =  yearAcc.get(i).get("incomes");
            Double[] expense =  yearAcc.get(i).get("expenses");
            for (int m=0; m<12; ++m) {
                if (income[m] != null && expense[m] != null) {
                    avgIncome += income[m];
                    avgExpanse +=  expense[m];
                    System.out.println("Profit for " + numberToMonthString(m+1) + ": " + (income[m] -  expense[m]));
                }
            }
            System.out.println("Average income: " + avgIncome / income.length);
            System.out.println("Average expanse: " + avgExpanse / expense.length);
        }
    }

    private void MonthlyReport() {
        for (int year : monthAcc.keySet()) {
            for (int month : monthAcc.get(year).keySet()) {
                Item maxExpense = new Item();
                Item maxIncome = new Item();
                for (String item : monthAcc.get(year).get(month).get("expenses").keySet()) {
                    Double currItemTotal = monthAcc.get(year).get(month).get("expenses").get(item);
                    if (maxExpense.total < currItemTotal) {
                        maxExpense.item = item;
                        maxExpense.total = currItemTotal;
                    }
                }
                for (String item : monthAcc.get(year).get(month).get("incomes").keySet()) {
                    Double currItemTotal = monthAcc.get(year).get(month).get("incomes").get(item);
                    if (maxIncome.total < currItemTotal) {
                        maxIncome.item = item;
                        maxIncome.total = currItemTotal;
                    }
                }
                System.out.println(numberToMonthString(month));
                System.out.println("Maximimum income: " + maxIncome.item + " " + maxIncome.total);
                System.out.println("Maximimum expense: " + maxExpense.item + " " + maxExpense.total);
            }
        }
    }

    private void IsDataConsistent() {
        for (int i : yearAcc.keySet()) {
            Double[] income =  yearAcc.get(i).get("incomes");
            Double[] expense =  yearAcc.get(i).get("expenses");
            for (int m=1; m<+12; ++m) {
                Double incMonth = 0.0;
                Double expMonth = 0.0;
                if (income[m-1] != null) {
                    if (monthAcc.get(i).get(m) != null && monthAcc.get(i).get(m).get("incomes") != null) {
                        for (String item : monthAcc.get(i).get(m).get("incomes").keySet()) {
                            incMonth += monthAcc.get(i).get(m).get("incomes").get(item);
                        }
                        if (Math.abs(incMonth - income[m-1]) > 0.00000000001) {
                            System.out.println("Error in " + numberToMonthString(m+1));
                            break;
                        }
                    } else {
                        System.out.println("Error in " + numberToMonthString(m+1));
                    }
                }
                if (expense[m-1] != null) {
                    if (monthAcc.get(i).get(m) != null && monthAcc.get(i).get(m).get("expenses") != null) {
                        for (String item : monthAcc.get(i).get(m).get("expenses").keySet()) {
                            expMonth += monthAcc.get(i).get(m).get("expenses").get(item);
                        }
                        if (Math.abs(expMonth - expense[m-1]) > 0.00000000001) {
                            System.out.println("Error in " + numberToMonthString(m+1));
                            break;
                        }
                    } else {
                        System.out.println("Error in " + numberToMonthString(m+1));
                    }
                }
            }
        }
    } 

    private static String numberToMonthString(Integer monthNumber) {
        String[] months = {
            "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
            "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
        };
        return months[monthNumber-1];
    }

    private static String readFileContentsOrNull(Path path) 
    {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            System.out.println("File cannot be read");
            return null;
        }
    }

    private static List<Path> findFiles(String pattern, String path) {
        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(pattern);
        try {
            return Files.walk(Paths.get(path)).filter(pathMatcher::matches).collect(Collectors.toList());
        } catch(IOException e) {
            return null;
        }
    }

}