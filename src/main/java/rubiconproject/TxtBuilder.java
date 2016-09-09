package rubiconproject;

import java.io.*;
/**
 * <p>
 * Copyright © Hengming Dai
 */
public class TxtBuilder {
    // use to build Txt output
    public void buildTxt(String pathToDirectory, String outputFile) throws Exception {
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
        BufferedReader reader = new BufferedReader(new FileReader(pathToDirectory));

        String inputFileSuffix = pathToDirectory.substring(pathToDirectory.lastIndexOf(".") + 1, pathToDirectory.length());

        if (inputFileSuffix.equals("json")) {
            convertJsonToTxt(dataOutputStream, reader, pathToDirectory);
        } else if (inputFileSuffix.equals("csv")) {
            convertCvsToTxt(dataOutputStream, reader, pathToDirectory);
        } else {
            throw new Exception("Input format is not csv or json");
        }

        outputTextSuffix(dataOutputStream);
        reader.close();
    }

    private void convertCvsToTxt(DataOutputStream dataOutputStream, BufferedReader reader, String pathToDirectory)
            throws IOException {

        outputTextPrefix(dataOutputStream, pathToDirectory);
        String[] titles = reader.readLine().split(",");
        String line = null;
        boolean notFirstJson = false;
        while((line = reader.readLine()) != null){
            String data[] = line.split("，");
            String[] contents = data[data.length-1].split(",");
            if (!contentCheck(notFirstJson, contents, titles, dataOutputStream)) continue;
            for (int i = 0; i < contents.length; i++) {
                // keyword row should before the score row
                if (titles[i].equals("score"))  outputToFile(dataOutputStream, "keyword", "Hello Guest");
                outputToFile(dataOutputStream, titles[i], contents[i]);
            }
            notFirstJson = true;
        }
    }

    private boolean contentCheck(boolean notFirstJson, String[] contents, String[] titles, DataOutputStream dataOutputStream)
            throws IOException {

        if (notFirstJson && contents.length != titles.length) return false;  // the contents data is not valid
        if (notFirstJson && contents.length == titles.length) dataOutputStream.writeBytes("    },\n");
        dataOutputStream.writeBytes("    {\n");
        dataOutputStream.flush();
        return true;
    }

    private void convertJsonToTxt(DataOutputStream dataOutputStream, BufferedReader reader, String pathToDirectory)
            throws IOException {

        outputTextPrefix(dataOutputStream, pathToDirectory);
        String line = null;
        boolean notFirstJson = false;

        while((line = reader.readLine()) != null){
            String data[] = line.split("，");
            String[] contents = data[data.length-1].split(",");
            if (notFirstJson && contents.length == 4) dataOutputStream.writeBytes("    },\n"); // check content is valid or not
            for (int i = 0; i < contents.length; i++) {
                if (contents[i].equals("[") || contents[i].equals("]")) continue;
                if (i == 0) {
                    int loc = contents[i].indexOf("{");
                    dataOutputStream.writeBytes("  "+contents[i].substring(0, loc + 1) + "\n");
                    dataOutputStream.writeBytes("      " + contents[i].substring(loc + 1) + "\n");
                } else if (i == contents.length - 1) {
                    // keyword row should before the score row
                    notFirstJson = true;
                    dataOutputStream.writeBytes("      \"keyword"  + "\": \"" + "Hello Guest" + "\"\n");
                    int loc = contents[i].indexOf("}");
                    dataOutputStream.writeBytes("     " + contents[i].substring(0, loc) + "\n");
                } else {
                    dataOutputStream.writeBytes("     " + contents[i] + "\n");
                }
            }
        }
    }

    private void outputTextPrefix(DataOutputStream dataOutputStream, String pathToDirectory) throws IOException {
        dataOutputStream.writeBytes("{\n");
        dataOutputStream.writeBytes("  \"collectionId\": " + "\"" + pathToDirectory + "\",\n");
        dataOutputStream.writeBytes("  \"sites\": [\n");
    }

    private void outputToFile(DataOutputStream dataOutputStream, String title, String content) throws IOException {
        dataOutputStream.writeBytes("      \"" + title + "\": \"" + content + "\"\n");
    }

    private void outputTextSuffix(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeBytes("    }\n");
        dataOutputStream.writeBytes("  ]\n");
        dataOutputStream.writeBytes("}\n");
        dataOutputStream.flush();
        dataOutputStream.close();
    }
}