package myProject.model;

import javafx.concurrent.Task;
import myProject.view.MainWindowController;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.function.Function;

public class DiskAnalyzer extends Task {
    private static final Logger LOGGER_SCANNER = Logger.getLogger(DiskAnalyzer.class);
    private String path = "D:\\Downloads";

    private File chosenFile;

    private long chosenFileSize = 0;
    private short levelList = 0;

    private long subDirectoryOrFileSize = 0;

    private ArrayList<ResultFile> resultList = new ArrayList<>();
    private DataWorker dataWorker = new DataWorker();
    public ArrayList<Integer> filesAmountOnLvl = new ArrayList<>();

    public double processStatus = 0.0;
    private long bt = new Date().getTime();

    private static final String SPECIAL_CHAR = "  ";

    private StringBuilder lvlTab;

    private StringBuilder reportString = new StringBuilder();
    private ReportGenerator rg = new ReportGenerator();
    private Calendar dateAnalysis = new GregorianCalendar();
    private long totalTime;
    private int dirFactor = 1;
    private long t1 = 0;
    private long t2 = 0;

    public Function<Integer, Double> funProgress = x ->
    {
        double y = 1;
        for (int i = 0; i < levelList; i++) {
            y *= (double) dirFactor / filesAmountOnLvl.get(i);// 1 * 10/34 * 10/12
        }
        y *= (double) x / filesAmountOnLvl.get(levelList);//y * 1/13
        return y;
    };

    @Override
    public Object call() {
        if (LOGGER_SCANNER.isInfoEnabled())
            LOGGER_SCANNER.info("\n===================\nScan started!");
        try {
            launch();
        } catch (IncorrectFileSelected incorrectFileSelected) {
            incorrectFileSelected.printStackTrace();
        }
        return null;
    }

    public void launch() throws IncorrectFileSelected {
        launch(path);
    }

    public void launch(final String incomingParameter) throws IncorrectFileSelected {
        launch(new File(incomingParameter));
    }

    @SuppressWarnings("all")
    public synchronized void launch(final File file) throws IncorrectFileSelected {
        if (LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("Launched!");
        dateAnalysis = new GregorianCalendar();
        clearAll();
        if (!file.exists()) {
            updateProgress(1, 1);
            LOGGER_SCANNER.error("File: '" + file + "' doesn't exist!");
            throw new IncorrectFileSelected("File does not exist!");
        }
        chosenFile = file;
        if (LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("Current " + (chosenFile.isFile() ? "file" : "directory") + ": '" + chosenFile.getName() + "'");
        if (chosenFile.isFile()) {
            subDirectoryOrFileSize = 0;
            fileSizeCalc(chosenFile);
            resultList.add(new ResultFile(
                    chosenFile.getName() +
                            "\n" + dataWorker.convert(subDirectoryOrFileSize, true),
                    subDirectoryOrFileSize
            ));
        } else {
            processStatus = 0;
            filesAmountOnLvl.add(0, calcParam(chosenFile));
            for (File f : chosenFile.listFiles()) {
                subDirectoryOrFileSize = 0;
                fileSizeCalc(f);
                resultList.add(new ResultFile(
                        f.getName() +
                                "\n" + dataWorker.convert(subDirectoryOrFileSize, true),
                        subDirectoryOrFileSize
                ));
            }
        }
        sortList();
        long otherFilesSize = 0;
        Iterator<ResultFile> iter = resultList.iterator();
        if (resultList.size() > 1) {
            long minSize = (int) ((double) resultList.get(0).getFinallySize() / 10);
            for (int i = 0; i < MainWindowController.maxBarsAmount && iter.hasNext() && iter.next().getFinallySize() > minSize; i++);
            while (iter.hasNext()) {
                ResultFile rf = iter.next();
                iter.remove();
                otherFilesSize += rf.getFinallySize();
            }
            if (otherFilesSize > 0)
                resultList.add(new ResultFile("Other\n" + dataWorker.convert(otherFilesSize, true), otherFilesSize));
        }
        totalTime = new Date().getTime() - bt;
        if (LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("Finish! File: '" + chosenFile + "'. Size: " + dataWorker.convert(chosenFileSize, true) + ". Time: " + (new Date().getTime() - bt) + "\n");
        System.out.println(new Date().getTime() - bt);
        System.out.println("T1 : " + t1);
        System.out.println("T2 : " + t2);
    }

    private Integer calcParam(File chosenFile) {
        return chosenFile.list().length;
    }

    @SuppressWarnings("all")
    private void fileSizeCalc(final File f1) {
        if (f1.isFile()) {
            long t = new Date().getTime();
            long fSize = f1.length();
            chosenFileSize += fSize;
            subDirectoryOrFileSize += fSize;
            t2 += new Date().getTime() - t;
            String reportPattern = lvlTab + "-" + f1.getName() + "(" + dataWorker.convert(fSize) + ")" + rg.getSeparator();
            reportString.append(reportPattern);
            increase(false);
        } else {
            long fSize;
            fSize = f1.length();
            String reportPattern = lvlTab + "-" + f1.getName() + "(" + dataWorker.convert(fSize) + ")" + rg.getSeparator();
            reportString.append(reportPattern);
            if (f1.list() != null) {
                if (f1.list().length != 0) {
                    File[] files = f1.listFiles();
                    for (File curFile : files) {
                        levelUp(files.length);
                        fileSizeCalc(curFile);
                        levelDown();
                    }
                } else increase(true);
            } else increase(true);
        }
    }

    private void increase(boolean isDir) {
        if (LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("Process status: " + processStatus + " -> " + (processStatus + (1d / (double) filesAmountOnLvl.get(0))) +
                    ". Files amount: " + filesAmountOnLvl.get(levelList) +
                    ". Cur lvl list: " + levelList);
        int x = isDir ? dirFactor : 1;
        processStatus += funProgress.apply(x);
        updateProgress(processStatus, 1.0);
    }

    public void setPath(String path) {
        this.path = path;
    }

    private void clearAll() {
        t2 = 0;
        filesAmountOnLvl = new ArrayList<>();
        resultList.clear();
        chosenFileSize = 0;
        reportString = new StringBuilder("");
        levelList = 0;
        lvlTab = new StringBuilder(" ");
        bt = new Date().getTime();
        subDirectoryOrFileSize = 0;
        processStatus = 0;
    }

    private void sortList() {
        int startIndex = 0;
        int endIndex = resultList.size() - 1;
        doSort(startIndex, endIndex);
    }

    @SuppressWarnings("all")
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


    public ArrayList<ResultFile> getResultList() {
        return resultList;
    }

    public long getChosenFileSize() {
        return chosenFileSize;
    }

    private void levelUp(int filesAmount) {
        if (LOGGER_SCANNER.isTraceEnabled())
            LOGGER_SCANNER.trace("Go in subdir");
        levelList++;
        t1++;
        if (filesAmountOnLvl.size() - 1 < levelList)
            filesAmountOnLvl.add(levelList, filesAmount);
        else
            filesAmountOnLvl.set(levelList, filesAmount);
        lvlTab.append(SPECIAL_CHAR);
    }

    private void levelDown() {
        if (LOGGER_SCANNER.isTraceEnabled())
            LOGGER_SCANNER.trace("Go in superdir");
        levelList--;
        lvlTab.delete(lvlTab.length() - 1 - SPECIAL_CHAR.length(), lvlTab.length() - 1);
    }

    public File getChosenFile() {
        return chosenFile;
    }

    public Calendar getDateAnalysis() {
        return dateAnalysis;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public StringBuilder getReportString() {
        return reportString;
    }
}
