package com.zyao89.view.zweb.constants;

/**
 * 框架层定义的协议，不许允许修改
 *
 * @author Zyao89
 * @Create 2017/11/6.
 */

public interface InternalFunctionName
{
    String MAIN_CALL_OBJ = "__ZWeb__";

    String INIT_FRAMEWORK   = "initFramework";
    String REQUIRE_CALLBACK = "requireCallback";
    String MESSAGE_CALLBACK = "messageCallback";
    String DATABASE_CALLBACK = "databaseCallback";
    /**
     * 通用方法
     */
    String CALL_RECEIVER    = "callReceiver";
}
