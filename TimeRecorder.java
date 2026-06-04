/**
 * 出退勤を管理するクラスです。
 *
 * <p>コマンドライン引数で出勤または退勤を指定して実行します。</p>
 *
 * <h2>利用方法</h2>
 * <ul>
 *   <li>コマンドライン引数に "1" を指定した場合は出勤として処理します。</li>
 *   <li>コマンドライン引数に "0" を指定した場合は退勤として処理します。</li>
 * </ul>
 *
 * <h2>実行結果</h2>
 * <p>実行時の現在日時を取得し、標準出力に以下の形式で出力して終了します。</p>
 *
 * <pre>
 * 出勤 yyyy/MM/dd HH:mm:ss
 * 退勤 yyyy/MM/dd HH:mm:ss
 * </pre>
 *
 * <p>
 * 出勤時は「出勤」、退勤時は「退勤」を出力し、
 * その後に1文字の空白を挟んで現在日時を出力します。
 * </p>
 */
public class TimeRecorder {
    public static final String STATUS_WORK = "1";
    public static final String STATUS_OFF = "0";
    public static final String LABEL_WORK = "出勤";
    public static final String LABEL_OFF = "退勤";
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("コマンドライン引数を1つ指定してください。");
            return;
        }

        String action = args[0];
        String statusLabel;

        if (action.equals(STATUS_WORK)) {
            statusLabel = LABEL_WORK;
        } else if (action.equals(STATUS_OFF)) {
            statusLabel = LABEL_OFF;
        } else {
            System.out.println("コマンドライン引数に '" + STATUS_WORK + "' または '" + STATUS_OFF + "' を指定してください。");
            return;
        }

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        System.out.println(statusLabel + " " + formattedDateTime);
    }
}
 