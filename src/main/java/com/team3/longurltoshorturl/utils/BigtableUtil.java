package com.team3.longurltoshorturl.utils;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BigtableUtil {
    private static String COLUMN_FAMILY ;
    private static String tableId ;
    private static String projectId ;
    private static String instanceId ;

    public static String getTableId() {
        return tableId;
    }

    @Value("${gcp.bigtable.tableId}")
    public void setTableId(String tableId) {
        BigtableUtil.tableId = tableId;
    }

    public static String getProjectId() {
        return projectId;
    }

    @Value("${gcp.bigtable.projectId}")
    public void setProjectId(String projectId) {
        BigtableUtil.projectId = projectId;
    }

    public static String getInstanceId() {
        return instanceId;
    }

    @Value("${gcp.bigtable.instanceId}")
    public  void setInstanceId(String instanceId) {
        BigtableUtil.instanceId = instanceId;
    }

    public static String getCOLUMN_FAMILY() {
        return COLUMN_FAMILY;
    }

    @Value("${gcp.bigtable.COLUMN_FAMILY}")
    public  void setCOLUMN_FAMILY(String COLUMN_FAMILY) {
        BigtableUtil.COLUMN_FAMILY = COLUMN_FAMILY;
    }


    public static BigtableDataClient generateBigTableDataClient() throws IOException {
        BigtableDataSettings settings =
                BigtableDataSettings.newBuilder().setProjectId(projectId).setInstanceId(instanceId).build();
        return BigtableDataClient.create(settings);
    }

    public static void close(BigtableDataClient dataClient) {
        dataClient.close();
    }
}
