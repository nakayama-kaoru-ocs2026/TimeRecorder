/**
 * このクラスは、ログファイルにメッセージを出力するためのユーティリティクラスです。
 * ログレベルは INFO、ERROR、CRITICAL の3種類が定義されており、
 * ログファイルには日時とともにログレベルとメッセージが記録されます。
 * ログファイルは "timerecorder.log" という名前で、UTF-8 形式で保存されます。
 * ファイルが存在しない場合は新規作成され、存在する場合は末尾に追記されます。
 * ログ出力の際にファイルへの書き込みに失敗した場合は、
 * 標準エラー出力にエラーメッセージを表示し、スタックトレースを出力します。
 * ログ出力の形式は以下の通りです。
 * <pre>
 * yyyy/MM/dd HH:mm:ss [LOG_LEVEL] メッセージ
 * </pre>
 * 例:
 * <pre>
 * 2024/06/01 12:00:00 [INFO] コマンドライン引数: 1
 * 2024/06/01 12:00:00 [INFO] 出勤
 * 2024/06/01 12:00:01 [ERROR] ログファイルへの出力に失敗しました。
 * java.io.IOException: ファイルが見つかりません
 *    at java.base/java.io.FileWriter.<init>(FileWriter.java:123)
 *   at java.base/java.io.FileWriter.<init>(FileWriter.java:94)
 *  at Logger.println(Logger.java:20)
 * </pre>
 * このクラスは、TimeRecorder クラスから呼び出され、出退勤の記録やエラーメッセージのログ出力に使用されます。
 * ログレベルに応じて、INFO レベルは通常の操作ログ、ERROR レベルはエラー情報、CRITICAL レベルは例外情報を記録するために使用されます。
 * ログファイルへの出力に失敗した場合は、標準エラー出力にエラーメッセージを表示し、スタックトレースを出力します。
 * このクラスは、出退勤記録の操作ログを管理するためのものであり、TimeRecorder クラスの正常な動作をサポートします。
 * <h2>利用方法</h2>
 * <pre>ｌ
 * Logger.println(Logger.loglevel.INFO, "コマンドライン引数: " + args[0]);
 * Logger.println(Logger.loglevel.INFO, "出勤");   
 * Logger.println(Logger.loglevel.ERROR, "ログファイルへの出力に失敗しました。");
 * Logger.println(Logger.loglevel.CRITICAL, "例外メッセージ");
 * </pre>
 * このクラスは、TimeRecorder クラスの正常な動作をサポートするためのものであり、出退勤記録の操作ログを管理します。
 * ログレベルに応じて、INFO レベルは通常の操作ログ、ERROR レベルはエラー情報、CRITICAL レベルは例外情報を記録するために使用されます。
 * ログファイルへの出力に失敗した場合は、標準エラー出力にエラーメッセージを表示し、スタックトレースを出力します。
 * <h2>ログファイル</h2>
 * <ul>
 *   <li>ファイル名: {@code timerecorder.log}</li> 
 *  <li>文字コード: UTF-8</li>
 *  <li>ファイルが存在しない場合は新規作成します。</li>
 * <li>ファイルが存在する場合は末尾へ追記します。</li>
 * </ul>
 * <h2>ログ出力形式</h2>
 * <pre>
 * yyyy/MM/dd HH:mm:ss [LOG_LEVEL] メッセージ
 */
public class Logger {
    public static final String LOG_FILE_NAME = "timerecorder.log";
    /**
     * ログレベルを表す列挙型です。
     */
    public static enum loglevel {
        INFO, 
        ERROR, 
        CRITICAL
    }
    /**
     * 指定されたログレベルとメッセージをログファイルに出力します。
     * @param level:  ログレベル (INFO, ERROR, CRITICAL)
     * @param message:  ログメッセージ
     */
    public static void println(loglevel level, String message) {
        try (java.io.PrintWriter logWriter = new java.io.PrintWriter(new java.io.FileWriter(LOG_FILE_NAME, true))) {
                logWriter.println(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + " [" + level + "] " + message);
        } catch (java.io.IOException e) {
                System.err.println("ログファイルへの出力に失敗しました。");
                e.printStackTrace();
        }
    }
}
