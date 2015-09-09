package myProject.model;

import myProject.view.MainWindowController;

import java.io.File;
import java.util.*;

/**
 * Created by serega on 02.09.2015.
 */
public class DiskAnalyzer {

    private String path = "D:\\Downloads";
    private File chosenFile;
    private static final String SPECIAL_CHAR = "--";


    private long chosenFileSize = 0;
    private StringBuilder resultString = new StringBuilder();

    private short levelList = 1;
    private StringBuilder lvlTab = new StringBuilder("-");
    private long bt = new Date().getTime();
    private long subDirectoryOrFileSize = 0;
    private int maxLengthName;
    private final String emptyString = "                                  ";
    private ArrayList<ResultFile> resultList = new ArrayList<ResultFile>();
    private DataWorker dataWorker = new DataWorker();
    private int numberDoneFiles = 0;

    private volatile boolean operationDone = false;

    public final int processStatusDetalization = 2;
    public int filesAmountOnLvl[] = new int[processStatusDetalization];
    public double processStatus = 0.0;
    private StringBuilder report = new StringBuilder();

    public void launch() {
        launch(path);
    }

    public void launch(final String incomingParameter) {
        launch(new File(incomingParameter));
    }

    public void setOperationDone(boolean operationDone) {
        this.operationDone = operationDone;
    }

    public synchronized void launch(final File file) {
//        operationDone = false;
        clearAll();
        if (!file.exists()) {
            try {
                throw new IncorrectFileSelected("File does not exist!");
            } catch (IncorrectFileSelected incorrectFileSelected) {
            }
            return;
        }
        chosenFile = file;
        System.out.println("Chosen " + (chosenFile.isFile() ? "file" : "") + (chosenFile.isDirectory() ? "directory" : "") + ": '" + chosenFile.getName() + "'");
        if (chosenFile.isFile()) {
            subDirectoryOrFileSize = 0;
            fileSizeCalc(chosenFile);
            resultList.add(new ResultFile(
//                    (f.getName().length()>10?f.getName().substring(0, 9):f.getName()) +
                    chosenFile.getName() +
                            "\n" + dataWorker.convert(subDirectoryOrFileSize, true),
                    subDirectoryOrFileSize
            ));
            report(chosenFile);
        } else {
            filesAmountOnLvl[0] = chosenFile.listFiles().length;
            for (File f : chosenFile.listFiles()) {

                subDirectoryOrFileSize = 0;
                fileSizeCalc(f);
                resultList.add(new ResultFile(
//                    (f.getName().length()>10?f.getName().substring(0, 9):f.getName()) +
                        f.getName() +
                                "\n" + dataWorker.convert(subDirectoryOrFileSize, true),
                        subDirectoryOrFileSize
                ));
                report(f);
            }
        }
        sortList();
        for (ResultFile f : resultList) {
//            System.out.println(f.getName() + "\n");
        }
        report();
        long otherFilesSize = 0;
        Iterator<ResultFile> iter = resultList.iterator();
        for (int i = 0; i < MainWindowController.maxBarsAmount && iter.hasNext(); i++)
            if (iter.hasNext()) {
                iter.next();
            }
        while (iter.hasNext()) {
            ResultFile rf = iter.next();
//            if (rf.getFinallySize() < resultList.get(0).getFinallySize() * 0.01) {
                iter.remove();
                otherFilesSize += rf.getFinallySize();
//            }
        }
        resultList.add(new ResultFile("Other\n" + dataWorker.convert(otherFilesSize, true), otherFilesSize));
//        for(File ff : resultList) {
//            System.out.println(ff);
//        }
//        System.out.println("OTHER: " + otherFilesSize + ": " + resultList.get(resultList.size() - 1).getFinallySize());
//        if (resultList.size() > 10) {
//            long otherFilesSize = 0;
//            Iterator<ResultFile> iter = resultList.iterator();
//            iter.next();
//            iter.next();
//            iter.next();
//            while (iter.hasNext()) {
//                ResultFile rf = iter.next();
//                if (rf.getFinallySize() < resultList.get(0).getFinallySize() * 0.01) {
//                    iter.remove();
//                    otherFilesSize += rf.getFinallySize();
//                }
//            }
//            resultList.add(new ResultFile("Other\n" + dataWorker.convert(otherFilesSize, true), otherFilesSize));
//            System.out.println("OTHER: " + otherFilesSize + ": " + resultList.get(resultList.size()-1).getFinallySize());
//        }
        operationDone = true;
    }

