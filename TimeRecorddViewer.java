import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * 出退勤情報をSQLLiteデータベースから取得し、標準出力に表示するプログラムです。
 */
public class TimeRecorddViewer {
    private static final String DB_URL = "jdbc:sqlite:time_record.db"; // SQLite データベースの URL

    /**
     * プログラムのエントリーポイントです。
     * 
     * @param args: コマンドライン引数は使用しません。
     */
    public static void main(String[] args) {
        final String sql = "SELECT m.no, m.name, t.datetime, t.io_kbn "
            + "FROM T_time_record t "
            + "INNER JOIN M_member m ON t.no = m.no "
            + "ORDER BY m.no, t.datetime";

        try (Connection con = DriverManager.getConnection(DB_URL);
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String no = rs.getString("no");
                String name = rs.getString("name");
                Timestamp dateTime = rs.getTimestamp("datetime");
                String ioKbn = rs.getString("io_kbn");
                
                String dt = dateTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
                String status = "";
                if (TimeRecorder.STATUS_WORK.equals(ioKbn)) {
                    status = "出勤";
                } else {
                    status = "退勤";
                }

                System.out.println(no + " " + name + " " + dt + " " + status);
            }

        } catch (SQLException e) {
            System.err.println("データベースへのアクセスに失敗しました。");
            e.printStackTrace();
            // ログへの記録
            Logger.println(Logger.LogLevel.CRITICAL, "データベースの読み込みに失敗しました。");
        }
    }
}
