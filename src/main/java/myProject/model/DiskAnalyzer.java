package myProject.model;

import javafx.concurrent.Task;
import myProject.view.MainWindowController;
import org.apache.log4j.Logger;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

/**
 * Created by serega on 02.09.2015.
 */
public class DiskAnalyzer extends Task {
    private static final Logger LOGGER_SCANNER = Logger.getLogger(DiskAnalyzer.class);
    private static final int TIME_SPAN_AMOUNT = 10;
    private String path = "D:\\Downloads";

    private File chosenFile;

    private long chosenFileSize = 0;
    private short levelList = 0;

    private long subDirectoryOrFileSize = 0;

    private ArrayList<ResultFile> resultList = new ArrayList<>();

    private ArrayList<ResultFile[]> resultDatesList = new ArrayList<ResultFile[]>();
    private LocalDateTime[] datesList = new LocalDateTime[7];

    private DataWorker dataWorker = new DataWorker();

    //    public final int PROGRESS_SPECIFICATION = 500;
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
    //    private long t1 = 0;
    private long t1 = 0;
    private long t2 = 0;
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
    private int currentFile = 0;

    public DiskAnalyzer() {
        clearAll();
    }

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
                int i;
                for (i = 0; i < datesList.length; i++) ;
                ResultFile[] resFileMass = new ResultFile[i--];
//                Arrays.fill(resFileMass, new ResultFile(
//                        f.getName() + "\n" + dataWorker.convert(subDirectoryOrFileSize, true),
//                        subDirectoryOrFileSize));
                for (int j = 0; j < resFileMass.length; j++) {
                    resFileMass[j] = new ResultFile(
                            f.getName() + "\n" + dataWorker.convert(subDirectoryOrFileSize, true),
                            subDirectoryOrFileSize);
                }
                resultDatesList.add(resFileMass);
                fileSizeCalc(f);
                resultList.add(new ResultFile(
                        f.getName() +
                                "\n" + dataWorker.convert(subDirectoryOrFileSize, true),
                        subDirectoryOrFileSize
                ));
                System.out.println("============= "+currentFile+" =============");
                for (int j = 0; j < resultDatesList.get(currentFile).length; j++) {
                    System.out.println(resultDatesList.get(currentFile)[j] + "   S: " +
                                    resultDatesList.get(currentFile)[j].getFinallySize()
                    );
                }
                System.out.println("==========================================");
                currentFile++;
            }
        }
        if(LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("Sorting is started!");
//        sortList(resultList);
        if(LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("Sorting is finished!");
        if(MainWindowController.isdListIsOn()) {
            sortList(resultDatesList);
        } else
            sortList();
        if(LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("Sorting dates is finished!");
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
        if (otherFilesSize > 0)
            resultList.add(new ResultFile("Other\n" + dataWorker.convert(otherFilesSize, true), otherFilesSize));
        totalTime = new Date().getTime() - bt;
        if (LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("Finish! File: '" + chosenFile + "'. Size: " + dataWorker.convert(chosenFileSize, true) + ". Time: " + (new Date().getTime() - bt) + "\n");
//        System.out.println(new Date().getTime() - bt);
//        System.out.println("T1 : " + t1);
//        System.out.println("T2 : " + t2);
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
            addToReList(f1, fSize);
            t2 += new Date().getTime() - t;
            String reportPattern = lvlTab + "-" + f1.getName() + "(" + dataWorker.convert(fSize) + ")" + rg.getSeparator();
            reportString.append(reportPattern);
            increase(false);
        } else {
            long fSize;
            fSize = f1.length();
            addToReList(f1, fSize);
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

    private void addToReList(File f1, long fSize) {
        subDirectoryOrFileSize += fSize;
        int i = 0;
        for (i = 0; i < datesList.length && datesList[i] != null; i++) {
            if (new Date(f1.lastModified()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                    .isAfter(datesList[i])) {
//                System.out.println("HERE " + i);
                if (i >0)
                    i--;
                break;
            }
        }
        if (i > 6)
            i--;
//        System.out.println("date list: " + datesList.length);
//        System.out.println("result date list: " + resultDatesList.size());
//        System.out.println("file number in mass = " + i);
//        System.out.println("rees list length = " + resultDatesList.get(currentFile).length);
//        System.out.println("*** " + i);
//        System.out.println(resultDatesList.get(currentFile)[0].getFinallySize());
//        System.out.println(resultDatesList.get(currentFile)[i].getFinallySize());
        resultDatesList.get(currentFile)[i].incFinallySize(fSize);
//        System.out.println(resultDatesList.get(currentFile)[0].getFinallySize());
//        System.out.println(resultDatesList.get(currentFile)[i].getFinallySize());
//        System.out.println("***");
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
        resultDatesList.clear();
        chosenFileSize = 0;
        reportString = new StringBuilder("");
        levelList = 0;
        lvlTab = new StringBuilder(" ");
        bt = new Date().getTime();
        subDirectoryOrFileSize = 0;
        processStatus = 0;
//        ListSettingsController.getDates();
        datesList = MainWindowController.getDates();
//        Arrays.fill(datesList, LocalDateTime.MIN);
//        datesList[0] = LocalDateTime.now();
//        datesList[1] = LocalDateTime.now().minusHours(1);
    }

    private void sortList() {
        if(LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("start");
        int startIndex = 0;
        int endIndex = resultList.size() - 1;
        doSort(startIndex, endIndex);
    }
    private void sortList(ArrayList currentList) {
        if(LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("start");
        int startIndex = 0;
        int endIndex = currentList.size() - 1;
        doSort(startIndex, endIndex, currentList);
    }

    private long calcElementSize(ResultFile[] currentList) {
        System.out.println("CALC");
        if(currentList.length == 0) {
            System.err.println("List is empty!");
            return 0;
        }
        long size = 0;
        for(ResultFile f : currentList) {
            size += f.getFinallySize();
        }
        return size;
    }
    private void doSort(int start, int end, ArrayList<ResultFile[]> currentList) {
        if(LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("do sort");
        if (start >= end)
            return;
        int i = start, j = end;
        int cur = i - (i - j) / 2;
        while (i < j) {
            System.out.println("+1");
            System.out.println(currentList.get(i));
            System.out.println(currentList.get(cur));
            System.out.println(calcElementSize(currentList.get(i)));
            System.out.println("-1");
            while (i < cur && (calcElementSize(currentList.get(i)) >= calcElementSize(currentList.get(cur)))) {
                i++;
                System.out.println(currentList.get(i));
                System.out.println(currentList.get(cur));
                System.out.println("12");
            }
            System.out.println("2");
            while (j > cur && (calcElementSize(currentList.get(cur)) >= calcElementSize(currentList.get(j)))) {
                j--;
                System.out.println("22");
            }
            System.out.println("3");
            if (i < j) {
                ResultFile[] rfTmp = currentList.get(i);
                currentList.add(i, currentList.get(j));
                currentList.remove(i + 1);
                currentList.add(j, rfTmp);
                currentList.remove(j + 1);
                if (i == cur)
                    cur = j;
                else if (j == cur)
                    cur = i;
            }
            System.out.println("3");
        }
        doSort(start, cur, currentList);
        doSort(cur + 1, end, currentList);
    }
    private void doSort(int start, int end) {
        if(LOGGER_SCANNER.isDebugEnabled())
            LOGGER_SCANNER.debug("do sort");
        if (start >= end)
            return;
        int i = start, j = end;
        int cur = i - (i - j) / 2;
        while (i < j) {
            System.out.println("+1");
            System.out.println("-1");
            while (i < cur && (resultList.get(i).getFinallySize() >= resultList.get(cur).getFinallySize())) {
                i++;
                System.out.println("12");
            }
            System.out.println("2");
            while (j > cur && (resultList.get(cur).getFinallySize() >= resultList.get(j).getFinallySize())) {
                j--;
                System.out.println("22");
            }
            System.out.println("3");
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
            System.out.println("3");
        }
        doSort(start, cur);
        doSort(cur + 1, end);
    }

    public ArrayList<ResultFile> getResultList() {
        return resultList;
    }


    public ArrayList<ResultFile[]> getResultDatesList() {
//        resultDatesList = new ArrayList<ResultFile[]>();
//        for (int i = 0; i < resultList.size(); i++) {
//            resultList.get(i).lastModified()
//        }
//        re
        return resultDatesList;
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

    //    private long t4 = new Date().getTime();
//    private long t3 = new Date().getTime();
//    private long t2 = new Date().getTime();
//    private long t4 = 0;
//    private long t3 = 0;
    public LocalDateTime[] getDatesList() {
        return datesList;
    }

    public static LocalDateTime getDateOf(double value) {
        value++;
//        int curTimeSpan = (int) (value / TIME_SPAN_AMOUNT);
        int curTimeSpan = (int) (value);
        LocalDateTime now = LocalDateTime.now();
        int years = 0;
        int months = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        switch (curTimeSpan) {
            case 0:
                return LocalDateTime.of(
                        LocalDate.MIN,
                        LocalTime.MIN
                );
            case 1:
                years = 5;
                months = 0;
                days = 0;
                hours = 0;
                minutes = 0;
                break;
            case 2:
                years = 3;
                months = 0;
                days = 0;
                hours = 0;
                minutes = 0;
                break;
            case 3:
                years = 2;
                months = 0;
                days = 0;
                hours = 0;
                minutes = 0;
                break;
            case 4:
                years = 1;
                months = 0;
                days = 0;
                hours = 0;
                minutes = 0;
                break;
            case 5:
                years = 0;
                months = 6;
                days = 0;
                hours = 0;
                minutes = 0;
                break;
            case 6:
                years = 0;
                months = 1;
                days = 0;
                hours = 0;
                minutes = 0;
                break;
            case 7:
                years = 0;
                months = 0;
                days = 7;
                hours = 0;
                minutes = 0;
                break;
            case 8:
                years = 0;
                months = 0;
                days = 1;
                hours = 0;
                minutes = 0;
                break;
            case 9:
                years = 0;
                months = 0;
                days = 0;
                hours = 1;
                minutes = 0;
                break;
            case 10:
                years = 0;
                months = 0;
                days = 0;
                hours = 0;
                minutes = 5;
                break;
        }
//        return LocalDateTime.of(
//                LocalDate.of(now.getYear() - years, now.getMonthValue() - months, now.getDayOfMonth() - days),
//                LocalTime.of(now.getHour() - hours, now.getMinute() - minutes)
//        );
//        LocalDateTime.now().minusMinutes(minutes).minusHours(hours).minusDays(days).minusMonths(months).minusYears(years);
//        LocalDateTime.now().minusHours(hours);
//        LocalDateTime.now().minusDays(days);
//        LocalDateTime.now().minusMonths(months).minusYears(years);
//        LocalDateTime.now().minusYears(years);
        return LocalDateTime.now().minusMinutes(minutes).minusHours(hours).minusDays(days).minusMonths(months).minusYears(years);
    }

    public void setDatesList(LocalDateTime[] datesList) {
        this.datesList = datesList;
    }
}
