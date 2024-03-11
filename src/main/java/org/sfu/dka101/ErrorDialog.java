package org.sfu.dka101;

import javax.swing.*;
import java.awt.event.*;

public class ErrorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel dialogLabel;

    public ErrorDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> dispose());

        // call dispose() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call dispose() on ESCAPE
        contentPane.registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
        );
    }

    public void setDialogLabelText(String labelText) {
        this.dialogLabel.setText(labelText);
    }
}
