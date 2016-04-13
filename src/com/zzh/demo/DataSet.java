package com.zzh.demo;

import java.io.File;
import java.util.ArrayList;

import com.zzh.util.Constants;
import com.zzh.util.DocX;
import com.zzh.util.FileX;

public class DataSet {

	// root directory that store data
	private String rootDirStr;
	// rootDir FileX
	private File rootDir;

	public DataSet() {
		rootDirStr = Constants.DATAROOTDIR;
		rootDir = new File(rootDirStr);
	}

	// fetch docs
	public ArrayList<DocX> fecthDocs() {
		ArrayList<DocX> docs = new ArrayList<DocX>();
		File[] files = rootDir.listFiles();
		for (int i=0;i<files.length;i++) {
			String title = files[i].getName();
			String content = FileX.getText(files[i]);
			docs.add(new DocX(title, content));
		}
		return docs;
	}

	public static void main(java.lang.String[] args) {
		DataSet dataset = new DataSet();
	}
}
