/**
 * 出退勤情報をサーバーへ送信するクライアントアプリケーションです。
 * <p>
 * コマンドライン引数で出勤または退勤を判別し、
 * 実行時の現在日時を標準出力へ出力します。
 * また、出勤または退勤の情報を TCP/IP 通信により
 * サーバーへ送信します。
 * </p>
 *
 * <p>
 * 出退勤記録ファイルへの保存は行いません。
 * 出退勤時刻の記録およびファイル出力はサーバー側で実施します。
 * </p>
 *
 * <h2>利用方法</h2>
 *
 * <pre>
 * java TimeRecorder 1
 * </pre>
 *
 * 出勤情報を標準出力へ出力し、サーバーへ送信します。
 *
 * <pre>
 * java TimeRecorder 0
 * </pre>
 *
 * 退勤情報を標準出力へ出力し、サーバーへ送信します。
 *
 * <h2>出力形式</h2>
 *
 * <pre>
 * 出勤 yyyy/MM/dd HH:mm:ss
 * 退勤 yyyy/MM/dd HH:mm:ss
 * </pre>
 *
 * <ul>
 *   <li>コマンドライン引数が {@code 1} の場合は「出勤」を出力します。</li>
 *   <li>コマンドライン引数が {@code 0} の場合は「退勤」を出力します。</li>
 *   <li>「出勤」または「退勤」の後には半角スペースを1文字出力します。</li>
 *   <li>日時はクライアント実行時点の現在日時を
 *       {@code yyyy/MM/dd HH:mm:ss} 形式で出力します。</li>
 * </ul>
 *
 * <h2>サーバー送信データ</h2>
 *
 * <ul>
 *   <li>TCP/IP 通信を使用してサーバーへ送信します。</li>
 *   <li>送信する内容は「出勤」または「退勤」を表す情報のみです。</li>
 *   <li>日時情報は送信しません。</li>
 *   <li>出退勤時刻はサーバー側の時計を使用して記録されます。</li>
 * </ul>
 *
 * <h2>ログファイル</h2>
 *
 * <ul>
 *   <li>ファイル名: {@code timerecorder.log}</li>
 *   <li>文字コード: UTF-8</li>
 *   <li>ファイルが存在しない場合は新規作成します。</li>
 *   <li>ファイルが存在する場合は末尾へ追記します。</li>
 * </ul>
 *
 * <h2>ログ出力形式</h2>
 *
 * <pre>
 * yyyy/MM/dd HH:mm:ss [INFO] コマンドライン引数
 * yyyy/MM/dd HH:mm:ss [INFO] サーバー送信結果
 * yyyy/MM/dd HH:mm:ss [ERROR] エラーメッセージ
 * yyyy/MM/dd HH:mm:ss [CRITICAL] 例外メッセージ
 * </pre>
 *
 * <ul>
 *   <li>コマンドライン引数の受領内容を INFO レベルで記録します。</li>
 *   <li>サーバーへの送信結果を INFO レベルで記録します。</li>
 *   <li>入力値不正などのエラーを ERROR レベルで記録します。</li>
 *   <li>例外発生時は例外メッセージを CRITICAL レベルで記録します。</li>
 * </ul>
 */
public class TimeRecorder {
    public static final String STATUS_WORK = "1";
    public static final String STATUS_OFF_WORK = "0";

    /**
     * プログラムのエントリーポイントです。
     * 
     * @param args コマンドライン引数。出勤は"1"、退勤は"0"を指定します。
     */
    public static void main(String[] args) {
        // コマンドライン引数の処理とサーバーへの送信処理を実装します。
        if (args.length != 2) {
            System.err.println("コマンドライン引数が不正です。出勤は\"1\"、退勤は\"0\"を指定してください。");
            Logger.println(Logger.loglevel.ERROR, "コマンドライン引数が不正です。");
            return;
        }
       
        String status;
        if(args[1].equals(STATUS_WORK)) {
            status = "出勤";
        } else if(args[].equals(STATUS_OFF_WORK)) {
            status = "退勤";
        } else {
            System.err.println("コマンドライン引数が不正です。出勤は\"1\"、退勤は\"0\"を指定してください。");
            Logger.println(Logger.loglevel.ERROR, "コマンドライン引数が不正です。");
            return;
        }
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        System.out.println(status + " " + timestamp);

        // サーバーへの送信処理を実装します。
        try (java.net.Socket socket = new java.net.Socket("localhost", 12345);
             java.io.PrintWriter out = new java.io.PrintWriter(socket.getOutputStream(), true)) {
            out.println(status);
            Logger.println(Logger.loglevel.INFO, "サーバーへの送信成功: " + status);
        } catch (java.io.IOException e) {
            System.err.println("サーバーへの送信に失敗しました。");
            //ログへの記録
            Logger.println(Logger.loglevel.CRITICAL, "サーバーへの送信に失敗: " + e.getMessage());
        }
        //ログへの記録
        Logger.println(Logger.loglevel.INFO, "コマンドライン引数: " + args[0]);
    }
}