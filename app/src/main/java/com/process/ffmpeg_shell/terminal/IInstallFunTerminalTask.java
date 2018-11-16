package com.process.ffmpeg_shell.terminal;

/**
 * 安装功能终端(二进制)加载器任务接口
 * Created by kerwin on 2018/11/14
 */
public interface IInstallFunTerminalTask {
    /**
     * 调用进行功能终端(二进制)安装初始化
     * */
    IInstallFunTerminalTask initialized();

    /**
     * 调用进行功能终端(二进制)释放
     * */
    void release();

    /**
     * 设置安装终端监听器实例
     * @param listener 安装终端监听器实例
     * */
    IInstallFunTerminalTask setOnInstallFunTerminalListener(OnInstallFunTerminalListener listener);


    /** 安装功能终端(二进制)结果实体类 */
    class InstallFunTerminalResult {
        /** 安装结果为无状态 */
        public static final int CODE_NONE = 0;
        /** 安装结果为错误状态 */
        public static final int CODE_ERROR = 1;
        /** 安装结果为完成状态 */
        public static final int CODE_FINISH = 2;

        /** 安装功能终端(二进制)代码值 */
        private int mCode = CODE_NONE;

        /** 安装功能终端路径 */
        private String mPath = "";

        InstallFunTerminalResult() { }

        InstallFunTerminalResult setCode(int code) {
            this.mCode = code;
            return this;
        }

        public int getCode() {
            return this.mCode;
        }

        InstallFunTerminalResult setPath(String path) {
            this.mPath = path;
            return this;
        }

        public String getPath() {
            return this.mPath;
        }
    }

    /**
     * 安装功能终端事件监听器
     * */
    interface OnInstallFunTerminalListener {
        /**
         * 安装功能终端完成时触发回调
         * @param result 安装终端(二进制)结果实体
         * */
        void onInstallFunTerminal(InstallFunTerminalResult result);
    }
}
