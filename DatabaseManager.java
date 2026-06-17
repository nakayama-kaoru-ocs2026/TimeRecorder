import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * SQLite データベースへのアクセスを管理するクラスです。
 * <p>
 * 以下の 2 つのテーブルを管理します。
 * </p>
 * <ul>
 * <li>社員マスタ ({@code M_member}): 社員番号と社員名を保持します。</li>
 * <li>出退勤テーブル ({@code T_time_record}): 出退勤区分・日時を社員番号に紐付けて保持します。</li>
 * </ul>
 *
 * <p>
 * インスタンス生成時にデータベースへ接続し、テーブルが存在しない場合は自動的に作成します。
 * 使用後は {@link #close()} を呼び出してリソースを解放してください。
 * {@link AutoCloseable} を実装しているため、try-with-resources 構文でも使用できます。
 * </p>
 */
public class DatabaseManager implements AutoCloseable {
    private static final String DB_URL = "jdbc:sqlite:time_record.db"; // データベースファイル名
    private final Connection con; // データベース接続オブジェクト

    /**
     * コンストラクタ
     * データベースへ接続し、必要ならテーブルを新規作成します。
     *
     * @throws SQLException データベース接続またはテーブル作成に失敗した場合
     */
    public DatabaseManager() throws SQLException {
        con = DriverManager.getConnection(DB_URL);
        createTables();
    }

    /**
     * データベース接続をクローズします。
     * try-with-resources 構文で自動的に呼び出されます。
     *
     * @throws SQLException クローズに失敗した場合
     */
    @Override
    public void close() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }

    /**
     * テーブルが存在しない場合に {@code M_member} テーブルおよび
     * {@code T_time_record} テーブルを作成します。
     *
     * @throws SQLException テーブル作成に失敗した場合
     */
    private void createTables() throws SQLException {
        try (Statement st = con.createStatement()) {
            // 社員マスタ
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS M_member ("
                            + "  no   TEXT NOT NULL PRIMARY KEY,"
                            + "  name TEXT NOT NULL"
                            + ")");
            // 出退勤テーブル
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS T_time_record ("
                            + "  no       TEXT    NOT NULL,"
                            + "  io_kbn   TEXT    NOT NULL,"
                            + "  datetime DATETIME NOT NULL"
                            + ")");
        }
    }

    /**
     * 社員番号に対応する社員名を取得します。
     *
     * @param no: 検索する社員番号
     * @return 社員名（該当しない場合は {@code null}）
     * @throws SQLException SELECT に失敗した場合
     */
    public String getMemberName(String no) throws SQLException {
        final String sql = "SELECT name FROM M_member WHERE no = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, no);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }
        return null;
    }

    /**
     * 出退勤テーブルに新しいレコードを追加します。
     *
     * @param no:       社員番号
     * @param ioKbn:    出退勤区分 ("1"=出勤, "0"=退勤)
     * @param datetime: 出退勤日時
     * @throws SQLException INSERT に失敗した場合
     */
    public void insertTimeRecord(String no, String ioKbn, LocalDateTime datetime) throws SQLException {
        final String sql = "INSERT INTO T_time_record (no, io_kbn, datetime) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, no);
            ps.setString(2, ioKbn);
            ps.setTimestamp(3, Timestamp.valueOf(datetime));
            ps.executeUpdate();
        }
    }
}
