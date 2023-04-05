package com.team3.longurltoshorturl.repository.Impl;

import com.team3.longurltoshorturl.entity.Url;
import com.team3.longurltoshorturl.repository.UrlRepository;
import com.team3.longurltoshorturl.utils.BigtableUtil;
import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Date;

@Repository
public class UrlRepositoryImpl implements UrlRepository {

    @Override
    public Url save(Url url) throws IOException {
        BigtableUtil bigtableUtil = new BigtableUtil();
        BigtableDataClient bigtableDataClient = BigtableUtil.generateBigTableDataClient();
        try {
            System.out.println("\nWriting some greetings to the table");
                RowMutation rowMutation =
                        RowMutation.create(bigtableUtil.getTableId(), String.valueOf(url.getId()))
                                .setCell(BigtableUtil.getCOLUMN_FAMILY(), "id",String.valueOf(url.getId()))
                                .setCell(BigtableUtil.getCOLUMN_FAMILY(), "long_url", url.getLongUrl())
                                .setCell(BigtableUtil.getCOLUMN_FAMILY(), "created_date", String.valueOf(url.getCreatedDate().getTime()))
                                .setCell(BigtableUtil.getCOLUMN_FAMILY(), "expires_date", String.valueOf(url.getExpiresDate().getTime()));
                bigtableDataClient.mutateRow(rowMutation);
        } catch (NotFoundException e) {
            System.err.println("Failed to write to non-existent table: " + e.getMessage());
        } finally {
            BigtableUtil.close(bigtableDataClient);
        }
        return url;
    }

    @Override
    public Url findById(long id) throws IOException {
        BigtableDataClient bigtableDataClient = BigtableUtil.generateBigTableDataClient();
        Url url = new Url();
        try {
            System.out.println("\nReading a single row by row key");
            Row row = bigtableDataClient.readRow(BigtableUtil.getTableId(), String.valueOf(id));
            System.out.println("Row: " + row.getKey().toStringUtf8());
            System.out.println(row.getCells(BigtableUtil.getCOLUMN_FAMILY(), "id").get(0).getValue().toStringUtf8());
            url.setId(Long.valueOf(row.getCells(BigtableUtil.getCOLUMN_FAMILY(), "id").get(0).getValue().toStringUtf8()));
            url.setLongUrl(row.getCells(BigtableUtil.getCOLUMN_FAMILY(), "long_url").get(0).getValue().toStringUtf8());
            url.setCreatedDate(new Date(Long.valueOf(row.getCells(BigtableUtil.getCOLUMN_FAMILY(), "created_date").get(0).getValue().toStringUtf8())));
            url.setExpiresDate(new Date(Long.valueOf(row.getCells(BigtableUtil.getCOLUMN_FAMILY(), "expires_date").get(0).getValue().toStringUtf8())));
            return url;
        } catch (NotFoundException e) {
            System.err.println("Failed to read from a non-existent table: " + e.getMessage());
            return url;
        } finally {
            BigtableUtil.close(bigtableDataClient);
        }
    }

    @Override
    public void delete(Url url) {

    }
}
