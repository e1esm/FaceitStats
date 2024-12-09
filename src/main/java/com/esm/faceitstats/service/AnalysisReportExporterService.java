package com.esm.faceitstats.service;


import com.esm.faceitstats.dto.AnalysisMap;
import com.esm.faceitstats.dto.AnalysisMapCategories;
import com.esm.faceitstats.dto.AnalysisUser;
import com.esm.faceitstats.dto.AnalysisUserCategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class AnalysisReportExporterService {

    private StorageService storageService;

    @Autowired
    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    public void save(String dateStart, String dateEnd,
                     AnalysisMapCategories analysisMapCategories,
                     AnalysisUserCategories analysisUserCategories) {

        String html = generateHtml(dateStart, dateEnd, analysisMapCategories, analysisUserCategories);
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());

        this.storageService.saveFile(String.format("Report(%s_%s).html", dateStart, dateEnd), inputStream);
    }

    private String generateHtml(String dateStart, String dateEnd,
                                       AnalysisMapCategories analysisMapCategories,
                                       AnalysisUserCategories analysisUserCategories) {
        String title = "Stats Report (" + dateStart + " - " + dateEnd + ")";
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>
                    """ + title + """
                    </title>
                    <style>
                        body {
                            background-color: #232323;
                            margin: 0;
                            padding: 0;
                            font-family: Arial, sans-serif;
                            overflow-y: scroll;
                            color: white;
                        }
                        h1 {
                            text-align: center;
                            margin: 20px 0;
                        }
                        .table-container {
                            width: 100%;
                            display: flex;
                            justify-content: center;
                            margin: 20px 0;
                        }
                        table {
                            box-shadow: 10px 10px 10px rgba(0, 0, 0, 0.1);
                            width: 70%;
                            border-collapse: collapse;
                            margin-bottom: 20px;
                        }
                        th, td {
                            color: white;
                            border: 1px solid #ff6d00;
                            padding: 8px;
                            text-align: left;
                        }
                        th {
                            background-color: #676767;
                        }
                        caption {
                            color: white;
                            font-weight: bold;
                            font-size: 1.2em;
                            margin-bottom: 10px;
                        }
                    </style>
                </head>
                <body>
                    <h1>
                    """ + title + """
                    </h1>
                    <div class="table-container">
                        <table>
                            <caption>Most Reliable Maps</caption>
                            <thead>
                                <tr>
                                    <th>Map</th>
                                    <th>Reliability Rate</th>
                                </tr>
                            </thead>
                            <tbody>
                                """ + generateTableRowsForMap(analysisMapCategories.getMostReliableMaps()) + """
                            </tbody>
                        </table>
                    </div>
                    <div class="table-container">
                        <table>
                            <caption>Least Reliable Maps</caption>
                            <thead>
                                <tr>
                                    <th>Map</th>
                                    <th>Reliability Rate</th>
                                </tr>
                            </thead>
                            <tbody>
                                """ + generateTableRowsForMap(analysisMapCategories.getLeastReliableMaps()) + """
                            </tbody>
                        </table>
                    </div>
                    <div class="table-container">
                        <table>
                            <caption>Most Reliable Users</caption>
                            <thead>
                                <tr>
                                    <th>Username</th>
                                    <th>ID</th>
                                    <th>Reliability Rate</th>
                                </tr>
                            </thead>
                            <tbody>
                                """ + generateTableRowsForUsers(analysisUserCategories.getMostReliablePlayers()) + """
                            </tbody>
                        </table>
                    </div>
                    <div class="table-container">
                        <table>
                            <caption>Least Reliable Users</caption>
                            <thead>
                                <tr>
                                    <th>Username</th>
                                    <th>ID</th>
                                    <th>Reliability Rate</th>
                                </tr>
                            </thead>
                            <tbody>
                                """ + generateTableRowsForUsers(analysisUserCategories.getLeastReliablePlayers()) + """
                            </tbody>
                        </table>
                    </div>
                </body>
                </html>
                """;
    }

    private static String generateTableRowsForUsers(List<AnalysisUser> users) {
        StringBuilder rows = new StringBuilder();
        for (AnalysisUser user : users) {
            rows.append("<tr>");
            rows.append("<td>").append(user.getUsername()).append("</td>");
            rows.append("<td>").append(user.getId()).append("</td>");
            rows.append("<td>").append(user.getOverallPredictionsFailed() / user.getOverallMatchesPlayed()).append("</td>");
            rows.append("</tr>");
        }
        return rows.toString();
    }

    private static String generateTableRowsForMap(List<AnalysisMap> maps) {
        StringBuilder rows = new StringBuilder();
        for (AnalysisMap map : maps) {
            rows.append("<tr>");
            rows.append("<td>").append(map.getMap()).append("</td>");
            rows.append("<td>").append(map.getOverallPredictionFailed() / map.getOverallPlayed()).append("</td>");
            rows.append("</tr>");
        }
        return rows.toString();
    }
}
