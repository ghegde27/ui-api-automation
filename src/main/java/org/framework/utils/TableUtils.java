package org.framework.utils;

import org.framework.driver.core.DriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TableUtils {

    private TableUtils() {
    }

    // =========================================================
    // GET TABLE
    // =========================================================

    public static WebElement getTable(
            By tableLocator
    ) {

        return DriverManager.getDriver()
                .findElement(tableLocator);
    }

    // =========================================================
    // GET ALL ROWS
    // =========================================================

    public static List<WebElement> getRows(
            By tableLocator
    ) {

        return getTable(tableLocator)
                .findElements(By.tagName("tr"));
    }

    // =========================================================
    // GET ROW COUNT
    // =========================================================

    public static int getRowCount(
            By tableLocator
    ) {

        return getRows(tableLocator).size();
    }

    // =========================================================
    // GET COLUMN COUNT
    // =========================================================

    public static int getColumnCount(
            By tableLocator
    ) {

        List<WebElement> rows =
                getRows(tableLocator);

        if (rows.isEmpty()) {

            return 0;
        }

        return rows.get(0)
                .findElements(
                        By.xpath("./th|./td")
                )
                .size();
    }

    // =========================================================
    // GET CELL VALUE
    // =========================================================

    public static String getCellValue(
            By tableLocator,
            int rowNumber,
            int columnNumber
    ) {

        String xpath =
                ".//tr[" + rowNumber + "]"
                        + "/td[" + columnNumber + "]";

        return getTable(tableLocator)
                .findElement(By.xpath(xpath))
                .getText()
                .trim();
    }

    // =========================================================
    // GET HEADER VALUE
    // =========================================================

    public static String getHeaderValue(
            By tableLocator,
            int columnNumber
    ) {

        String xpath =
                ".//tr[1]/th[" + columnNumber + "]";

        return getTable(tableLocator)
                .findElement(By.xpath(xpath))
                .getText()
                .trim();
    }

    // =========================================================
    // PRINT TABLE
    // =========================================================

    public static void printTable(
            By tableLocator
    ) {

        List<WebElement> rows =
                getRows(tableLocator);

        for (int i = 0; i < rows.size(); i++) {

            List<WebElement> cols =
                    rows.get(i)
                            .findElements(
                                    By.xpath("./th|./td")
                            );

            System.out.println(
                    "========== ROW "
                            + (i + 1)
                            + " =========="
            );

            for (WebElement col : cols) {

                System.out.println(
                        col.getText()
                );
            }
        }
    }

    // =========================================================
    // GET ENTIRE TABLE DATA
    // =========================================================

    public static List<List<String>> getTableData(
            By tableLocator
    ) {

        List<List<String>> tableData =
                new ArrayList<>();

        List<WebElement> rows =
                getRows(tableLocator);

        for (WebElement row : rows) {

            List<String> rowData =
                    new ArrayList<>();

            List<WebElement> cols =
                    row.findElements(
                            By.xpath("./th|./td")
                    );

            for (WebElement col : cols) {

                rowData.add(
                        col.getText().trim()
                );
            }

            tableData.add(rowData);
        }

        return tableData;
    }

    // =========================================================
    // FIND ROW BY TEXT
    // =========================================================

    public static WebElement findRowByText(
            By tableLocator,
            String text
    ) {

        List<WebElement> rows =
                getRows(tableLocator);

        for (WebElement row : rows) {

            if (row.getText().contains(text)) {

                return row;
            }
        }

        return null;
    }

    // =========================================================
    // CHECK ROW EXISTS
    // =========================================================

    public static boolean isRowPresent(
            By tableLocator,
            String text
    ) {

        return findRowByText(
                tableLocator,
                text
        ) != null;
    }

    // =========================================================
    // CLICK CELL LINK
    // =========================================================

    public static void clickCellLink(
            By tableLocator,
            int rowNumber,
            int columnNumber
    ) {

        String xpath =
                ".//tr[" + rowNumber + "]"
                        + "/td[" + columnNumber + "]//a";

        getTable(tableLocator)
                .findElement(By.xpath(xpath))
                .click();
    }

    // =========================================================
    // GET ROW DATA AS MAP
    // =========================================================

    public static Map<String, String> getRowDataAsMap(
            By tableLocator,
            int rowNumber
    ) {

        Map<String, String> rowMap =
                new HashMap<>();

        List<WebElement> headers =
                getTable(tableLocator)
                        .findElements(
                                By.xpath(".//tr[1]/th")
                        );

        List<WebElement> data =
                getTable(tableLocator)
                        .findElements(
                                By.xpath(
                                        ".//tr[" + rowNumber + "]/td"
                                )
                        );

        for (int i = 0;
             i < headers.size();
             i++) {

            rowMap.put(
                    headers.get(i).getText().trim(),
                    data.get(i).getText().trim()
            );
        }

        return rowMap;
    }

    // =========================================================
    // GET COLUMN DATA
    // =========================================================

    public static List<String> getColumnData(
            By tableLocator,
            int columnNumber
    ) {

        List<String> columnData =
                new ArrayList<>();

        List<WebElement> cells =
                getTable(tableLocator)
                        .findElements(
                                By.xpath(
                                        ".//tr/td[" + columnNumber + "]"
                                )
                        );

        for (WebElement cell : cells) {

            columnData.add(
                    cell.getText().trim()
            );
        }

        return columnData;
    }
}