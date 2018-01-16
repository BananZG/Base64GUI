import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Base64;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainWindow {

	protected Shell shell;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 200);
		shell.setText("Base64 Coder");

		Group grpType = new Group(shell, SWT.NONE);
		grpType.setText("Type:");
		grpType.setBounds(10, 10, 414, 49);

		Button btnEncode = new Button(grpType, SWT.RADIO);
		btnEncode.setBounds(10, 23, 90, 16);
		btnEncode.setText("Encode");

		Button btnDecode = new Button(grpType, SWT.RADIO);
		btnDecode.setBounds(106, 23, 90, 16);
		btnDecode.setText("Decode");

		Label label = new Label(shell, SWT.RIGHT);
		label.setText("Source Text:");
		label.setBounds(10, 65, 75, 15);

		Text txtSourceText = new Text(shell, SWT.BORDER);
		txtSourceText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// need to do it asynchronously because there is another mouse down action by
				// default.
				Display.getDefault().asyncExec(() -> {
					txtSourceText.selectAll();
				});
			}
		});
		txtSourceText.setBounds(91, 62, 333, 21);

		Button btnProcess = new Button(shell, SWT.NONE);
		btnProcess.setBounds(91, 89, 75, 25);
		btnProcess.setText("Process");

		Label lblWarningmsg = new Label(shell, SWT.NONE);
		lblWarningmsg.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lblWarningmsg.setBounds(172, 94, 252, 15);

		Label label_1 = new Label(shell, SWT.RIGHT);
		label_1.setText("Output:");
		label_1.setBounds(10, 120, 75, 15);

		Text lblOutputtext = new Text(shell, SWT.NONE);
		lblOutputtext.setEditable(false);
		lblOutputtext.setBounds(91, 120, 333, 15);

		btnProcess.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!btnEncode.getSelection() ^ btnDecode.getSelection()) {
					lblWarningmsg.setText("Please choose one option bro.");
					lblOutputtext.setText("");
					return;
				}
				if (txtSourceText.getText() == null || txtSourceText.getText().length() == 0) {
					lblWarningmsg.setText("Please input source text.");
					lblOutputtext.setText("");
					return;
				}
				String outputText = "";
				if (btnEncode.getSelection()) {
					outputText = Base64.getEncoder().encodeToString(txtSourceText.getText().getBytes());
				} else if (btnDecode.getSelection()) {
					outputText = new String(Base64.getDecoder().decode(txtSourceText.getText().getBytes()));
				}
				lblOutputtext.setText(outputText);

				// Copy Result to System Clipboard.
				StringSelection stringSelection = new StringSelection(outputText);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
				lblWarningmsg.setText("Output has been copied to System Clipboard.");
			}
		});

	}
}
