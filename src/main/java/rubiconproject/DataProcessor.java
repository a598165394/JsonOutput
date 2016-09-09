package rubiconproject;

public class DataProcessor {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new Exception("Input format is not right should be `pathToDirectory`, `outputFile`");
        }

        String pathToDirectory = args[0];

        String outputFile = args[1];
        TxtBuilder txtBuilder = new TxtBuilder();
        txtBuilder.buildTxt(pathToDirectory, outputFile);
    }
}