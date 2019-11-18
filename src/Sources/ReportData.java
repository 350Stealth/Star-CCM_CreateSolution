package Sources;

public class ReportData {
    public static ReportData instance;
    
    static {
        instance = new ReportData();
    }
    
    private ReportData() {
    
    }
    
    public static ReportData getInstance() {
        return instance;
    }
}
