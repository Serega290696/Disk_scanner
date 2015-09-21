package myProject.model;

import javafx.concurrent.Task;
import myProject.view.MainWindowController;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.function.Function;

/**
 * Created by serega on 02.09.2015.
 */
public class DiskAnalyzer extends Task {
    private static final Logger LOGGER_SCANNER = Logger.getLogger(DiskAnalyzer.class);
    private String path = "D:\\Downloads";

    private File chosenFile;

    private long chosenFileSize = 0;
    private short levelList = 0;

    private long subDirectoryOrFileSize = 0;

    //    private int maxLengthName;
//    private final String emptyString = "                                     ";
    private ArrayList<ResultFile> resultList = new ArrayList<>();
    private DataWorker dataWorker = new DataWorker();
    public final int PROGRESS_SPECIFICATION = 500;
    public ArrayList<Integer> filesAmountOnLvl = new ArrayList<>();

    //    public int filesAmountOnLvl[] = new int[PROGRESS_SPECIFICATION];
    public double processStatus = 0.0;
    private long bt = new Date().getTime();

    private static final String SPECIAL_CHAR = "  ";

    private StringBuilder lvlTab;

    private StringBuilder reportString = new StringBuilder();
    private ReportGenerator rg = new ReportGenerator();
    //    private StringBuilder report = new StringBuilder();
    private Calendar dateAnalysis = new GregorianCalendar();
    private long totalTime;
    private int dirFactor = 1;
    private DAOFiles daoFiles = new DAOFiles();
    //    private long t1 = 0;
    private long t1 = 0;
    private long t2 = 0;
//    private long t3 = 0;
//    private long t4 = 0;
//    private long t2 = new Date().getTime();
//    private long t3 = new Date().getTime();
//    private long t4 = new Date().getTime();

    public Function<Integer, Double> funProgress = x ->
    {
        long t = new Date().getTime();
        double y = 1;
        for (int i = 0; i < levelList; i++) {
            y *= (double) dirFactor / filesAmountOnLvl.get(i);// 1 * 10/34 * 10/12
        }
        y *= (double) x / filesAmountOnLvl.get(levelList);//y * 1/13
//        System.out.println(new Date().getTime() - t);
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
//        System.out.println("Chosen " + (chosenFile.isFile() ? "file" : "") + (chosenFile.isDirectory() ? "directory" : "") + ": '" + chosenFile.getName() + "'");
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
//                EntityFile subDirOrFile;
//                if ((subDirOrFile = daoFiles.get(f.getAbsolutePath())) != null) {
//                    long fSize = subDirOrFile.getSize();
//                    chosenFileSize += fSize;
//                    subDirectoryOrFileSize = fSize;
//                    String reportPattern = lvlTab + "-" + f.getName() + "(" + dataWorker.convert(fSize) + ")" + rg.getSeparator();
//                    reportString.append(reportPattern);
//                    increase(false);
//                    subDirOrFile.setLast_used(new java.sql.Date(new Date().getTime()));
//                    daoFiles.update(subDirOrFile);
//                }
//                else {
                    fileSizeCalc(f);
//                    daoFiles.insert(new EntityFile(f.getAbsolutePath(), new Date(f.lastModified()), subDirectoryOrFileSize, new java.sql.Date(new Date().getTime())));
//                }
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
        for (int i = 0; i < MainWindowController.maxBarsAmount && iter.hasNext(); i++)
            if (iter.hasNext()) {
                iter.next();
            }
        while (iter.hasNext()) {
            ResultFile rf = iter.next();
            iter.remove();
            otherFilesSize += rf.getFinallySize();
        }
        resultList.add(new ResultFile("Other\n" + dataWorker.convert(otherFilesSize, true), otherFilesSize));
        totalTime = new Date().getTime() - bt;
        if (LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("Finish! File: '" + chosenFile + "'. Size: " + dataWorker.convert(chosenFileSize, true) + ". Time: " + (new Date().getTime() - bt) + "\n");
        System.out.println(new Date().getTime() - bt);
        System.out.println("T1 : " + t1);
        System.out.println("T2 : " + t2);
    }

    private Integer calcParam2(File chosenFile) {
        long t = new Date().getTime();
        int a = 0;
        for (File f : chosenFile.listFiles())
            if (f.isFile())
                a += 1;
            else a += 10;
//        t2 += new Date().getTime() - t;
        return a;
    }

    private Integer calcParam(File chosenFile) {
//        int a = 0;
//        for (File f : chosenFile.listFiles())
//        a += chosenFile.list().length;
        return chosenFile.list().length;
    }

    private void fileSizeCalc(final File f1) {
//        System.out.println("A: " + f1);
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
//            if (file != null) {
//                fSize = file.getSize();
//                chosenFileSize += fSize;
//                subDirectoryOrFileSize += fSize;
//                increase(false);
//                return;
//            } else {
//            }
//            increase(true);
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
        long t = new Date().getTime();
        if (LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("Process status: " + processStatus + " -> " + (processStatus + (1d / (double) filesAmountOnLvl.get(0))) +
                    ". Files amount: " + filesAmountOnLvl.get(levelList) +
                    ". Cur lvl list: " + levelList);
//        processStatus += 1d / (double) filesAmountOnLvl[0];
        int x = isDir ? dirFactor : 1;
        double tmp = processStatus;
        processStatus += funProgress.apply(x);
//        LOGGER_SCANNER.error("Pr status = " + tmp + " (" + x + ") -> " + processStatus);
//        processStatus += funProgress.apply(x);
//        processStatus += x / filesAmountOnLvl[levelList];

        updateProgress(processStatus, 1.0);
    }

    public void setPath(String path) {
        this.path = path;
    }

    private void clearAll() {
        t2 = 0;
        filesAmountOnLvl = new ArrayList<>();
//        for (int i = 0; i < PROGRESS_SPECIFICATION; i++) {
//            filesAmountOnLvl.set(i, 0);
//        }
        resultList.clear();
        chosenFileSize = 0;
        reportString = new StringBuilder("");
        levelList = 0;
        lvlTab = new StringBuilder(" ");
        bt = new Date().getTime();
        subDirectoryOrFileSize = 0;
//        maxLengthName = 0;
        processStatus = 0;
    }

    private void sortList() {
        int startIndex = 0;
        int endIndex = resultList.size() - 1;
        doSort(startIndex, endIndex);
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


    public ArrayList<ResultFile> getResultList() {
//        Function<Integer, Integer> fun = MainWindowController.getFun2();
//        for (int i = 0; i < resultList.size(); i++) {
//            fun.apply(i);
//        }
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

    private void levelUp(int filesAmount) {
        long t = new Date().getTime();
        if (LOGGER_SCANNER.isTraceEnabled())
            LOGGER_SCANNER.trace("Go in subdir");
        levelList++;
        t1++;
//        long tf = new Date().getTime();
        if (filesAmountOnLvl.size() - 1 < levelList)
            filesAmountOnLvl.add(levelList, filesAmount);
        else
            filesAmountOnLvl.set(levelList, filesAmount);
//        t2 += new Date().getTime() - tf;
        lvlTab.append(SPECIAL_CHAR);

//        System.out.print("{");
//        for (int i = 0; i < filesAmountOnLvl.size() - 1; i++) {
//            System.out.print(filesAmountOnLvl.get(i) + ", ");
//        }
//        System.out.println("}  ");
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
