import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
public class Main {
    static int rightNum;
    static int wrongHum;
    static int checks;
    static int k;

    public static void main(String[] args) {
        k = Integer.parseInt(args[0]);

        ArrayList<Iris> trainDataList = fileToList("./Train-set.txt");
        ArrayList<Iris> testDataList = fileToList("./Test-set.txt");

        testDataList.forEach(x -> checkNew(x,trainDataList));

        //Printing info about work done
        System.out.println("Checks is: " + checks);
        System.out.println("RightNum is: " + rightNum);
        System.out.println("WrongNum is: " + wrongHum);
        System.out.println("Accuracy is: " + String.format("%.2f", (double)rightNum/checks*100)  + "%");

        userInput(trainDataList);
    }

    //Input part which waits until user give new info or 0 to exit
    public static void userInput(ArrayList<Iris> trainDataList){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write new iris info \nWrite 0 if you want stop program");
        String input = scanner.nextLine();
        while (!input.equals("0")){
            Iris is = inputConvert(input);
            checkNew(is, trainDataList);
            System.out.println("Write new iris info \nWrite 0 if you want stop program");
            input = scanner.nextLine();
        }
    }

    //Making from user input line Object Iris
    public static Iris inputConvert(String info){
        String[] tmpLine = info.split(",");
        ArrayList<Double> tmpNums = new ArrayList<>();

        for (int i=0; i< tmpLine.length-1; i++){
            tmpNums.add(Double.parseDouble(tmpLine[i]));
        }

        return new Iris(tmpNums,tmpLine[tmpLine.length-1]);
    }

    //Making from path of file list of lines from file
    public static ArrayList<Iris> fileToList(String path){
        ArrayList<String> rawData = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                rawData.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Iris> dataList = new ArrayList<>();

        for (String datum : rawData) {
            String[] tmpLine = datum.split(",");
            ArrayList<Double> tmpNums = new ArrayList<>();

            for (int i=0; i< tmpLine.length-1; i++){
                tmpNums.add(Double.parseDouble(tmpLine[i]));
            }

            dataList.add(new Iris(tmpNums, tmpLine[tmpLine.length-1]));
        }

        return dataList;
    }

    //Check new Iris object using existing training data
    public static void checkNew(Iris iris, ArrayList<Iris> data){
        ArrayList<Iris> powDistance = new ArrayList<>();
        int vecNum = data.get(0).getNumData().size();

        System.out.println("Now we are checking " + iris + " from test list");

        double tmpsum = 0;
        for (Iris datum : data) {
            for (int j = 0; j < vecNum; j++) {
                tmpsum += Math.pow((iris.getNumData().get(j) - datum.getNumData().get(j)), 2);
            }
            powDistance.add(new Iris(tmpsum, datum.getDecision()));
            tmpsum = 0;
        }

        Comparator<Iris> byDist = Comparator.comparingDouble(Iris::getDistance);
        powDistance.sort(byDist);
        String[] tmpArr = new String[k];
        for (int i = 0; i < k; i++) {
            tmpArr[i] = powDistance.get(i).getDecision();
        }

        System.out.println("-Program decided that this iris is: " + mostFrequent(tmpArr));
        System.out.println("-The true is: " + iris.getDecision());

        if (mostFrequent(tmpArr).equals(iris.getDecision())) {
            rightNum++;
        } else {
            wrongHum++;
        }

        checks++;
    }

    //Counting most frequent element in array
    public static String mostFrequent(String[] arr){
        int maxcount = 0;
        String element_having_max_freq = null;
        for (String s : arr) {
            int count = 0;
            for (String value : arr) {
                if (s.equals(value)) {
                    count++;
                }
            }

            if (count > maxcount) {
                maxcount = count;
                element_having_max_freq = s;
            }
        }

        return element_having_max_freq;
    }

}
