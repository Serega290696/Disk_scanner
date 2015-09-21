package myProject.model;

import java.io.File;
import java.util.GregorianCalendar;

/**
 * Created by serega on 19.09.2015.
 */
public class ReportGenerator {


    public static final int MAX_REPORT_SIZE = 5_000_000;
    private FileWorker fileWorker = new FileWorker();

    private File reportFolder = (File) SettingsConstants.SETTINGS.getDEFAULT_REPORTS_FOLDER_2();

    private static int reportNumber = 0;

    public String getSeparator() {
        return separator;
    }

    private String separator = "\r\n";
    private DataWorker dataWorker = new DataWorker();

    private String HEADER_PATTERN = "===========================================================" + separator +
            "================= Disk scanner saveReport =================" + separator +
            "===========================================================" + separator +
            "Analyzed directory: %s" + separator +
            "Directory size: %s" + separator +
            "Total time: %5f seconds" + separator +
            "Date: %4$tb %4$te, %4$tY  %4$TH:%4$TM.%4$TS" + separator +
            "" + separator +
            "Pattern: " + separator +
            "\t\t-Directory name (directory size)" + separator +
            "\t\t\t-Subdirectory name (size)" + separator +
            "\t\t\t-file name.file_extension (size)" + separator +
            "\t\t-Next directory name (directory size)" + separator +
            "\t\t\t-file name.file_extension (size)" + separator + separator +
            "===========================================================" + separator + separator;
    private String FOOTER_PATTERN = "=============================================================" + separator +
            "%s" + separator +
            "Report created: %2$tb %2$te, %2$tY  %2$TH:%2$TM.%2$TS";
    private boolean autoCreated;

    public void saveReport(DiskAnalyzer diskAnalyzer, boolean auto) {
        autoCreated = auto;
//        System.out.println(autoGenerateReportName());
        fileWorker.write(autoGenerateReportName(), createReport(diskAnalyzer));
//        fileWorker.write(autoGenerateReportName(), createReport(diskAnalyzer));
    }


    private String autoGenerateReportName() {
        return autoGenerateReportName(reportFolder.getAbsolutePath());
    }

    public String autoGenerateReportName(String filePath) {
        String s;
        if (new File(s = (filePath + ".txt")).exists() && autoCreated)
            while (new File(s = (filePath + (reportNumber) + ".txt")).exists()) {
                reportNumber++;
            }
        return s;
    }

    private String createReport(DiskAnalyzer diskAnalyzer) {
        String report = diskAnalyzer.getReportString().toString();
//        reportString.append((chosenFile.isFile() ? "File" : "Directory") + " size: " + dataWorker.convert(chosenFileSize));
//        reportString.append("Time: " + (new Date().getTime() - bt));
        Object[] headerObjects = {
                diskAnalyzer.getChosenFile(),
                dataWorker.convert(diskAnalyzer.getChosenFileSize()),
                (float)diskAnalyzer.getTotalTime()/1000f,
                diskAnalyzer.getDateAnalysis(),
        };
        Object[] footerObjects = {
                autoCreated(),
                new GregorianCalendar()
        };
        String header = String.format(HEADER_PATTERN, headerObjects);
        String footer = String.format(FOOTER_PATTERN, footerObjects);
        if(report.length() > MAX_REPORT_SIZE)
            report.substring(0, MAX_REPORT_SIZE);
        report = header + "\n" + report + "\n" + footer;

        return report;
    }

    private String autoCreated() {
        if (autoCreated)
            return "This report saved automatically.";
        return "You created this report.";
    }

    public void setReportFolder(File reportFolder) {
        this.reportFolder = reportFolder;
    }
}
