package net.lm.ied.sqlitedatabase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * 功能：数据库与表操作演示
 * 作者：liumce
 * 日期：2017年12月14日
 */
public class MainActivity extends Activity {

    /**
     *数据库名
     */
    private final String DB_NAME = "student.db";
    /**
     * 表名
     */
    private final String TABLE_NAME = "student";
    /**
     * 文件访问模式 私有模式
     */
    private final int MODE = Context.MODE_PRIVATE;
    /**
     * SQLite数据库
     */
    private SQLiteDatabase db;
    /**
     * 学号
     */
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //利用资源布局文件设置用户界面
        setContentView(R.layout.activity_main);
    }
    /**
     * 创建或打开数据库
     *
     * @param view
     */
    public void doCreateOrOpenDB(View view) {
    // 判断数据库是否存在
        if (databaseList().length == 0) {
    // 创建数据库（数据库名，访问模式，游标工厂）
            db = openOrCreateDatabase(DB_NAME, MODE, null);
    // 提示用户数据库创建成功
            Toast.makeText(this, "恭喜，数据库【" + DB_NAME + "】创建成功！", Toast.LENGTH_LONG).show();
        } else {
    // 打开数据库（数据库名，访问模式，游标工厂）
            db = openOrCreateDatabase(DB_NAME, MODE, null);
    // 提示用户数据库打开成功
            Toast.makeText(this, "恭喜，数据库【" + DB_NAME + "】打开成功！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 创建表
     *
     * @param view
     */
    public void doCreateTable(View view) {
     // 判断数据库对象是否为空
        if (db == null) {
     // 判断数据库是否存在
            if (databaseList().length == 0) {
                Toast.makeText(this, "请创建数据库【" + DB_NAME + "】。", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "请打开数据库【" + DB_NAME + "】。", Toast.LENGTH_LONG).show();
            }
        } else {
        // 判断表是否存在
            if (isTableExisted(TABLE_NAME)) {
        // 弹出吐司提示用户表已存在
                Toast.makeText(this, "表【" + TABLE_NAME + "】已经存在！", Toast.LENGTH_LONG).show();
            } else {
                try {
        // 定义SQL字符串
                    String strSQL = "CREATE TABLE " + TABLE_NAME + "(id integer, name text, gender text)";
        // 执行SQL语句
                    db.execSQL(strSQL);
        // 提示用户创建表成功
                    Toast.makeText(this, "创建表成功！", Toast.LENGTH_LONG).show();
                } catch (SQLException e) {
        // 提示用户创建表失败
                    Toast.makeText(this, "创建表失败！", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 判断表是否存在
     *
     * @param tableName
     * @return true 表存在；false 表不存在
     */
    private boolean isTableExisted(String tableName) {
        // 定义SQL字符串
        String strSQL = "SELECT * FROM sqlite_master WHERE type = ? AND name = ?";
        // 执行SQL查询，返回游标
        Cursor cursor = db.rawQuery(strSQL, new String[] { "table", tableName });
        // 判断游标里是否有记录
        //return cursor.getConut() >0;
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 添加表记录
     *Toast.LENGTH_LONG=3500
     * Toast.LENGTH_SHORT=2000
     * @param view
     */
    public void doAddRecord(View view) {
    // 判断数据库对象是否为空
        if (db == null) {
    // 判断数据库是否存在
            if (databaseList().length == 0) {
                Toast.makeText(this, "请创建数据库【" + DB_NAME + "】。", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "请打开数据库【" + DB_NAME + "】。", Toast.LENGTH_LONG).show();
            }
        } else {
            // 判断表是否存在
            if (isTableExisted(TABLE_NAME)) {
            // 获取新记录的学号
                id = getNewId(TABLE_NAME);
            // 创建内容值对象
                ContentValues values = new ContentValues();
            // 以键值对方式添加字段数据
                values.put("id", id);
                values.put("name", "学生" + id);
                values.put("gender", id % 2 == 1 ? "男" : "女");
            // 将数据插入表中
                long count = db.insert(TABLE_NAME, null, values);
                if (count != -1) {
            // 弹出吐司提示用户添加成功
                    Toast.makeText(this, "恭喜，表记录添加成功！", Toast.LENGTH_LONG).show();
                } else {
            // 弹出吐司提示用户添加失败
                    Toast.makeText(this, "恭喜，表记录添加失败！", Toast.LENGTH_LONG).show();
                }
            } else {
                // 提示用户先创建表
                Toast.makeText(this, "表【" + TABLE_NAME + "】不存在，请先创建！", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 显示全部表记录
     *
     * @param view
     */
    public void doDisplayAllRecords(View view) {
    // 判断数据库对象是否为空  逻辑对象
        if (db == null) {
    // 判断数据库是否存在  物理概念
            if (databaseList().length == 0) {
                Toast.makeText(this, "请创建数据库【" + DB_NAME + "】。", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "请打开数据库【" + DB_NAME + "】。", Toast.LENGTH_LONG).show();
            }
        } else {
        // 判断表是否存在
            if (isTableExisted(TABLE_NAME)) {
        // 判断表里是否有记录
                if (getRecordCount(TABLE_NAME) > 0) {
        // 定义SQL字符串
                    String strSQL = "SELECT * FROM " + TABLE_NAME;
        // 执行SQL，返回游标
                    Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        // 定义字符串生成器
                    StringBuilder builder = new StringBuilder();
        // 遍历游标对象（记录集）
                    while (cursor.moveToNext()) {
        // 将每条记录信息组合之后添加到字符串生成器
                        builder.append(cursor.getInt(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + "\n");
                    }
        // 弹出吐司显示全部学生记录
                    Toast.makeText(this, "全部表记录\n\n" + builder.toString(), Toast.LENGTH_LONG).show();
                } else {
            // 弹出吐司提示用户没有表记录
                    Toast.makeText(this, "没有表记录可显示，请先添加表记录！", Toast.LENGTH_LONG).show();
                }
            } else {
        // 提示用户先创建表
                Toast.makeText(this, "表【" + TABLE_NAME + "】不存在，请先创建！", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 更新表记录 修改第一条记录，名字改为“张晓芸”，性别改为“女” 三个层面的判断：数据库-->表-->记录
     *
     * @param view
     */
    public void doUpdateRecord(View view) {
        // 判断数据库对象是否为空
        if (db == null) {
            // 判断数据库是否存在
            if (databaseList().length == 0) {
                Toast.makeText(this, "请创建数据库【" + DB_NAME + "】。", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "请打开数据库【" + DB_NAME + "】。", Toast.LENGTH_LONG).show();
            }
        } else {
            // 判断表是否存在
            if (isTableExisted(TABLE_NAME)) {
                    // 判断是否有表记录
                if (getRecordCount(TABLE_NAME) > 0) {
                    // 定义SQL字符串
                    String strSQL = "UPDATE " + TABLE_NAME + " SET name = ?, gender = ? WHERE id = ?";
                    try {
                        // 执行SQL，返回更新表记录数
                        db.execSQL(strSQL, new Object[] { "张晓芸", "女", 1 });
                        // 提示用户更新记录成功
                        Toast.makeText(this, "恭喜，表记录更新成功！", Toast.LENGTH_LONG).show();
                    } catch (SQLException e) {
                        // 提示用户更新记录失败
                        Toast.makeText(this, "遗憾，表记录更新失败！", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // 弹出吐司提示用户没有表记录
                    Toast.makeText(this, "没有表记录可更新，请先添加表记录！", Toast.LENGTH_LONG).show();
                }
            } else {
                // 提示用户先创建表
                Toast.makeText(this, "表【" + TABLE_NAME + "】不存在，请先创建！", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 删除全部表记录（三个层面的判断）
     *
     * @param view
     */
    public void doDeleteAllRecords(View view) {
    // 判断数据库对象是否为空
        if (db == null) {
            // 判断数据库是否存在
            if (databaseList().length == 0) {
                Toast.makeText(this, "请创建数据库【" + DB_NAME + "】。", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "请打开数据库【" + DB_NAME + "】。", Toast.LENGTH_LONG).show();
            }
        } else {
            // 判断表是否存在
            if (isTableExisted(TABLE_NAME)) {
                // 判断是否有表记录
                if (getRecordCount(TABLE_NAME) > 0) {
                    // 定义SQL字符串
                    String strSQL = "DELETE FROM " + TABLE_NAME;
                    try {
                        // 执行SQL，删除表记录
                        db.execSQL(strSQL);
                        // 提示用户删除表记录成功
                        Toast.makeText(this, "全部表记录已删除！", Toast.LENGTH_LONG).show();
                    } catch (SQLException e) {
                        // 提示用户删除表记录失败
                        Toast.makeText(this, "删除表记录失败！", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // 弹出吐司提示用户没有表记录
                    Toast.makeText(this, "没有表记录可删除，请先添加表记录！", Toast.LENGTH_LONG).show();
                }
            } else {
                    // 提示用户先创建表
                Toast.makeText(this, "表【" + TABLE_NAME + "】不存在，请先创建！", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 删除表
     *
     * @param view
     */
    public void doDeleteTable(View view) {
        // 判断数据库对象是否为空
        if (db == null) {
        // 判断数据库是否存在
            if (databaseList().length == 0) {
                Toast.makeText(this, "请创建数据库【" + DB_NAME + "】。", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "请打开数据库【" + DB_NAME + "】。", Toast.LENGTH_LONG).show();
            }
        } else {
            // 判断表是否存在
            if (isTableExisted(TABLE_NAME)) {
            // 定义SQL字符串
                String strSQL = "DROP TABLE " + TABLE_NAME;
                try {
                    // 执行SQL，删除表
                    db.execSQL(strSQL);
                    // 提示用户表删除成功
                    Toast.makeText(this, "表删除成功！", Toast.LENGTH_LONG).show();
                } catch (SQLException e) {
                    // 提示用户表删除失败
                    Toast.makeText(this, "表删除失败！", Toast.LENGTH_LONG).show();
                }
            } else {
                    // 提示用户先创建表
                Toast.makeText(this, "表【" + TABLE_NAME + "】不存在，请先创建！", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 删除数据库
     *
     * @param view
     */
    public void doDeleteDB(View view) {
        // 判断数据库是否存在
        if (databaseList().length == 0) {
        // 提示用户没有数据库
            Toast.makeText(this, "没有数据库可删除！", Toast.LENGTH_LONG).show();
        } else {
            // 判断数据库删除是否成功
            if (deleteDatabase(DB_NAME)) {
                // 提示用户删除成功
                Toast.makeText(this, "数据库【" + DB_NAME + "】删除成功！", Toast.LENGTH_LONG).show();
            } else {
                // 提示用户删除失败
                Toast.makeText(this, "数据库【" + DB_NAME + "】删除失败！", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 返回表记录数
     *
     * @param tableName
     * @return 表记录数
     */
    private int getRecordCount(String tableName) {
        // 定义SQL字符串
        String strSQL = "SELECT * FROM " + tableName;
        // 执行SQL，返回游标
        Cursor cursor = db.rawQuery(strSQL, null);
        // 返回记录数
        return cursor.getCount();
    }

    /**
     * 获取新记录的学号
     *
     * @param tableName
     * @return
     */
    private int getNewId(String tableName) {
        // 判断表是否存在
        if (isTableExisted(tableName)) {
            // 查询全部表记录，返回游标
            Cursor cursor = db.query(tableName, null, null, null, null, null, null);
            // 移到最后一条记录
            if (cursor.moveToLast()) {
                // 获取最后一条记录的id
                int id = cursor.getInt(0);
                // 返回新记录的id
                return id + 1;
            }
        }
        return 1;
    }

}
