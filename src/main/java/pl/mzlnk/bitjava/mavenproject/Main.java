package pl.mzlnk.bitjava.mavenproject;

import pl.mzlnk.bitjava.fileutil.FileUtil;
import pl.mzlnk.bitjava.fileutil.FileUtilImpl;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        FileUtil fileUtil = new FileUtilImpl();
        fileUtil.saveFile(new File("test.txt"));
    }

}
