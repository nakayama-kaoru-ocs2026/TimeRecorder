/**
 * 出退勤を管理するクラスです。
 *
 * <p>
 * コマンドライン引数で出勤または退勤を指定し、
 * 実行時の現在日時を標準出力およびファイルへ出力します。
 * </p>
 *
 * <h2>利用方法</h2>
 * <ul>
 *   <li>コマンドライン引数に "1" を指定した場合は出勤として処理します。</li>
 *   <li>コマンドライン引数に "0" を指定した場合は退勤として処理します。</li>
 * </ul>
 *
 * <h2>出力形式</h2>
 * <pre>
 * 出勤 yyyy/MM/dd HH:mm:ss
 * 退勤 yyyy/MM/dd HH:mm:ss
 * </pre>
 *
 * <p>
 * 出勤時は「出勤」、退勤時は「退勤」を出力し、
 * その後に1文字の空白を挟んで現在日時を出力します。
 * </p>
 *
 * <h2>追加機能</h2>
 * <p>
 * 出退勤情報をファイルへ記録します。
 * </p>
 * <ul>
 *   <li>ファイル名：time_record.dat</li>
 *   <li>文字コード：UTF-8</li>
 *   <li>ファイルが存在する場合は末尾へ追記します。</li>
 *   <li>ファイルが存在しない場合は新規作成します。</li>
 *   <li>標準出力と同じ形式の内容をファイルへ出力します。</li>
 * </ul>
 *
 * @author S2SA11 中山薫
 */
public class TimeRecorder {
    public static final String work_in = "1";
    public static final String work_out = "0";
    public static final String file_name = "time_record.dat";
    public static void main(String[] args) {
        // コマンドライン引数の処理
        if (args.length != 1) {
            System.out.println("使用方法: java TimeRecorder <" + work_in + "|" + work_out + ">");
            System.out.println(work_in + ": 出勤, " + work_out + ": 退勤");
            return;
        }

        String command = args[0];
        String status;
        if (work_in.equals(command)) {
            status = "出勤";
        } else if (work_out.equals(command)) {
            status = "退勤";
        } else {
            System.out.println("無効な引数です。使用方法: java TimeRecorder <" + work_in + "|" + work_out + ">");
            return;
        }

        // 現在日時の取得とフォーマット
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        // 出力内容の作成
        String output = status + " " + formattedDateTime;

        // 標準出力への出力
        System.out.println(output);

        // ファイルへの出力
        try (java.io.FileWriter writer = new java.io.FileWriter("time_record.dat", true)) {
            writer.write(output + System.lineSeparator());
        } catch (java.io.IOException e) {
            System.err.println("ファイルへの書き込みに失敗しました: " + e.getMessage());
        }
    }
}