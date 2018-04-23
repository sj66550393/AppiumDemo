package utils;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import factory.LogFactory;

public class Log {
    // 自定义的全局log（个人一般用这个记录）
    public static Logger log = LogFactory.getGlobalLog();
    // Jdk1.7以后自带的全局log（后面我添加了FileHandler，用于写入文件日志）
    private static Logger sysLog = Logger.getGlobal();

    static {
        LogUtils.addFileHandler(sysLog, Level.INFO, LogFactory.LOG_FOLDER + File.separator + "sys.log");
    }
	
}
