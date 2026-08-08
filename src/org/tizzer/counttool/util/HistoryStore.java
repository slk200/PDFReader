package org.tizzer.counttool.util;

import org.tizzer.counttool.bean.ConvertRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 转换历史记录的SQLite存储
 *
 * <p>数据库文件保存在当前工作目录下的convert-history.db，
 * 与setting.dat保持同一存放约定。</p>
 */
public class HistoryStore {

    private static final String DB_URL = "jdbc:sqlite:convert-history.db";
    private static Connection connection;

    private HistoryStore() {
    }

    /**
     * 记录一条转换历史
     *
     * @param time     转换时间（yyyy-MM-dd HH:mm:ss）
     * @param fileName 文件名（含后缀）
     * @param filePath 文件路径
     * @param status   转换状态（成功/失败）
     */
    public static synchronized void insert(String time, String fileName, String filePath, String status) {
        try {
            ensureConnection();
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO convert_history(time, file_name, file_path, status) VALUES (?,?,?,?)")) {
                statement.setString(1, time);
                statement.setString(2, fileName);
                statement.setString(3, filePath);
                statement.setString(4, status);
                statement.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println("写入转换历史失败：" + e.getMessage());
        }
    }

    /**
     * 查询历史记录，按时间倒序
     *
     * @param keyword 关键词，匹配文件名或路径，传空表示不限
     * @param status  状态筛选，传空或"全部"表示不限
     * @return 记录列表
     */
    public static synchronized List<ConvertRecord> query(String keyword, String status) {
        return query(keyword, status, null, null);
    }

    /**
     * 查询历史记录，按时间倒序
     *
     * @param keyword  关键词，匹配文件名或路径，传空表示不限
     * @param status   状态筛选，传空或"全部"表示不限
     * @param dateFrom 起始日期（yyyy-MM-dd），传空表示不限
     * @param dateTo   截止日期（yyyy-MM-dd），传空表示不限
     * @return 记录列表
     */
    public static synchronized List<ConvertRecord> query(String keyword, String status,
                                                         String dateFrom, String dateTo) {
        List<ConvertRecord> records = new ArrayList<>();
        try {
            ensureConnection();
            StringBuilder sql = new StringBuilder(
                    "SELECT time, file_name, file_path, status FROM convert_history WHERE 1=1");
            boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
            boolean hasStatus = status != null && !status.trim().isEmpty() && !"全部".equals(status.trim());
            boolean hasFrom = dateFrom != null && !dateFrom.trim().isEmpty();
            boolean hasTo = dateTo != null && !dateTo.trim().isEmpty();
            if (hasKeyword) {
                sql.append(" AND (file_name LIKE ? OR file_path LIKE ?)");
            }
            if (hasStatus) {
                sql.append(" AND status = ?");
            }
            if (hasFrom) {
                sql.append(" AND date(time) >= ?");
            }
            if (hasTo) {
                sql.append(" AND date(time) <= ?");
            }
            sql.append(" ORDER BY time DESC");
            try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
                int index = 1;
                if (hasKeyword) {
                    String like = "%" + keyword.trim() + "%";
                    statement.setString(index++, like);
                    statement.setString(index++, like);
                }
                if (hasStatus) {
                    statement.setString(index++, status.trim());
                }
                if (hasFrom) {
                    statement.setString(index++, dateFrom.trim());
                }
                if (hasTo) {
                    statement.setString(index, dateTo.trim());
                }
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        records.add(new ConvertRecord(
                                resultSet.getString("time"),
                                resultSet.getString("file_name"),
                                resultSet.getString("file_path"),
                                resultSet.getString("status")));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("查询转换历史失败：" + e.getMessage());
        }
        return records;
    }

    /**
     * 获取可用连接，失效时重建
     */
    private static void ensureConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            initTable();
        }
    }

    /**
     * 初始化表结构
     */
    private static void initTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS convert_history (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "time TEXT NOT NULL, " +
                            "file_name TEXT NOT NULL, " +
                            "file_path TEXT NOT NULL, " +
                            "status TEXT NOT NULL)");
            statement.executeUpdate(
                    "CREATE INDEX IF NOT EXISTS idx_history_time ON convert_history(time)");
        }
    }
}
