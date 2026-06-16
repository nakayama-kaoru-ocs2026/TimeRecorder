/**
 * このクラスは、ログファイルにメッセージを出力するためのユーティリティクラスです。
 * ログレベルは INFO、ERROR、CRITICAL の3種類があり、ログファイルには日時、ログレベル、メッセージが記録されます。
 * ログファイルは UTF-8 でエンコードされ、存在しない場合は新規作成され、存在する場合は末尾に追記されます。
 * ログファイルの名前は "timerecorder.log" です。
 */
public class Logger {
    public static final String LOG_FILE_NAME = "timerecorder.log";  // ログファイル名
    /**
     * ログレベルを表す列挙型です。
     */
    public static enum LogLevel {
        INFO,
        ERROR,
        CRITICAL
    }
    /**
     * 指定されたログレベルとメッセージをログファイルに書き込みます。
     * @param level: ログレベル
     * @param message: ログメッセージ
     */
    public static void println(LogLevel level, String message) {
        try (java.io.PrintWriter logWriter = new java.io.PrintWriter(new java.io.FileWriter(LOG_FILE_NAME, true))) {
            logWriter.println(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + " [" + level + "] " + message);
        } catch (java.io.IOException e) {
            System.err.println("ログファイルへの出力に失敗しました。");
            e.printStackTrace();
        }
    }
}
