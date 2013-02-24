package com.acrussell.learnalanguage;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class EditorPanel extends JPanel {
    private List<File> openFiles = new ArrayList<File>();
    private JTabbedPane tabs = new JTabbedPane();
    private Preferences prefs = Preferences.userNodeForPackage(this.getClass());

    public EditorPanel() {
        super(new BorderLayout());
        this.add(tabs);
        jsyntaxpane.DefaultSyntaxKit.initKit();

        for (File f : Files.getOpenFiles()) {
            addFile(f);
        }

        File lastOpenFile = Files.getLastOpenFile();

        int openFileIndex = openFiles.indexOf(lastOpenFile);

        if (openFileIndex > -1) {
            tabs.setSelectedIndex(openFileIndex);
        }
    }

    public File getOpenFile() {
        int index = tabs.getSelectedIndex();
        return index > -1 ? openFiles.get(index) : null;
    }

    public void addFile(File newFile) {
        if (!openFiles.contains(newFile)) {
            JEditorPane editor = new JEditorPane();

            JScrollPane sp = new JScrollPane(editor);
            editor.setContentType("text/java");
            tabs.addTab(newFile.getName(), sp);
            editor.setText(Files.readFile(newFile));

            openFiles.add(newFile);
        }
    }

    public void removeFile(File file) {
        int index = openFiles.indexOf(file);
        if (index > -1) {
            tabs.remove(index);
        }
    }
    
    private void writeFile(File file) {
        int index = openFiles.indexOf(file);
        JScrollPane jsp = (JScrollPane) tabs.getComponentAt(index);
        JEditorPane editor = (JEditorPane) jsp.getViewport().getView();
        
        try {
            Files.writeToFile(editor.getText(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }       
    }
    
    public boolean confirmSave(File file) {
        writeFile(file);
        return true; // TODO Make this ask the user first
    }
    
    public void cleanUpFiles() {
        List<File> filesToReopen = new ArrayList<File>();
        for (File f : openFiles) {
            boolean isSaved = confirmSave(f);
            if (isSaved) {
                filesToReopen.add(f);
            }
        }
        
        StringBuilder builder = new StringBuilder();
        for (File f : filesToReopen) {
            builder.append(f.getAbsolutePath());
            builder.append(";");
        }
        System.out.println(builder.toString());
        prefs.put(Settings.OPEN_FILES, builder.toString());
    }
}
