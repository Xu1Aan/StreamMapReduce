package com.xuan.workers;

import com.xuan.messages.BatchMessage;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/03/20/19:53
 * @Description:
 */
public interface Filter {
    BatchMessage onBatchMessage(BatchMessage batchMessage);
}
