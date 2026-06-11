/**
 * 出退勤情報を受信して記録するサーバーアプリケーションです。
 * <p>
 * TCP/IP通信（java.net.ServerSocket）を利用して
 * クライアントから送信された出勤または退勤情報を受信します。
 * 受信時にはサーバー側の現在日時を取得し、
 * 出退勤情報を標準出力および出退勤記録ファイルへ出力します。
 * 処理完了後は再び受信待ち状態へ戻ります。
 * </p>
 *
 * <h2>起動方法</h2>
 * <pre>
 * java TimeRecordServer
 * </pre>
 *
 * <h2>動作概要</h2>
 * <ol>
 *   <li>指定ポートで受信待ち状態になります。</li>
 *   <li>クライアントから出退勤情報を受信します。</li>
 *   <li>サーバー側の現在日時を取得します。</li>
 *   <li>出退勤情報を標準出力へ出力します。</li>
 *   <li>出退勤情報を出退勤記録ファイルへ追記します。</li>
 *   <li>再び受信待ち状態へ戻ります。</li>
 * </ol>
 *
 * <h2>受信データ</h2>
 * <ul>
 *   <li>「出勤」または「退勤」を表す状態情報を受信します。</li>
 *   <li>日時情報はクライアントから受信しません。</li>
 *   <li>記録日時はサーバー側で生成します。</li>
 * </ul>
 *
 * <h2>出力形式</h2>
 * <pre>
 * 出勤 yyyy/MM/dd HH:mm:ss
 * 退勤 yyyy/MM/dd HH:mm:ss
 * </pre>
 *
 * <ul>
 *   <li>受信した状態情報を出力します。</li>
 *   <li>日時はサーバー受信時点の現在日時を
 *       {@code yyyy/MM/dd HH:mm:ss} 形式で出力します。</li>
 * </ul>
 *
 * <h2>出退勤記録ファイル</h2>
 * <ul>
 *   <li>ファイル名: {@code time_record.dat}</li>
 *   <li>文字コード: UTF-8</li>
 *   <li>ファイルが存在しない場合は新規作成します。</li>
 *   <li>ファイルが存在する場合は末尾へ追記します。</li>
 *   <li>標準出力と同一内容を記録します。</li>
 * </ul>
 *
 * <h2>ログファイル</h2>
 * <ul>
 *   <li>ファイル名: {@code timerecordserver.log}</li>
 *   <li>文字コード: UTF-8</li>
 *   <li>ファイルが存在しない場合は新規作成します。</li>
 *   <li>ファイルが存在する場合は末尾へ追記します。</li>
 * </ul>
 *
 * <h2>ログ出力形式</h2>
 * <pre>
 * yyyy/MM/dd HH:mm:ss [INFO] サーバー起動
 * yyyy/MM/dd HH:mm:ss [INFO] クライアント接続
 * yyyy/MM/dd HH:mm:ss [INFO] 出退勤情報受信
 * yyyy/MM/dd HH:mm:ss [ERROR] エラーメッセージ
 * yyyy/MM/dd HH:mm:ss [CRITICAL] 例外メッセージ
 * </pre>
 */
public class TimeRecordServer {
    public static final int PORT = 12345;
    public static final String TIME_RECORD_FILE = "time_record.dat";

    /**
     * サーバーアプリケーションのエントリーポイントです。
     * 
     * @param args コマンドライン引数（使用しません）
     */
    public static void main(String[] args) {
        try(java.net.ServerSocket serverSocket = new java.net.ServerSocket(PORT)) {
            System.out.println("サーバーが起動しました。ポート: " + PORT);
            while (true) {
                try (java.net.Socket clientSocket = serverSocket.accept()) {
                    System.out.println("クライアントが接続しました: " + clientSocket.getInetAddress());
                    java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(clientSocket.getInputStream()));
                    String status = in.readLine();
                    if (status != null) {
                        String timestamp = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date());
                        String record = (status.equals("1") ? "出勤 " : "退勤 ") + timestamp;
                        System.out.println(record);
                        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(TIME_RECORD_FILE, true))) {
                            writer.write(record);
                            writer.newLine();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("クライアント処理中にエラーが発生しました: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("サーバー起動中にエラーが発生しました: " + e.getMessage());
        }

    }
}