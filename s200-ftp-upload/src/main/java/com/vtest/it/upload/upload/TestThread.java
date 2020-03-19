package com.vtest.it.upload.upload;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class TestThread {
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        for (int i = 0; i < 105; i++) {
            if (i < 10) {
                File file = new File("D:/Data/VTEST_CP_AMC_GXLX2_A_T45K95.00_T45K95-11B4_TTO-01_POS-08_GXLX2-J750-PC-03_CP1_RP0_1079_20191229_154" + 00 + i + ".sum");
                file.createNewFile();
            }
            if (i >= 10 && i <= 99) {
                File file = new File("D:/Data/VTEST_CP_AMC_GXLX2_A_T45K95.00_T45K95-11B4_TTO-01_POS-08_GXLX2-J750-PC-03_CP1_RP0_1079_20191229_1540" + i + ".sum");
                file.createNewFile();
            }
            if (i > 99) {
                File file = new File("D:/Data/VTEST_CP_AMC_GXLX2_A_T45K95.00_T45K95-11B4_TTO-01_POS-08_GXLX2-J750-PC-03_CP1_RP0_1079_20191229_154" + i + ".sum");
                file.createNewFile();
            }
        }
    }

    public void test() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor service = new ThreadPoolExecutor(10, 10, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.AbortPolicy());
        List<Future<String>> list = new LinkedList<Future<String>>();
        for (int i = 0; i < 100; i++) {
            final int a = i;
            Future<String> future = service.submit(new Callable<String>() {
                public String call() throws Exception {
                    if (a % 5 == 0) {
                        throw new Exception(a + "find!");
                    }
                    System.err.println(a);
                    return a + " finished";
                }
            });
            list.add(future);
        }
        for (Future<String> future : list) {
            try {
                System.err.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
    }
}
