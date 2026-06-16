/**
 * 出退勤時刻を記録するクラスです。
 * <p>
 * コマンドライン引数で出勤または退勤を判別し、
 * 実行時の現在日時を標準出力および記録ファイルへ出力します。
 * また、処理内容やエラー情報を操作ログへ記録します。
 * </p>
 *
 * <h2>利用方法</h2>
 * 
 * <pre>
 * java TimeRecorder 1
 * </pre>
 * 
 * 出勤として現在日時を出力します。
 *
 * <pre>
 * java TimeRecorder 0
 * </pre>
 * 
 * 退勤として現在日時を出力します。
 *
 * <h2>出力形式</h2>
 * 
 * <pre>
 * 出勤 yyyy/MM/dd HH:mm:ss
 * 退勤 yyyy/MM/dd HH:mm:ss
 * </pre>
 *
 * <ul>
 * <li>コマンドライン引数が {@code 1} の場合は「出勤」を出力します。</li>
 * <li>コマンドライン引数が {@code 0} の場合は「退勤」を出力します。</li>
 * <li>「出勤」または「退勤」の後には半角スペースを1文字出力します。</li>
 * <li>日時は実行時点の現在日時を {@code yyyy/MM/dd HH:mm:ss} 形式で出力します。</li>
 * </ul>
 *
 * <h2>出退勤記録ファイル</h2>
 * <ul>
 * <li>ファイル名: {@code time_record.dat}</li>
 * <li>文字コード: UTF-8</li>
 * <li>ファイルが存在しない場合は新規作成します。</li>
 * <li>ファイルが存在する場合は末尾へ追記します。</li>
 * <li>標準出力と同一内容を記録します。</li>
 * </ul>
 *
 * <h2>ログファイル</h2>
 * <ul>
 * <li>ファイル名: {@code timerecorder.log}</li>
 * <li>文字コード: UTF-8</li>
 * <li>ファイルが存在しない場合は新規作成します。</li>
 * <li>ファイルが存在する場合は末尾へ追記します。</li>
 * </ul>
 *
 * <h2>ログ出力形式</h2>
 * 
 * <pre>
 * yyyy/MM/dd HH:mm:ss [INFO] コマンドライン引数
 * yyyy/MM/dd HH:mm:ss [INFO] 出退勤の出力結果
 * yyyy/MM/dd HH:mm:ss [ERROR] エラーメッセージ
 * yyyy/MM/dd HH:mm:ss [CRITICAL] 例外メッセージ
 * </pre>
 *
 * <ul>
 * <li>コマンドライン引数の受領内容を INFO レベルで記録します。</li>
 * <li>出退勤の出力結果を INFO レベルで記録します。</li>
 * <li>入力値不正などのエラーを ERROR レベルで記録します。</li>
 * <li>例外発生時は例外メッセージを CRITICAL レベルで記録します。</li>
 * </ul>
 */
public class TimeRecorder {
    public static final String STATUS_WORK = "1"; // 出勤を表す定数
    public static final String STATUS_OFF = "0"; // 退勤を表す定数

    /**
     * プログラムのエントリーポイントです。
     * 
     * @param args: コマンドライン引数。出勤は "1"、退勤は "0" を指定します。
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("コマンドライン引数の数が不正です。");
            // ログへの記録
            Logger.println(Logger.LogLevel.ERROR, "コマンドライン引数の数が不正です。");
            return;
        }

        String status;
        if (args[1].equals(STATUS_WORK)) {
            status = "出勤";
        } else if (args[1].equals(STATUS_OFF)) {
            status = "退勤";
        } else {
            System.out.println("コマンドライン引数が不正です。");
            // ログへの記録
            Logger.println(Logger.LogLevel.ERROR, "コマンドライン引数が不正です。");
            return;
        }
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        System.out.println(status + " " + timestamp + " " + args[0]);
        // サーバーへ送信
        try (java.net.Socket socket = new java.net.Socket("localhost", TimeRecordServer.PORT);
                java.io.PrintWriter writer = new java.io.PrintWriter(socket.getOutputStream(), true)) {
            writer.println(args[0] + "," + args[1]);
        } catch (java.io.IOException e) {
            System.err.println("サーバーへの接続に失敗しました。");
            e.printStackTrace();
            // ログへの記録
            Logger.println(Logger.LogLevel.CRITICAL, "サーバーへの接続に失敗しました。例外: " + e.getMessage());
        }
        // ログへの記録
        Logger.println(Logger.LogLevel.INFO, status + " " + timestamp + " " + args[0]);
    }
}