/**
 * 出退勤情報を受信して記録するサーバーアプリケーションです。
 * <p>
 * TCP/IP 通信によってクライアントアプリケーションから
 * 出勤または退勤の情報を受信します。
 * 通信には {@link java.net.ServerSocket} および
 * {@link java.net.Socket} を使用します。
 * </p>
 *
 * <p>
 * サーバーは起動後、受信待ち状態となります。
 * クライアントから出退勤情報を受信すると、
 * サーバー側の現在日時を用いて出退勤情報を生成し、
 * 標準出力および出退勤記録ファイルへ出力します。
 * 処理終了後は再び受信待ち状態へ戻ります。
 * </p>
 *
 * <h2>動作概要</h2>
 * <ol>
 * <li>起動後、受信待ち状態になります。</li>
 * <li>クライアントから「出勤」または「退勤」を受信します。</li>
 * <li>受信時点の現在日時を取得します。</li>
 * <li>出退勤情報を標準出力へ出力します。</li>
 * <li>同じ内容を出退勤記録ファイルへ追記します。</li>
 * <li>再び受信待ち状態へ戻ります。</li>
 * </ol>
 *
 * <h2>出力形式</h2>
 * 
 * <pre>
 * 出勤 yyyy/MM/dd HH:mm:ss
 * 退勤 yyyy/MM/dd HH:mm:ss
 * </pre>
 *
 * <ul>
 * <li>「出勤」または「退勤」の後には半角スペースを1文字出力します。</li>
 * <li>日時は受信時点の現在日時を {@code yyyy/MM/dd HH:mm:ss} 形式で出力します。</li>
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
 */
public class TimeRecordServer {
    public static final int PORT = 12345; // サーバーが待ち受けるポート番号
    public static final String FILE_NAME = "time_record.dat"; // 出退勤記録ファイル名

    /**
     * プログラムのエントリーポイントです。
     * 
     * @param args: コマンドライン引数は使用しません。
     */
    public static void main(String[] args) {
        try (java.net.ServerSocket serverSocket = new java.net.ServerSocket(PORT)) {
            System.out.println("サーバーがポート " + PORT + " で起動しました。");
            // ログへの記録
            Logger.println(Logger.LogLevel.INFO, "[Server]サーバーがポート " + PORT + " で起動しました。");
            while (true) {
                try (java.net.Socket clientSocket = serverSocket.accept();
                        java.io.BufferedReader reader = new java.io.BufferedReader(
                                new java.io.InputStreamReader(clientSocket.getInputStream()));
                        java.io.PrintWriter writer = new java.io.PrintWriter(
                                new java.io.FileWriter(FILE_NAME, true))) {
                    String line = reader.readLine();
                    if (line == null) {
                        continue;
                    }
                    String[] parts = line.split(",", 2);
                    String status;
                    if (parts[1].equals(TimeRecorder.STATUS_WORK)) {
                        status = "出勤";
                    } else if (parts[1].equals(TimeRecorder.STATUS_OFF)) {
                        status = "退勤";
                    } else {
                        System.out.println("不正な入力を受信しました: " + parts[1]);
                        // ログへの記録
                        Logger.println(Logger.LogLevel.ERROR, "[Server]不正な入力を受信しました: " );
                        continue;
                    }
                    String output = status + " "
                            + java.time.LocalDateTime.now().format(
                                    java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))+ " " + parts[0];
                    System.out.println(output);
                    // 出退勤記録ファイルへ出力(追記)
                    writer.println(output);
                    // ログへの記録
                    Logger.println(Logger.LogLevel.INFO, "[Server]" + output);
                } catch (java.io.IOException e) {
                    System.err.println("クライアントとの通信に失敗しました。");
                    e.printStackTrace();
                    // ログへの記録
                    Logger.println(Logger.LogLevel.CRITICAL, "[Server]サーバーへの接続に失敗しました。例外: " + e.getMessage());
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("サーバーの起動に失敗しました。");
            e.printStackTrace();
            // ログへの記録
            Logger.println(Logger.LogLevel.CRITICAL, "[Server]サーバーの起動に失敗しました。例外: " + e.getMessage());
        }
    }
}