    private void fileSizeCalc(final File f1) {
//        System.out.println(f1.getName() + " : " + processStatus);
        if (f1.isFile()) {
            chosenFileSize += f1.length();
            subDirectoryOrFileSize += f1.length();
            String tmpStr = lvlTab + "" + f1.getName() + "(" + dataWorker.convert(f1.length()) + ")\t\n";
            resultString.append(tmpStr);
            if (levelList == 1)
                processStatus += 1d / filesAmountOnLvl[0];
//            if (levelList == 2)
//                processStatus += 1d / filesAmountOnLvl[1];
        } else {
            if (f1.list() != null) {
                if (f1.list().length != 0) {
                    for (File curFile : f1.listFiles()) {
                        levelUp();
                        if (levelList == 1) {
                            filesAmountOnLvl[1] = f1.listFiles().length;
                            processStatus += 1d / filesAmountOnLvl[0];
//                            System.out.println(levelList);
                        }
//                        if (levelList == 2) {
//                            filesAmountOnLvl[1] = f1.listFiles().length;
//                            processStatus += 1d / filesAmountOnLvl[1];
//                            System.out.println(levelList);
//                        }
                        fileSizeCalc(curFile);
                        levelDown();
                    }
                }
            }
        }
    }

    private void clearAll() {
        resultList.clear();
        chosenFileSize = 0;
        resultString = new StringBuilder("");
        levelList = 1;
        lvlTab = new StringBuilder("-");
        bt = new Date().getTime();
        subDirectoryOrFileSize = 0;
        maxLengthName = 0;
        numberDoneFiles = 0;
    }

    private void sortList() {

        int startIndex = 0;
        int endIndex = resultList.size() - 1;
        doSort(startIndex, endIndex);
//        resultList.re
    }

    private void doSort(int start, int end) {
        if (start >= end)
            return;
        int i = start, j = end;
        int cur = i - (i - j) / 2;
        while (i < j) {
            while (i < cur && (resultList.get(i).getFinallySize() >= resultList.get(cur).getFinallySize())) {
                i++;
            }
            while (j > cur && (resultList.get(cur).getFinallySize() >= resultList.get(j).getFinallySize())) {
                j--;
            }
            if (i < j) {
                ResultFile rfTmp = resultList.get(i);
                resultList.add(i, resultList.get(j));
                resultList.remove(i + 1);
                resultList.add(j, rfTmp);
                resultList.remove(j + 1);
                if (i == cur)
                    cur = j;
                else if (j == cur)
                    cur = i;
            }
        }
        doSort(start, cur);
        doSort(cur + 1, end);
    }

    private void consoleFormating() {
        fileSizeCalc(chosenFile);
        for (File f : chosenFile.listFiles()) {
            if (f.getName().length() > maxLengthName) maxLengthName = f.getName().length();
        }

        if ("File name".length() > maxLengthName + 3)
            System.out.print("|File name".substring(0, maxLengthName + 1));
        else
            System.out.print("|   File name" + emptyString.substring(0, maxLengthName + 2 - "|   File name".length()));
        if ("File name".length() > maxLengthName + 3)
            System.out.print("Size".substring(0, maxLengthName + 1));
        else
            System.out.print("|   Size" + emptyString.substring(0, maxLengthName - "|   Size".length()));
        System.out.print("\r\n|");
        for (int i = 0; i < maxLengthName + 1; i++) {
            System.out.print("-");
        }
        System.out.print("|");
        for (int i = 0; i < 25; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    private void report(File f) {
//        System.out.println("|" + f.getName().concat(emptyString.substring(0, maxLengthName - f.getName().length())) + " | \t" + dataWorker.convert(subDirectoryOrFileSize, true));
    }

    private void report() {
        report.append((chosenFile.isFile() ? "File" : "Directory") + " size: " + dataWorker.convert(chosenFileSize));
        report.append("Time: " + (new Date().getTime() - bt));
        System.out.println(report);
//        System.out.println((chosenFile.isFile() ? "File" : "Directory") + " size: " + dataWorker.convert(chosenFileSize));
//        System.out.println("Time: " + (new Date().getTime() - bt));
//        System.out.println("Result length: " + resultString.length());
    }

    public ArrayList<ResultFile> getResultList() {
        return resultList;
    }

    public ArrayList<String> getResultListFileNames() {
        ArrayList<String> listNames = new ArrayList<String>();
        for (ResultFile rf : resultList) {
            listNames.add(rf.getName());
        }
        return listNames;
    }

    public long getChosenFileSize() {
        return chosenFileSize;
    }

    private void levelUp() {
        levelList++;
        lvlTab.append(SPECIAL_CHAR);
    }

    private void levelDown() {
        levelList--;
        lvlTab.delete(lvlTab.length() - 1 - SPECIAL_CHAR.length(), lvlTab.length() - 1);
    }

    public boolean isOperationDone() {
        return operationDone;
    }

    public String getReport() {
        return new String(report);
    }
}
