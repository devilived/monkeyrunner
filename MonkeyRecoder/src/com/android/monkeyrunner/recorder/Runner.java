package com.android.monkeyrunner.recorder;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.python.modules.thread.thread;

import com.android.monkeyrunner.MonkeyFormatter;
import com.android.monkeyrunner.MonkeyRunnerOptions;
import com.android.monkeyrunner.MonkeyRunnerStarter;

public class Runner extends JFrame implements ActionListener {
	private JPanel jContentPane = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JButton jButton2 = null;
	private MonkeyRecorder f1 = null;
	private static String fileString = "";
	File[] files;
	private static int j = 0;
	private static int flag = 0;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	private JButton getJButton() {

		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(32, 22, 115, 35));
			jButton.setText("选择单个文件");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new Thread() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							super.run();
							JFileChooser file = new JFileChooser(".");
							file.setAcceptAllFileFilterUsed(false);
							file.addChoosableFileFilter(new FileFilter() {

								@Override
								public String getDescription() {
									return null;
								}

								@Override
								public boolean accept(File f) {
									return f.getName().endsWith("");
								}
							});
							int result = file.showOpenDialog(null);
							String path = null;
							if (result == JFileChooser.APPROVE_OPTION) {
								path = file.getSelectedFile().getAbsolutePath();
							} else {
								return;
							}
							if (path == null) {
								return;
							}
							System.out.println("Script Path :");
							System.out.println(path);
							String[] margs = null;
							MonkeyRunnerOptions options = MonkeyRunnerOptions
									.processOptions(margs, path);
							if (options == null) {
								return;
							}
							replaceAllLogFormatters(
									MonkeyFormatter.DEFAULT_INSTANCE,
									options.getLogLevel());
							MonkeyRunnerStarter runner = new MonkeyRunnerStarter(
									options);
							int error = runner.run();
							System.exit(error);
						}

					}.start();

				}
			});
		}
		return jButton;
	}

	private JButton getJButton1() {
		fileString = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath();
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new Rectangle(152, 22, 115, 35));
			jButton1.setText("选择多个文件");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					new Thread() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							super.run();
							JFileChooser fileChooser = new JFileChooser(".");
							fileChooser
									.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
							int returnVal = fileChooser
									.showOpenDialog(fileChooser);
							if (returnVal == JFileChooser.APPROVE_OPTION) {
								// System.out.println(fileChooser.getSelectedFile()
								// .getAbsolutePath());// 这个就是你选择的文件夹的路径

								File root = new File(fileChooser
										.getSelectedFile().getAbsolutePath());
								files = root.listFiles();
								for (File file : files) {
									j++;

									// System.out.println("显示"
									// + fileChooser.getSelectedFile()
									// .getAbsolutePath() + "下所有子目录"
									// + file.getAbsolutePath());
								}
								String path1 = "";
								String path = "";
								for (int i = 0; i < j; i++) {
									if ((files[i].toString().substring(files[i]
											.toString().indexOf(".")))
											.equals(".mr")) {
										flag = 1;
										path1 = path1 + files[i] + "|";
									}
								}
								// System.out.println(path1);
								if (flag == 1) {
									path = path1.substring(0,
											path1.lastIndexOf("|"));
									flag = 0;
								}

								if (path == null) {
									return;
								}

								if (path.equals("")) {
									JOptionPane.showMessageDialog(null,
											"不存在mr文件 ! ");
								} else {

									System.out.println("Script Path ：");
									for (int i = 0; i < j; i++) {
										System.out.println(files[i]);
									}

									String[] margs = null;
									MonkeyRunnerOptions options = MonkeyRunnerOptions
											.processOptions(margs, path);
									if (options == null) {
										return;
									}
									replaceAllLogFormatters(
											MonkeyFormatter.DEFAULT_INSTANCE,
											options.getLogLevel());
									MonkeyRunnerStarter runner = new MonkeyRunnerStarter(
											options);
									int error = runner.run();
									System.exit(error);
								}
							}
						}

					}.start();

				}
			});
		}
		return jButton1;
	}

	private JButton getJButton2() {

		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setBounds(new Rectangle(92, 95, 105, 35));
			jButton2.setText("结束运行");
			jButton2.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// new Thread() {
					// @Override
					// public void run() {
					// TODO Auto-generated method stub
					// super.run();
					Runtime rt = Runtime.getRuntime();
					// String[] command1=new
					// String[]{"cmd","cd","C://Program Files//Thunder"};
					String command = "taskkill /F /IM MonkeyRunner.exe";
					System.exit(0);
					try {
						// rt.exec(command1);//返回一个进程
						rt.exec(command);
						System.out.println("success closed");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					// }

					// }.start();

				}

			});
		}
		return jButton2;
	}

	public Runner() {
		super();
		initialize();
	}

	public Runner(MonkeyRecorder f) {
		this();
		f1 = f;
	}

	private static final void replaceAllLogFormatters(Formatter form,
			Level level) {
		LogManager mgr = LogManager.getLogManager();
		Enumeration<String> loggerNames = mgr.getLoggerNames();
		while (loggerNames.hasMoreElements()) {
			String loggerName = loggerNames.nextElement();
			Logger logger = mgr.getLogger(loggerName);
			for (Handler handler : logger.getHandlers()) {
				handler.setFormatter(form);
				handler.setLevel(level);
			}
		}
	}

	private void initialize() {
		this.setSize(300, 180);
		this.setContentPane(getJContentPane());
		this.setTitle("运行");
		this.setResizable(false);

	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);

			jContentPane.add(getJButton(), null);
			jContentPane.add(getJButton1(), null);
			jContentPane.add(getJButton2(), null);

		}
		return jContentPane;
	}
}
